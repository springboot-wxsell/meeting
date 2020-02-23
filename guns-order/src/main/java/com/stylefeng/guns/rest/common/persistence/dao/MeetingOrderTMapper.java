package com.stylefeng.guns.rest.common.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.common.persistence.model.MeetingOrderT;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author david
 * @since 2020-02-23
 */
public interface MeetingOrderTMapper extends BaseMapper<MeetingOrderT> {

    String getSeatsByFieldId(@Param("fieldId") Integer fieldId);

}
