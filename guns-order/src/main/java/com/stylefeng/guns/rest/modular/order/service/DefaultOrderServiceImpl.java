package com.stylefeng.guns.rest.modular.order.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.david.meeting.api.cinema.CinemaServiceApi;
import com.david.meeting.api.cinema.vo.FilmInfoVO;
import com.david.meeting.api.cinema.vo.OrderQueryVO;
import com.david.meeting.api.order.OrderServiceApi;
import com.david.meeting.api.order.vo.OrderVO;
import com.stylefeng.guns.rest.common.persistence.dao.MeetingOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MeetingOrderT;
import com.stylefeng.guns.rest.common.utils.FTPUtils;
import com.stylefeng.guns.rest.common.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
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

    @Reference(interfaceClass = CinemaServiceApi.class, check = false)
    private CinemaServiceApi cinemaServiceApi;

    @Autowired
    private FTPUtils ftpUtils;

    @Override
    public boolean isTrueSeats(Integer fieldId, String seats) {

        // 根据 fieldId 找到对应的座位位置图
        String seatAddress = meetingOrderTMapper.getSeatsByFieldId(fieldId);

        // 读取位置图
        String addressStr = ftpUtils.getFileStrByAddress(seatAddress);

        //将 address 转化为 Json 对象
        JSONObject jsonObject = JSONObject.parseObject(addressStr);
        String ids = jsonObject.get("ids").toString();
//        String ids = "11,12";
        String[] idArrs = ids.split(",");
        String[] seatArrs = seats.split(",");

        // 每一次匹配上， isTrue 都加1
        int isTrue = 0;
        for (String id : idArrs) {
            for (String seat : seatArrs) {
                if (seat.equalsIgnoreCase(id)) {
                    isTrue++;
                }
            }
        }
        // 如果匹配上的数量与已售座位数据一致，则为全部匹配上
        if (seatArrs.length == isTrue) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 优化空间，sql 直接横转纵用，拼接后，通过二分法查询匹配
     *
     * @param fieldId
     * @param seats
     * @return
     */
    @Override
    public boolean isNotSoldSeats(Integer fieldId, String seats) {
        EntityWrapper<MeetingOrderT> entityWrapper = new EntityWrapper();
        entityWrapper.eq("field_id", fieldId);
        List<MeetingOrderT> orderTList = meetingOrderTMapper.selectList(entityWrapper);
        String[] seatArrs = seats.split(",");
        // 有任何一个匹配上，则直接返回失败
        for (MeetingOrderT orderT : orderTList) {
            String[] ids = orderT.getSeatsIds().split(",");
            for (String id : ids) {
                for (String seat : seatArrs) {
                    if (id.equalsIgnoreCase(seat)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // 创建新订单
    @Override
    @Transactional
    public OrderVO createOrder(Integer fieldId, String soldSeats, String seatsName, Integer userId) {

        // 编号
        String uuid = UuidUtils.genUuid();

        // 获取影片信息
        FilmInfoVO filmInfo = cinemaServiceApi.getFilmInfoByFieldId(fieldId);
        Integer filmId = Integer.parseInt(filmInfo.getFilmId());

        // 获取影院信息
        OrderQueryVO orderQueryVO = cinemaServiceApi.getOrderNeeds(fieldId);
        Integer cinemaId = Integer.parseInt(orderQueryVO.getCinemaId());
        BigDecimal filmPrice = new BigDecimal(orderQueryVO.getFilmPrice()).setScale(2);

        // 计算订单总金额
        int solds = soldSeats.split(",").length;
        BigDecimal totalPrice = filmPrice.multiply(new BigDecimal(solds)).setScale(2, RoundingMode.HALF_UP);
        MeetingOrderT meetingOrderT = new MeetingOrderT();
        meetingOrderT.setUuid(uuid);
        meetingOrderT.setSeatsName(seatsName);
        meetingOrderT.setSeatsIds(soldSeats);
        meetingOrderT.setOrderUser(userId);
        meetingOrderT.setOrderPrice(totalPrice);
        meetingOrderT.setFilmPrice(filmPrice);
        meetingOrderT.setFilmId(filmId);
        meetingOrderT.setFieldId(fieldId);
        meetingOrderT.setCinemaId(cinemaId);
        Integer num = meetingOrderTMapper.insert(meetingOrderT);
        if (num > 0) {
            OrderVO orderVO = meetingOrderTMapper.getOrderInfoById(uuid);
            if (orderVO == null || orderVO.getOrderId() == null) {
                log.error("订单信息查询失败, 订单编号为: {}", uuid);
                return null;
            } else {
                return orderVO;
            }
        } else {
            // 订单插入失败
            log.error("订单插入失败");
            return null;
        }
    }

    @Override
    public Page<OrderVO> getOrderByUserId(Integer userId, int nowPage, int pageSize) {
        Page<OrderVO> page = new Page<>();
        if (userId == null) {
            log.error("订单查询业务失败, 用户编号未获取到");
            return null;
        } else {
            List<OrderVO> orderVOList = meetingOrderTMapper.getOrderInfosByUserId(userId);
            if (CollectionUtils.isEmpty(orderVOList)) {
                page.setTotal(0);
                page.setRecords(Collections.emptyList());
                return page;
            } else {
                EntityWrapper<MeetingOrderT> entityWrapper = new EntityWrapper();
                entityWrapper.eq("order_user", userId);
                Integer count = meetingOrderTMapper.selectCount(entityWrapper);
                page.setTotal(count);
                page.setRecords(orderVOList);
                return page;
            }
        }
    }

    // 根据放映查询， 获取所有的已售座位

    /**
     * 1、 1,2,3,4
     * 2、 5,7,8
     * --> 1,2,3,4,5,7,8
     *
     * @param fieldId
     * @return
     */
    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        if (fieldId == null) {
            log.error("未传入任何场次编号");
            return null;
        } else {
            String seatIds = meetingOrderTMapper.getSoldSeatsByFieldId(fieldId);
            return seatIds;
        }
    }

    @Override
    public OrderVO getOrderInfoById(String orderId) {
        return meetingOrderTMapper.getOrderInfoById(orderId);
    }

    @Override
    public boolean paySuccess(String orderId) {
        MeetingOrderT meetingOrderT = new MeetingOrderT();
        meetingOrderT.setUuid(orderId);
        meetingOrderT.setOrderStatus(1);
        Integer num = meetingOrderTMapper.updateById(meetingOrderT);
        if (num >= 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean payFail(String orderId) {
        MeetingOrderT meetingOrderT = new MeetingOrderT();
        meetingOrderT.setUuid(orderId);
        meetingOrderT.setOrderStatus(2);
        Integer num = meetingOrderTMapper.updateById(meetingOrderT);
        if (num >= 1) {
            return true;
        } else {
            return false;
        }
    }
}


