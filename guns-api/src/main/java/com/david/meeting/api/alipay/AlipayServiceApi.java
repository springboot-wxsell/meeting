package com.david.meeting.api.alipay;

import com.david.meeting.api.alipay.vo.AlipayInfoVO;
import com.david.meeting.api.alipay.vo.AlipayRequestVO;

/**
 * @author : wangwei
 * @date : Created in 2020/3/5 23:29
 * @description: 支付接口
 * @modified By:
 * @version: ${version}
 */
public interface AlipayServiceApi {

    /**
     * 获取订单支付二维码
     *
     * @param orderId
     * @return
     */
    AlipayInfoVO getQRcode(String orderId);


    /**
     * 获取订单状态
     *
     * @param orderId
     * @return
     */
    AlipayRequestVO getOrderStatus(String orderId);
}
