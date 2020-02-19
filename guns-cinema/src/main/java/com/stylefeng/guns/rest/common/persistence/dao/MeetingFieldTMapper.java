package com.stylefeng.guns.rest.common.persistence.dao;

import com.david.meeting.api.cinema.vo.FilmInfoVO;
import com.david.meeting.api.cinema.vo.HallInfoVO;
import com.stylefeng.guns.rest.common.persistence.model.MeetingFieldT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 放映场次表 Mapper 接口
 * </p>
 *
 * @author david
 * @since 2020-02-16
 */
public interface MeetingFieldTMapper extends BaseMapper<MeetingFieldT> {

    List<FilmInfoVO> getFilmInfos(@Param("cinemaId") int cinemaId);

    HallInfoVO getHallInfo(@Param("fieldId") int fieldId);

    FilmInfoVO getFilmInfoById(@Param("fieldId") int fieldId);
}
