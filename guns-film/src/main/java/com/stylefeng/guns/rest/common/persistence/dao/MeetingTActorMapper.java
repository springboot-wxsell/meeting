package com.stylefeng.guns.rest.common.persistence.dao;

import com.david.meeting.api.film.vo.ActorVO;
import com.stylefeng.guns.rest.common.persistence.model.MeetingTActor;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 演员表 Mapper 接口
 * </p>
 *
 * @author david
 * @since 2020-02-02
 */
public interface MeetingTActorMapper extends BaseMapper<MeetingTActor> {

    List<ActorVO> getActors(@Param("filmId") String filmId);

}
