package com.david.meeting.api.alipay;

import com.david.meeting.api.alipay.vo.AlipayInfoVO;
import com.david.meeting.api.alipay.vo.AlipayRequestVO;

/**
 * @author : wangwei
 * @date : Created in 2020/3/8 15:30
 * @description: 业务降级处理
 * @modified By:
 * @version: ${version}
 */
public class AliPayServiceMock implements AlipayServiceApi {


    @Override
    public AlipayInfoVO getQRcode(String orderId) {
        return null;
    }

    @Override
    public AlipayRequestVO getOrderStatus(String orderId) {
        AlipayRequestVO alipayRequestVO = new AlipayRequestVO();
        alipayRequestVO.setOrderId(orderId);
        alipayRequestVO.setOrderStatus(0);
        alipayRequestVO.setOrderMsg("尚未支付成功!");
        return alipayRequestVO;
    }
}
