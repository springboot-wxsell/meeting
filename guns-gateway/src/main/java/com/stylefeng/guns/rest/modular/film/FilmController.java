package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.david.meeting.api.film.FilmAsyncServiceApi;
import com.david.meeting.api.film.FilmServiceApi;
import com.david.meeting.api.film.vo.*;
import com.stylefeng.guns.rest.common.vo.ResponseVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author : wangwei
 * @date : Created in 2020/2/1 21:32
 * @description: 影片
 * @modified By:
 * @version: v1.0.0
 */
@RequestMapping("/film/")
@RestController()
@Slf4j
public class FilmController {

    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    @Reference(interfaceClass = FilmServiceApi.class)
    private FilmServiceApi filmServiceApi;

    // Dubbo 异步调用的方法
    @Reference(interfaceClass = FilmAsyncServiceApi.class, async = true)
    private FilmAsyncServiceApi filmAsyncServiceApi;

    /**
     * API网关:
     * 1.功能聚合(API聚合)
     * 优点：
     * 1. 留个接口，一次请求，同一时刻节省了五次http请求
     * 2. 同一个接口对外 暴露，降低了前后端分离的难度和复杂度
     * 缺点：
     * 1. 一次获取数据过多，容易出现问题
     *
     * @return
     */
    // 获取首页信息
    @GetMapping(value = "getIndex")
    public ResponseVO getIndex() {
        FilmIndexVO filmIndexVO = new FilmIndexVO();
        // 获取banner信息，
        filmIndexVO.setBanners(filmServiceApi.getBanners());
        //获取正在热映的电影
        filmIndexVO.setHotFilms(filmServiceApi.getHotFilms(true, 8, 1, 1, 99, 99, 99));

        // 获取即将上映的电影
        filmIndexVO.setSoonFilms(filmServiceApi.getSoonFilms(true, 8, 1, 1, 99, 99, 99));

        // 票房排行榜
        filmIndexVO.setBoxRanking(filmServiceApi.getBoxRanking());

        // 受欢迎的榜单
        filmIndexVO.setExpectRanking(filmServiceApi.getExpectRanking());

        // 获取前100名
        filmIndexVO.setTop100(filmServiceApi.getTop());

        return ResponseVO.success(IMG_PRE, filmIndexVO);
    }

    // 获取首页信息
    @GetMapping(value = "getConditionList")
    public ResponseVO getConditionList(@RequestParam(value = "catId", required = false, defaultValue = "99") String catId,
                                       @RequestParam(value = "sourceId", required = false, defaultValue = "99") String sourceId,
                                       @RequestParam(value = "yearId", required = false, defaultValue = "99") String yearId) {

        FilmConditionVO filmConditionVO = new FilmConditionVO();

        // 标识位
        boolean flag = false;
        // 类型集合
        List<CatVO> cats = filmServiceApi.getCats();
        List<CatVO> catResult = new ArrayList<>();
        CatVO cat = null;
        for (CatVO catVO : cats) {
            // 判断集合是否存在catId，如果存在，则将对应的实体变成active状态
            // 6
            // 1,2,3,99,4,5 ->
            /*
                优化：【理论上】
                    1、数据层查询按Id进行排序【有序集合 -> 有序数组】
                    2、通过二分法查找
             */
            if (catVO.getCatId().equals("99")) {
                cat = catVO;
                continue;
            }
            if (catVO.getCatId().equals(catId)) {
                flag = true;
                catVO.setActive(true);
            } else {
                catVO.setActive(false);
            }
            catResult.add(catVO);
        }
        // 如果不存在，则默认将全部变为Active状态
        if (!flag) {
            cat.setActive(true);
            catResult.add(cat);
        } else {
            cat.setActive(false);
            catResult.add(cat);
        }


        // 片源集合
        flag = false;
        List<SourceVO> sources = filmServiceApi.getSources();
        List<SourceVO> sourceResult = new ArrayList<>();
        SourceVO sourceVO = null;
        for (SourceVO source : sources) {
            if (source.getSourceId().equals("99")) {
                sourceVO = source;
                continue;
            }
            if (source.getSourceId().equals(catId)) {
                flag = true;
                source.setActive(true);
            } else {
                source.setActive(false);
            }
            sourceResult.add(source);
        }
        // 如果不存在，则默认将全部变为Active状态
        if (!flag) {
            sourceVO.setActive(true);
            sourceResult.add(sourceVO);
        } else {
            sourceVO.setActive(false);
            sourceResult.add(sourceVO);
        }

        // 年代集合
        flag = false;
        List<YearVO> years = filmServiceApi.getYears();
        List<YearVO> yearResult = new ArrayList<>();
        YearVO yearVO = null;
        for (YearVO year : years) {
            if (year.getYearId().equals("99")) {
                yearVO = year;
                continue;
            }
            if (year.getYearId().equals(catId)) {
                flag = true;
                year.setActive(true);
            } else {
                year.setActive(false);
            }
            yearResult.add(year);
        }
        // 如果不存在，则默认将全部变为Active状态
        if (!flag) {
            yearVO.setActive(true);
            yearResult.add(yearVO);
        } else {
            yearVO.setActive(false);
            yearResult.add(yearVO);
        }

        filmConditionVO.setCatInfo(catResult);
        filmConditionVO.setSourceInfo(sourceResult);
        filmConditionVO.setYearInfo(yearResult);

        return ResponseVO.success(filmConditionVO);
    }

    // 获取首页信息
    @GetMapping(value = "getFilms")
    public ResponseVO getFilms(FilmRequestVO filmRequestVO) {
        FilmVO filmVO = null;
        // 根据showType 判断影片查询类型
        switch (filmRequestVO.getShowType()) {
            case 1 :
                filmVO = filmServiceApi.getHotFilms(false, filmRequestVO.getPageSize(), filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(), filmRequestVO.getSourceId(), filmRequestVO.getYearId(), filmRequestVO.getCatId());
                break;
            case 2 :
                filmVO = filmServiceApi.getSoonFilms(false, filmRequestVO.getPageSize(), filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(), filmRequestVO.getSourceId(), filmRequestVO.getYearId(), filmRequestVO.getCatId());
                break;
            case 3 :
                filmVO = filmServiceApi.getClassicFilms(filmRequestVO.getPageSize(), filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(), filmRequestVO.getSourceId(), filmRequestVO.getYearId(), filmRequestVO.getCatId());
                break;
            default:
                filmVO = filmServiceApi.getHotFilms(false, filmRequestVO.getPageSize(), filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(), filmRequestVO.getSourceId(), filmRequestVO.getYearId(), filmRequestVO.getCatId());
                break;
        }
        // 根据 sortType 排序

        // 添加各种查询类型

        // 判断当前是第几页

        return ResponseVO.success(filmVO.getNowPage(), filmVO.getTotalPage(), IMG_PRE, filmVO.getFilmInfo());
    }

    @GetMapping(value = "films/{searchParam}")
    public ResponseVO films(@PathVariable("searchParam")String searchParam,
                            int searchType) throws ExecutionException, InterruptedException {
        // 根据searchType，判断查询类型
        FilmDetailVO filmDetailVO = filmServiceApi.getFilmDetail(searchType, searchParam);
        String filmId = filmDetailVO.getFilmId();

        // 查询影片详细信息 -> Dubbo 的异步获取
        // 获取影片描述信息
        filmAsyncServiceApi.getFilmDesc(filmId);
        Future<FilmDescVO> filmDescVOFuture = RpcContext.getContext().getFuture();
        // 获取图片信息
        filmAsyncServiceApi.getImgs(filmId);
        Future<ImgVO> imgVOFuture = RpcContext.getContext().getFuture();
        // 获取导演信息
        filmAsyncServiceApi.getDectInfo(filmId);
        Future<ActorVO> actorVOVOFuture = RpcContext.getContext().getFuture();

        // 获取演员信息
        filmAsyncServiceApi.getActors(filmId);
        Future<List<ActorVO>> actorVOListFuture = RpcContext.getContext().getFuture();

        // 组装 info 对象
        InfoRequstVO infoRequstVO = new InfoRequstVO();
        // 组织 Actor 属性

        ActorRequestVO actorRequestVO = new ActorRequestVO();
        actorRequestVO.setActors(actorVOListFuture.get());
        actorRequestVO.setDirector(actorVOVOFuture.get());

        infoRequstVO.setActors(actorRequestVO);
        infoRequstVO.setImgVO(imgVOFuture.get());
        infoRequstVO.setFilmId(filmId);
        infoRequstVO.setBiography(filmDescVOFuture.get().getBiography());

        filmDetailVO.setInfo04(infoRequstVO);
        return ResponseVO.success(IMG_PRE,filmDetailVO);
    }
}
