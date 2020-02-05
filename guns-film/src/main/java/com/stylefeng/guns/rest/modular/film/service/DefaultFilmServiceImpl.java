package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.david.meeting.api.film.FilmServiceApi;
import com.david.meeting.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
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
@Service(interfaceClass = FilmServiceApi.class)
public class DefaultFilmServiceImpl implements FilmServiceApi {

    @Autowired
    private MeetingTBannerMapper meetingTBannerMapper;

    @Autowired
    private MeetingTFilmMapper meetingTFilmMapper;

    @Autowired
    private MeetingTSourceDictMapper meetingTSourceDictMapper;

    @Autowired
    private MeetingTCatDictMapper meetingTCatDictMapper;

    @Autowired
    private MeetingTYearDictMapper meetingTYearDictMapper;

    @Autowired
    private MeetingTFilmInfoMapper meetingTFilmInfoMapper;

    @Autowired
    private MeetingTActorMapper meetingTActorMapper;

    @Override
    public List<BannerVO> getBanners() {
        List<BannerVO> bannerVOList = new ArrayList<>();
        List<MeetingTBanner> meetingTBanners = meetingTBannerMapper.selectList(null);
        for (MeetingTBanner meetingTBanner : meetingTBanners) {
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerId(meetingTBanner.getUuid() + "");
            bannerVO.setBannerUrl(meetingTBanner.getBannerUrl());
            bannerVO.setBannerAddress(meetingTBanner.getBannerAddress());
            bannerVOList.add(bannerVO);
        }
        return bannerVOList;
    }

    @Override
    public FilmVO getHotFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> hotFilms = null;
        // 热映影片的限制条件
        EntityWrapper<MeetingTFilm> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        // 判断是否是首页所需内容
        if (isLimit) {
            // 如果是，则限制条数，限制为热映影片
            Page<MeetingTFilm> page = new Page<>(1, nums);
            List<MeetingTFilm> meetingTFilmList = meetingTFilmMapper.selectPage(page, entityWrapper);
            hotFilms = getFilmInfos(meetingTFilmList);
            filmVO.setFilmNum(meetingTFilmList.size());
            filmVO.setFilmInfo(hotFilms);
        } else {
            // 如果不是，则是列表页，限制内容为热映影片
            Page<MeetingTFilm> page = null;
            // 根据 sortId 不同，来构造不同的对象
            // 查询类型，1-正在热映，2-即将上映，3-经典影片
            switch (sortId) {
                case 1 :
                    page = new Page<>(nowPage, nums, "film_box_office");
                    break;
                case 2 :
                    page = new Page<>(nowPage, nums, "film_time");
                    break;
                case 3 :
                    page = new Page<>(nowPage, nums, "film_score");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_box_office");
                    break;
            }
            // 如果sourceId, yearId, catId 不为99，按照对应的编码进行查询
            if (99 != sourceId) {
                entityWrapper.eq("film_source", sourceId);
            }
            if (99 != yearId) {
                entityWrapper.eq("film_status", yearId);
            }
            if (99 != catId) {
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats", catStr);
            }
            List<MeetingTFilm> meetingTFilmList = meetingTFilmMapper.selectPage(page, entityWrapper);
            hotFilms = getFilmInfos(meetingTFilmList);
            filmVO.setFilmNum(meetingTFilmList.size());

            // 计算分页
            int totalCounts = meetingTFilmMapper.selectCount(entityWrapper);
            int totalPages = (totalCounts/nums) + 1;
            filmVO.setNowPage(nowPage);
            filmVO.setTotalPage(totalPages);
            filmVO.setFilmInfo(hotFilms);
        }
        return filmVO;
    }

    private List<FilmInfo> getFilmInfos(List<MeetingTFilm> meetingTFilmList) {
        List<FilmInfo> filmInfoList = new ArrayList<>();
        for (MeetingTFilm meetingTFilm : meetingTFilmList) {
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setScore(meetingTFilm.getFilmScore());
            filmInfo.setImgAddress(meetingTFilm.getImgAddress());
            filmInfo.setFilmType(meetingTFilm.getFilmType());
            filmInfo.setFilmScore(meetingTFilm.getFilmScore());
            filmInfo.setFilmName(meetingTFilm.getFilmName());
            filmInfo.setFilmId(meetingTFilm.getUuid() + "");
            filmInfo.setExpectNum(meetingTFilm.getFilmPresalenum());
            filmInfo.setBoxNum(meetingTFilm.getFilmBoxOffice());
            filmInfo.setShowTime(DateUtil.getDays(meetingTFilm.getFilmTime()));

            filmInfoList.add(filmInfo);
        }
        return filmInfoList;
    }

    @Override
    public FilmVO getSoonFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> soonFilms = null;
        // 热映影片的限制条件
        EntityWrapper<MeetingTFilm> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "2");
        // 判断是否是首页所需内容
        if (isLimit) {
            // 如果是，则限制条数，限制为即将上映影片
            Page<MeetingTFilm> page = null;
            // 根据 sortId 不同，来构造不同的对象
            // 查询类型，1-正在热映，2-即将上映，3-经典影片
            switch (sortId) {
                case 1 :
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                case 2 :
                    page = new Page<>(nowPage, nums, "film_time");
                    break;
                case 3 :
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
            }
            List<MeetingTFilm> meetingTFilmList = meetingTFilmMapper.selectPage(page, entityWrapper);
            soonFilms = getFilmInfos(meetingTFilmList);
            filmVO.setFilmNum(meetingTFilmList.size());
            filmVO.setFilmInfo(soonFilms);
        } else {
            // 如果不是，则是列表页，限制内容为即将上映影片
            Page<MeetingTFilm> page = new Page<>(nowPage, nums);
            // 如果sourceId, yearId, catId 不为99，按照对应的编码进行查询
            if (99 != sourceId) {
                entityWrapper.eq("film_source", sourceId);
            }
            if (99 != yearId) {
                entityWrapper.eq("film_status", yearId);
            }
            if (99 != catId) {
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats", catStr);
            }
            List<MeetingTFilm> meetingTFilmList = meetingTFilmMapper.selectPage(page, entityWrapper);
            soonFilms = getFilmInfos(meetingTFilmList);
            filmVO.setFilmNum(meetingTFilmList.size());

            // 计算分页
            int totalCounts = meetingTFilmMapper.selectCount(entityWrapper);
            int totalPages = (totalCounts/nums) + 1;
            filmVO.setNowPage(nowPage);
            filmVO.setTotalPage(totalPages);
            filmVO.setFilmInfo(soonFilms);
        }
        return filmVO;
    }

    @Override
    public FilmVO getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> classicFilms = null;
        // 经典影片的限制条件
        EntityWrapper<MeetingTFilm> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "3");
        Page<MeetingTFilm> page = null;
        // 根据 sortId 不同，来构造不同的对象
        // 查询类型，1-正在热映，2-即将上映，3-经典影片
        switch (sortId) {
            case 1 :
                page = new Page<>(nowPage, nums, "film_box_office");
                break;
            case 2 :
                page = new Page<>(nowPage, nums, "film_time");
                break;
            case 3 :
                page = new Page<>(nowPage, nums, "film_score");
                break;
            default:
                page = new Page<>(nowPage, nums, "film_box_office");
                break;
        }
        // 如果sourceId, yearId, catId 不为99，按照对应的编码进行查询
        if (99 != sourceId) {
            entityWrapper.eq("film_source", sourceId);
        }
        if (99 != yearId) {
            entityWrapper.eq("film_status", yearId);
        }
        if (99 != catId) {
            String catStr = "%#" + catId + "#%";
            entityWrapper.like("film_cats", catStr);
        }
        List<MeetingTFilm> meetingTFilmList = meetingTFilmMapper.selectPage(page, entityWrapper);
        classicFilms = getFilmInfos(meetingTFilmList);
        filmVO.setFilmNum(meetingTFilmList.size());

        // 计算分页
        int totalCounts = meetingTFilmMapper.selectCount(entityWrapper);
        int totalPages = (totalCounts/nums) + 1;
        filmVO.setNowPage(nowPage);
        filmVO.setTotalPage(totalPages);
        filmVO.setFilmInfo(classicFilms);
        return filmVO;
    }

    @Override
    public List<FilmInfo> getBoxRanking() {
        // 条件 --> 在热映的，票房前10名
        EntityWrapper<MeetingTFilm> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");

        Page<MeetingTFilm> page = new Page<>(1, 10, "film_box_office");
        List<MeetingTFilm> filmList = meetingTFilmMapper.selectPage(page, entityWrapper);

        List<FilmInfo> filmInfoList = getFilmInfos(filmList);

        return filmInfoList;
    }

    @Override
    public List<FilmInfo> getExpectRanking() {
        // 条件 --> 正在热映的，预售前10名
        EntityWrapper<MeetingTFilm> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "2");

        Page<MeetingTFilm> page = new Page<>(1, 10, "film_preSaleNum");
        List<MeetingTFilm> filmList = meetingTFilmMapper.selectPage(page, entityWrapper);

        List<FilmInfo> filmInfoList = getFilmInfos(filmList);
        return filmInfoList;
    }

    @Override
    public List<FilmInfo> getTop() {
        // 条件 --> 在热映的，评分前10名
        EntityWrapper<MeetingTFilm> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        Page<MeetingTFilm> page = new Page<>(1, 10, "film_score");
        List<MeetingTFilm> filmList = meetingTFilmMapper.selectPage(page, entityWrapper);

        List<FilmInfo> filmInfoList = getFilmInfos(filmList);
        return filmInfoList;
    }

    @Override
    public List<CatVO> getCats() {
        // 查询实体对象 --
        // 将实体对象转换为业务对象
        List<CatVO> catVOList = new ArrayList<>();
        List<MeetingTCatDict> meetingTCatDicts = meetingTCatDictMapper.selectList(null);
        for (MeetingTCatDict meetingTCatDict : meetingTCatDicts) {
            CatVO catVO = new CatVO();
            catVO.setCatId(meetingTCatDict.getUuid() + "");
            catVO.setCatName(meetingTCatDict.getShowName());

            catVOList.add(catVO);
        }
        return catVOList;
    }

    @Override
    public List<SourceVO> getSources() {
        List<SourceVO> sourceVOList = new ArrayList<>();
        List<MeetingTSourceDict> meetingTSourceDicts = meetingTSourceDictMapper.selectList(null);
        for (MeetingTSourceDict meetingTSourceDict : meetingTSourceDicts) {
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceId(meetingTSourceDict.getUuid() + "");
            sourceVO.setSourceName(meetingTSourceDict.getShowName());

            sourceVOList.add(sourceVO);
        }
        return sourceVOList;
    }

    @Override
    public List<YearVO> getYears() {
        // 查询实体对象 --
        // 将实体对象转换为业务对象
        List<YearVO> yearVOList = new ArrayList<>();
        List<MeetingTYearDict> meetingTCatDicts = meetingTYearDictMapper.selectList(null);
        for (MeetingTYearDict meetingTYearDict : meetingTCatDicts) {
            YearVO yearVO = new YearVO();
            yearVO.setYearId(meetingTYearDict.getUuid() + "");
            yearVO.setYearName(meetingTYearDict.getShowName());

            yearVOList.add(yearVO);
        }
        return yearVOList;
    }

    @Override
    public FilmDetailVO getFilmDetail(int searchType, String searchParam) {
        FilmDetailVO filmDetailVO = null;
        if (searchType == 1) {
            filmDetailVO = meetingTFilmMapper.getFilmDetailByName(searchParam);
        } else {
            filmDetailVO = meetingTFilmMapper.getFilmDetailById(searchParam);
        }
        return filmDetailVO;
    }

    @Override
    public FilmDescVO getFilmDesc(String filmId) {
        MeetingTFilmInfo meetingTFilmInfo = getFilmInfo(filmId);
        FilmDescVO filmDescVO = new FilmDescVO();
        filmDescVO.setBiography(meetingTFilmInfo.getBiography());
        filmDescVO.setFilmId(filmId);
        return filmDescVO;
    }

    private MeetingTFilmInfo getFilmInfo(String filmId) {
        MeetingTFilmInfo meetingTFilmInfo = new MeetingTFilmInfo();
        meetingTFilmInfo.setFilmId(filmId);
        meetingTFilmInfo = meetingTFilmInfoMapper.selectOne(meetingTFilmInfo);
        return meetingTFilmInfo;
    }

    @Override
    public ImgVO getImgs(String filmId) {
        MeetingTFilmInfo meetingTFilmInfo = getFilmInfo(filmId);
        // 图片呢地址是5个以逗号分隔的url
        String filmImgStr = meetingTFilmInfo.getFilmImgs();
        String[] filmImgs = filmImgStr.split(",");
        ImgVO imgVO = new ImgVO();
        imgVO.setMainImg(filmImgs[0]);
        imgVO.setImg01(filmImgs[1]);
        imgVO.setImg02(filmImgs[2]);
        imgVO.setImg03(filmImgs[3]);
        imgVO.setImg04(filmImgs[4]);
        return imgVO;
    }

    @Override
    public ActorVO getDectInfo(String filmId) {
        MeetingTFilmInfo meetingTFilmInfo = getFilmInfo(filmId);
        // 获取导演编号
        Integer directorId = meetingTFilmInfo.getDirectorId();
        MeetingTActor meetingTActor = meetingTActorMapper.selectById(directorId);
        ActorVO actorVO = new ActorVO();
        actorVO.setImgAddress(meetingTActor.getActorImg());
        actorVO.setDirectorName(meetingTActor.getActorName());

        return actorVO;
    }

    @Override
    public List<ActorVO> getActors(String filmId) {
        List<ActorVO> actorVOList = meetingTActorMapper.getActors(filmId);
        return actorVOList;
    }
}
