package com.david.meeting.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wangwei
 * @date : Created in 2020/3/5 23:30
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@Data
public class AlipayInfoVO implements Serializable {

    private static final long serialVersionUID = -196685543481167977L;

    /**
     * 订单 id
     */
    private String orderId;

    /**
     * 二维码地址
     */
    private String QRcodeAddress;
}
