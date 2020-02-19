package com.stylefeng.guns.rest.modular.cinema.vo;

import com.david.meeting.api.cinema.vo.CinemaInfoVO;
import com.david.meeting.api.cinema.vo.FilmInfoVO;
import com.david.meeting.api.cinema.vo.HallInfoVO;
import lombok.Data;

/**
 * @author : wangwei
 * @date : Created in 2020/2/19 13:31
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@Data
public class CinemaFieldResponseVO {

    private FilmInfoVO filmInfo;

    private CinemaInfoVO cinemaInfo;

    private HallInfoVO hallInfo;
}
