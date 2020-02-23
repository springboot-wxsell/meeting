package com.david.meeting.api.order.vo;

import lombok.Data;

/**
 * @author : wangwei
 * @date : Created in 2020/2/23 14:29
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@Data
public class OrderVO {

    private String orderId;

    private String filmName;

    private String fieldTime;

    private String cinemaName;

    private String seatName;

    private String orderPrice;

    private String orderTimestamp;

    private String orderStatus;
}
