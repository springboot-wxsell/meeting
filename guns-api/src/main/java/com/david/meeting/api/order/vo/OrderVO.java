package com.david.meeting.api.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wangwei
 * @date : Created in 2020/2/23 14:29
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@Data
public class OrderVO implements Serializable {

    private String orderId;

    private String filmName;

    private String fieldTime;

    private String cinemaName;

    private String seatsName;

    private String orderPrice;

    private String orderTimestamp;

    private String orderStatus;
}
