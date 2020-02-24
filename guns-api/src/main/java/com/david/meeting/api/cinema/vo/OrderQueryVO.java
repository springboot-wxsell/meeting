package com.david.meeting.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wangwei
 * @date : Created in 2020/2/23 20:46
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@Data
public class OrderQueryVO implements Serializable {

    private String cinemaId;

    private String filmPrice;
}
