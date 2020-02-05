package com.david.meeting.api.film;

import com.david.meeting.api.film.vo.*;

import java.util.List;

/**
 * Dubbo 的异步调用
 */
public interface FilmAsyncServiceApi {

    /**
     * 获取影片描述信息
     * @param filmId
     * @return
     */
    FilmDescVO getFilmDesc(String filmId);

    /**
     * 获取图片信息
     * @param filmId
     * @return
     */
    ImgVO getImgs(String filmId);

    /**
     * 获取导演信息
     * @param filmId
     * @return
     */
    ActorVO getDectInfo(String filmId);

    /**
     * 获取演员信息
     * @param filmId
     * @return
     */
    List<ActorVO> getActors(String filmId);

}
