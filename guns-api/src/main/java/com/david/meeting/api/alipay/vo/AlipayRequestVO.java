package com.david.meeting.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wangwei
 * @date : Created in 2020/3/5 23:33
 * @description: 支付结果返回实体
 * @modified By:
 * @version: ${version}
 */
@Data
public class AlipayRequestVO implements Serializable {

    private static final long serialVersionUID = -5412734493006605640L;

    private String orderId;

    private Integer orderStatus;

    private String orderMsg;
}
