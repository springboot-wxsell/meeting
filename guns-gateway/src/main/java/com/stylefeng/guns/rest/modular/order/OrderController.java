package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.david.meeting.api.order.OrderServiceApi;
import com.david.meeting.api.order.vo.OrderVO;
import com.stylefeng.guns.core.util.TokenBucket;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.common.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
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


    @Reference(interfaceClass = OrderServiceApi.class, check = false)
    private OrderServiceApi orderServiceApi;

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
}
