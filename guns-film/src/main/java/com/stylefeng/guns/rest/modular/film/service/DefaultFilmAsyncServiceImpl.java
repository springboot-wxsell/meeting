package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.david.meeting.api.film.FilmAsyncServiceApi;
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
@Service(interfaceClass = FilmAsyncServiceApi.class)
public class DefaultFilmAsyncServiceImpl implements FilmAsyncServiceApi {

    @Autowired
    private MeetingTFilmInfoMapper meetingTFilmInfoMapper;

    @Autowired
    private MeetingTActorMapper meetingTActorMapper;


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
