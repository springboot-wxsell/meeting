package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.david.meeting.api.cinema.CinemaServiceApi;
import com.david.meeting.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.vo.ResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaConditionResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldsResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : wangwei
 * @date : Created in 2020/2/19 1:21
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@Slf4j
@RequestMapping("/cinema/")
@RestController
public class CinemaController {

    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    @Reference(interfaceClass = CinemaServiceApi.class, cache = "lru", check = false)
    private CinemaServiceApi cinemaServiceApi;

    //查询影院列表
    @GetMapping(value = "getCinemas")
    public ResponseVO getCinemas(CinemaQueryVO cinemaQueryVO) {
        try {
            //
            Page<CinemaVO> cinemaVOPage = cinemaServiceApi.getCinemas(cinemaQueryVO);
            if (CollectionUtils.isEmpty(cinemaVOPage.getRecords())) {
                return ResponseVO.success("没有影院可查");
            } else {
                return ResponseVO.success(cinemaVOPage.getCurrent(), (int) cinemaVOPage.getPages(), "", cinemaVOPage.getRecords());
            }
        } catch (Exception e) {
            log.error("获取影院列表异常", e);
            return ResponseVO.serviceFail("查询影院列表失败");
        }
    }


    //条件列表
    @GetMapping(value = "getCondition")
    public ResponseVO getCondition(CinemaQueryVO cinemaQueryVO) {
        try {
            List<BrandVO> brandVOList = cinemaServiceApi.getBrands(cinemaQueryVO.getBrandId());

            List<AreaVO> areaVOList = cinemaServiceApi.getAreas(cinemaQueryVO.getDistrictId());

            List<HallTypeVO> hallTypeVOList = cinemaServiceApi.getHallTypes(cinemaQueryVO.getHallType());

            CinemaConditionResponseVO cinemaConditionResponseVO = new CinemaConditionResponseVO();
            cinemaConditionResponseVO.setBrandList(brandVOList);
            cinemaConditionResponseVO.setAreaList(areaVOList);
            cinemaConditionResponseVO.setHalltypeList(hallTypeVOList);

            return ResponseVO.success(cinemaConditionResponseVO);
        } catch (Exception e) {
            log.error("影院信息条件查询异常", e);
            return ResponseVO.serviceFail("影院信息查询失败");
        }
    }
    //获取播放场次接口
    @GetMapping(value = "getFields")
    public ResponseVO getFields(Integer cinemaId) {
        try {
            CinemaInfoVO cinemaInfoVO = cinemaServiceApi.getCinemaInfoById(cinemaId);

            List<FilmInfoVO> filmInfoVOList = cinemaServiceApi.getFilmInfoByCinemaId(cinemaId);

            CinemaFieldsResponseVO cinemaFieldResponseVO = new CinemaFieldsResponseVO();
            cinemaFieldResponseVO.setCinemaInfo(cinemaInfoVO);
            cinemaFieldResponseVO.setFilmList(filmInfoVOList);
            return ResponseVO.success(IMG_PRE, cinemaFieldResponseVO);
        } catch (Exception e) {
            log.error("获取播放场次异常", e);
            return ResponseVO.serviceFail("获取播放场次失败");
        }
    }

    //获取场次详细信息接口
    @GetMapping(value = "getFieldInfo")
    public ResponseVO getFieldInfo(Integer cinemaId, Integer fieldId) {
        try {
            CinemaInfoVO cinemaInfoVO = cinemaServiceApi.getCinemaInfoById(cinemaId);

            FilmInfoVO filmInfoVO = cinemaServiceApi.getFilmInfoByFieldId(fieldId);

            HallInfoVO hallInfoVO = cinemaServiceApi.getFilmFieldInfo(fieldId);

            // TODO 造几个假数据，后续会对接订单接口
            hallInfoVO.setSoldSeats("1,2,3");
            CinemaFieldResponseVO cinemaFieldResponseVO = new CinemaFieldResponseVO();
            cinemaFieldResponseVO.setCinemaInfo(cinemaInfoVO);
            cinemaFieldResponseVO.setFilmInfo(filmInfoVO);
            cinemaFieldResponseVO.setHallInfo(hallInfoVO);
            return ResponseVO.success(IMG_PRE, cinemaFieldResponseVO);
        } catch (Exception e) {
            log.error("获取场次详细信息异常", e);
            return ResponseVO.serviceFail("获取场次详细信息失败");
        }
    }
}
