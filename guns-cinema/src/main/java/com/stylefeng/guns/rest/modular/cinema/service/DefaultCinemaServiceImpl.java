package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.david.meeting.api.cinema.CinemaServiceApi;
import com.david.meeting.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.MeetingAreaDictT;
import com.stylefeng.guns.rest.common.persistence.model.MeetingBrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.MeetingCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MeetingHallDictT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
@Service(interfaceClass = CinemaServiceApi.class, executes = 10)
public class DefaultCinemaServiceImpl implements CinemaServiceApi {

    @Autowired
    private MeetingFieldTMapper meetingFieldTMapper;

    @Autowired
    private MeetingCinemaTMapper meetingCinemaTMapper;

    @Autowired
    private MeetingBrandDictTMapper meetingBrandDictTMapper;

    @Autowired
    private MeetingAreaDictTMapper meetingAreaDictTMapper;

    @Autowired
    private MeetingHallDictTMapper meetingHallDictTMapper;


    // 分页获取影院列表
    @Override
    public Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO) {
        List<CinemaVO> cinemaVOList = new ArrayList<>();
        Page<MeetingCinemaT> page = new Page<>(cinemaQueryVO.getNowPage(), cinemaQueryVO.getPageSize());
        // 判断是否传入查询条件， brandId, distId, hallType 是否= 99
        EntityWrapper<MeetingCinemaT> entityWrapper = new EntityWrapper<>();
        if (cinemaQueryVO.getBrandId() != 99) {
            entityWrapper.eq("brand_id", cinemaQueryVO.getBrandId());
        }
        if (cinemaQueryVO.getDistrictId() != 99) {
            entityWrapper.eq("area_id", cinemaQueryVO.getDistrictId());
        }
        if (cinemaQueryVO.getHallType() != 99) {
            entityWrapper.like("hall_ids", "%#" + cinemaQueryVO.getHallType() + "#%");
        }
        // 将数据实体转为业务实体
        List<MeetingCinemaT> meetingCinemaTList = meetingCinemaTMapper.selectPage(page, entityWrapper);
        for (MeetingCinemaT meetingCinemaT : meetingCinemaTList) {
            CinemaVO cinemaVO = new CinemaVO();
            cinemaVO.setUuid(meetingCinemaT.getUuid() + "");
            cinemaVO.setAddress(meetingCinemaT.getCinemaAddress());
            cinemaVO.setCinemaName(meetingCinemaT.getCinemaName());
            cinemaVO.setMinimumPrice(meetingCinemaT.getMinimumPrice() + "");

            cinemaVOList.add(cinemaVO);
        }
        // 分页
        long counts = meetingCinemaTMapper.selectCount(entityWrapper);
        Page<CinemaVO> result = new Page<>();
        result.setRecords(cinemaVOList);
        result.setSize(cinemaQueryVO.getPageSize());
        result.setTotal(counts);
        return result;
    }

    //
    @Override
    public List<BrandVO> getBrands(int brandId) {
        boolean flag = false;
        List<BrandVO> brandVOList = new ArrayList<>();
        // 判断 brandId 是否存在
        MeetingBrandDictT meetingBrandDictT = meetingBrandDictTMapper.selectById(brandId);
        // 判断 brandId 是否等于 99
        if (brandId == 99 || meetingBrandDictT == null || meetingBrandDictT.getUuid() == null) {
            flag = true;
        }
        // 查询所有列表
        List<MeetingBrandDictT> meetingBrandDictTS = meetingBrandDictTMapper.selectList(null);
        for (MeetingBrandDictT brandDictT : meetingBrandDictTS) {
            BrandVO bannerVO = new BrandVO();
            bannerVO.setBrandId(brandDictT.getUuid() + "");
            bannerVO.setBrandName(brandDictT.getShowName());
            // 判断 flag 如果为 true, 则需要将99 置为true, 否则，将内容置为isActive
            if (flag) {
                if (brandDictT.getUuid() == 99) {
                    bannerVO.setActive(true);
                }
            } else {
                if (brandDictT.getUuid() == brandId) {
                    bannerVO.setActive(true);
                }
            }
            brandVOList.add(bannerVO);
        }
        return brandVOList;
    }

    // 获取行政区域列表
    @Override
    public List<AreaVO> getAreas(int areaId) {
        boolean flag = false;
        List<AreaVO> areaVOList = new ArrayList<>();
        // 判断 brandId 是否存在
        MeetingAreaDictT meetingAreaDictT = meetingAreaDictTMapper.selectById(areaId);
        // 判断 brandId 是否等于 99
        if (areaId == 99 || meetingAreaDictT == null || meetingAreaDictT.getUuid() == null) {
            flag = true;
        }
        // 查询所有列表
        List<MeetingAreaDictT> meetingAreaDictTList = meetingAreaDictTMapper.selectList(null);
        // 判断 flag 如果为 true, 则需要将99 置为true, 否则，将内容置为isActive
        for (MeetingAreaDictT areaDictT : meetingAreaDictTList) {
            AreaVO areaVO = new AreaVO();
            areaVO.setAreaId(areaDictT.getUuid() + "");
            areaVO.setAreaName(areaDictT.getShowName());
            if (flag) {
                if (areaDictT.getUuid() == 99) {
                    areaVO.setActive(true);
                }
            } else {
                if (areaDictT.getUuid() == areaId) {
                    areaVO.setActive(true);
                }
            }
            areaVOList.add(areaVO);
        }
        return areaVOList;
    }

    // 获取影厅类型列表
    @Override
    public List<HallTypeVO> getHallTypes(int hallType) {
        boolean flag = false;
        List<HallTypeVO> hallTypeVOList = new ArrayList<>();
        // 判断 hallType 是否存在
        MeetingHallDictT meetingHallDictT = meetingHallDictTMapper.selectById(hallType);
        // 判断 hallType 是否等于 99
        if (hallType == 99 || meetingHallDictT == null || meetingHallDictT.getUuid() == null) {
            flag = true;
        }
        // 查询所有列表
        List<MeetingHallDictT> meetingHallDictTList = meetingHallDictTMapper.selectList(null);
        // 判断 flag 如果为 true, 则需要将99 置为true, 否则，将内容置为isActive
        for (MeetingHallDictT hallDictT : meetingHallDictTList) {
            HallTypeVO hallTypeVO = new HallTypeVO();
            hallTypeVO.setHalltypeId(hallDictT.getUuid() + "");
            hallTypeVO.setHalltypeName(hallDictT.getShowName());
            if (flag) {
                if (hallDictT.getUuid() == 99) {
                    hallTypeVO.setActive(true);
                }
            } else {
                if (hallDictT.getUuid() == hallType) {
                    hallTypeVO.setActive(true);
                }
            }
            hallTypeVOList.add(hallTypeVO);
        }
        return hallTypeVOList;
    }

    //5、根据影院编号，获取影院信息
    @Override
    public CinemaInfoVO getCinemaInfoById(int cinemaId) {
        MeetingCinemaT meetingCinemaT = meetingCinemaTMapper.selectById(cinemaId);

        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        cinemaInfoVO.setImgUrl(meetingCinemaT.getImgAddress());
        cinemaInfoVO.setCinemaPhone(meetingCinemaT.getCinemaPhone());
        cinemaInfoVO.setCinemaName(meetingCinemaT.getCinemaName());
        cinemaInfoVO.setCinemaAdress(meetingCinemaT.getCinemaAddress());
        cinemaInfoVO.setCinemaId(meetingCinemaT.getUuid() + "");

        return cinemaInfoVO;
    }

    //6、获取所有电影的信息和对应的放映场次信息，根据影院编号
    @Override
    public List<FilmInfoVO> getFilmInfoByCinemaId(int cinemaId) {
        return meetingFieldTMapper.getFilmInfos(cinemaId);
    }

    //7、根据放映场次ID获取放映信息
    @Override
    public HallInfoVO getFilmFieldInfo(int fieldId) {
        return meetingFieldTMapper.getHallInfo(fieldId);
    }

    //8、根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
    @Override
    public FilmInfoVO getFilmInfoByFieldId(int fieldId) {
        return meetingFieldTMapper.getFilmInfoById(fieldId);
    }
}


