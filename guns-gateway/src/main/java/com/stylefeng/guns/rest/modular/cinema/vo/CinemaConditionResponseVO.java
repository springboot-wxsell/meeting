package com.stylefeng.guns.rest.modular.cinema.vo;

import com.david.meeting.api.cinema.vo.AreaVO;
import com.david.meeting.api.cinema.vo.BrandVO;
import com.david.meeting.api.cinema.vo.HallTypeVO;
import lombok.Data;

import java.util.List;

/**
 * @author : wangwei
 * @date : Created in 2020/2/19 13:08
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@Data
public class CinemaConditionResponseVO {

    private List<BrandVO> brandList;
    private List<AreaVO> areaList;
    private List<HallTypeVO> halltypeList;
}
