package com.stylefeng.guns.rest.modular.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.david.meeting.api.order.OrderServiceApi;
import com.david.meeting.api.order.vo.OrderVO;
import com.stylefeng.guns.rest.common.persistence.dao.MeetingOrderTMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : wangwei
 * @date : Created in 2020/2/2 1:18
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@Slf4j
@Component
@Service(interfaceClass = OrderServiceApi.class, executes = 10)
public class DefaultOrderServiceImpl implements OrderServiceApi {

    @Autowired
    private MeetingOrderTMapper meetingOrderTMapper;

    @Override
    public boolean isTrueSeats(Integer fieldId, String seats) {

        // 根据 fieldId 找到对应的座位位置图

        // 读取位置图

        //
        return false;
    }

    @Override
    public boolean isNotSoldSeats(String fieldId, String seats) {
        return false;
    }

    @Override
    public OrderVO createOrder(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        return null;
    }

    @Override
    public List<OrderVO> getOrderByUserId(Integer userId) {
        return null;
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        return null;
    }
}


