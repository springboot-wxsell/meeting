package com.stylefeng.guns.rest.common.persistence.dao;

import com.david.meeting.api.film.vo.FilmDetailVO;
import com.stylefeng.guns.rest.common.persistence.model.MeetingTFilm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author david
 * @since 2020-02-02
 */
public interface MeetingTFilmMapper extends BaseMapper<MeetingTFilm> {

    FilmDetailVO getFilmDetailByName(@Param("filmName") String filmName);

    FilmDetailVO getFilmDetailById(@Param("uuid") String uuid);



}
