package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.baomidou.mybatisplus.plugins.Page;
import com.david.meeting.api.alipay.AlipayServiceApi;
import com.david.meeting.api.alipay.vo.AlipayInfoVO;
import com.david.meeting.api.alipay.vo.AlipayRequestVO;
import com.david.meeting.api.order.OrderServiceApi;
import com.david.meeting.api.order.vo.OrderVO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stylefeng.guns.core.util.TokenBucket;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.common.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author : wangwei
 * @date : Created in 2020/2/23 14:06
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@RequestMapping("/order/")
@RestController()
@Slf4j
public class OrderController {

    private static TokenBucket tokenBucket = new TokenBucket();

    private static final String IMG_PRE = "http://img.meetingshop.cn/";


    @Reference(interfaceClass = OrderServiceApi.class, check = false)
    private OrderServiceApi orderServiceApi;

    @Reference(interfaceClass = AlipayServiceApi.class, check = false)
    private AlipayServiceApi alipayServiceApi;

    /*
        hystrix隔离:
        信号量隔离
        线程池隔离
        线程切换
     */
    @HystrixCommand(fallbackMethod = "error", commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),

            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000"),
            // 滑动统计的桶数量
            /**
             * 设置一个rolling window被划分的数量，若numBuckets＝10，rolling window＝10000，
             *那么一个bucket的时间即1秒。必须符合rolling window % numberBuckets == 0。默认1
             */
//            @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"),
            // 设置滑动窗口的统计时间。熔断器使用这个时间
            /** 设置统计的时间窗口值的，毫秒值。
             circuit break 的打开会根据1个rolling window的统计来计算。
             若rolling window被设为10000毫秒，则rolling window会被分成n个buckets，
             每个bucket包含success，failure，timeout，rejection的次数的统计信息。默认10000
             **/
//            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000")},
            /**
             * 设置在一个滚动窗口中，打开断路器的最少请求数。
             比如：如果值是20，在一个窗口内（比如10秒），收到19个请求，即使这19个请求都失败了，断路器也不会打开。
             */
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            //当出错率超过50%后熔断器启动
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")},
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "15"),
                    /**
                     * BlockingQueue的最大队列数，当设为-1，会使用SynchronousQueue，值为正时使用LinkedBlcokingQueue。
                     */
                    @HystrixProperty(name = "maxQueueSize", value = "15"),
                    /**
                     * 设置存活时间，单位分钟。如果coreSize小于maximumSize，那么该属性控制一个线程从实用完成到被释放的时间.
                     */
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    /**
                     * 设置队列拒绝的阈值,即使maxQueueSize还没有达到
                     */
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000")
            })
    // 购票
    @PostMapping("buyTickets")
    public ResponseVO buyTickets(@RequestParam Integer fieldId, String soldSeats, String seatsName) {
        try {
            if (!tokenBucket.getToken()) {
                return ResponseVO.serviceFail("购票人数过多，请稍后重试");
            } else {
                // 验证售出的票是否为真
                boolean isTrue = orderServiceApi.isTrueSeats(fieldId, soldSeats);

                // 已经销售的座位处理，有没有这些座位
                boolean isNotSold = orderServiceApi.isNotSoldSeats(fieldId, soldSeats);

                // 验证，上述两个内容有一个不为真，则不创建订单
                // 创建订单信息(获取当前登录人)
                if (isTrue && isNotSold) {
                    String userId = CurrentUser.getCurrentUser();
                    if (userId == null || userId.trim().length() == 0) {
                        return ResponseVO.serviceFail("用户未登录");
                    }
                    OrderVO orderVO = orderServiceApi.createOrder(fieldId, soldSeats, seatsName, Integer.parseInt(userId));
                    if (orderVO == null) {
                        log.error("购票失败");
                        return ResponseVO.serviceFail("购票业务失败");
                    } else {
                        return ResponseVO.success(orderVO);
                    }
                } else {
                    return ResponseVO.serviceFail("订单中的座位编号错误");
                }
            }
        } catch (Exception e) {
            log.error("购票业务异常");
            return ResponseVO.serviceFail("购票业务失败");
        }
    }

    public ResponseVO error(@RequestParam Integer fieldId, String soldSeats, String seatsName) {
        return ResponseVO.serviceFail("抱歉，下单人数太多，请稍后重试");
    }



    @GetMapping("getOrderInfo")
    public ResponseVO getOrderInfo(@RequestParam(name = "nowPage", required = false, defaultValue = "1") Integer nowPage,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize) {

        // 获取当前登录人的信息
        String userId = CurrentUser.getCurrentUser();
        if (userId == null || userId.trim().length() == 0) {
            return ResponseVO.serviceFail("用户未登录");
        } else {
            // 根据当前登录人获取已购买的订单信息
            Page<OrderVO> result = orderServiceApi.getOrderByUserId(Integer.parseInt(userId), nowPage, pageSize);
            return ResponseVO.success(nowPage, (int) result.getPages(), "", result.getRecords());
        }
    }

    /**
     * 获取支付信息
     * @param orderId
     * @return
     */
    @PostMapping("getPayInfo")
    public ResponseVO getPayInfo(@RequestParam("orderId") String orderId) {
        // 获取当前登录人的信息
        String userId = CurrentUser.getCurrentUser();
        if (userId == null || userId.trim().length() == 0) {
            return ResponseVO.serviceFail("抱歉, 用户未登录");
        }

        AlipayInfoVO alipayInfoVO = alipayServiceApi.getQRcode(orderId);
        return ResponseVO.success(IMG_PRE, alipayInfoVO);
    }

    /**
     * 获取支付结果
     * @param orderId
     * @param tryNums
     * @return
     */
    @PostMapping("getPayResult")
    public ResponseVO getPayResult(@RequestParam(name = "orderId") String orderId,
                                   @RequestParam(name = "tryNums", required = false, defaultValue = "1") Integer tryNums) {
            // 获取当前登录人的信息
        String userId = CurrentUser.getCurrentUser();
        if (userId == null || userId.trim().length() == 0) {
            return ResponseVO.serviceFail("抱歉, 用户未登录");
        }

        // 将当前登录人的信息传递给后端
        RpcContext.getContext().setAttachment("userId", userId);

        // 判断是否超时
        if (tryNums >= 4) {
            return ResponseVO.serviceFail("订单支付失败, 请稍后重试");
        } else {
            AlipayRequestVO alipayRequestVO = alipayServiceApi.getOrderStatus(orderId);
            if (alipayRequestVO == null || StringUtils.isEmpty(alipayRequestVO.getOrderId())) {
                AlipayRequestVO errorAlipayRequest = new AlipayRequestVO();
                errorAlipayRequest.setOrderId(orderId);
                errorAlipayRequest.setOrderStatus(0);
                errorAlipayRequest.setOrderMsg("支付失败");
                return ResponseVO.success(errorAlipayRequest);
            }
            return ResponseVO.success(alipayRequestVO);
        }
    }
}
