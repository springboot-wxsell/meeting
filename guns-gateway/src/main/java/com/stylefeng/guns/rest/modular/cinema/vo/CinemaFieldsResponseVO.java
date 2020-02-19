package com.stylefeng.guns.rest.modular.cinema.vo;

import com.david.meeting.api.cinema.vo.CinemaInfoVO;
import com.david.meeting.api.cinema.vo.FilmInfoVO;
import lombok.Data;

import java.util.List;

/**
 * @author : wangwei
 * @date : Created in 2020/2/19 13:21
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@Data
public class CinemaFieldsResponseVO {

    private CinemaInfoVO cinemaInfo;

    private List<FilmInfoVO> filmList;
}
