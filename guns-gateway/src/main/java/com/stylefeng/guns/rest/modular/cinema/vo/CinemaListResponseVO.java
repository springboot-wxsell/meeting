package com.stylefeng.guns.rest.modular.cinema.vo;

import com.david.meeting.api.cinema.vo.CinemaVO;
import lombok.Data;

import java.util.List;

/**
 * @author : wangwei
 * @date : Created in 2020/2/19 12:58
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@Data
public class CinemaListResponseVO {

    List<CinemaVO> cinemaVOS;
}
