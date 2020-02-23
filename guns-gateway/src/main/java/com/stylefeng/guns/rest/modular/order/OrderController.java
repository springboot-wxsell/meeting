package com.stylefeng.guns.rest.modular.order;

import com.stylefeng.guns.rest.common.vo.ResponseVO;
import jdk.nashorn.internal.ir.IdentNode;
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

    // 购票
    @PostMapping("buyTickets")
    public ResponseVO buyTickets(Integer fieldId, String soldSeats, String seatsName) {

        // 验证售出的票是否为真

        // 已经销售的座位处理，有没有这些座位

        // 创建订单信息(获取当前登录人)
        return null;
    }


    @PostMapping("getOrderInfo")
    public ResponseVO getOrderInfo(@RequestParam(name = "nowPage", required = false, defaultValue = "1") Integer nowPage,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize) {

        // 获取当前登录人的信息

        // 根据当前登录人获取已购买的订单信息


        return null;
    }
}
