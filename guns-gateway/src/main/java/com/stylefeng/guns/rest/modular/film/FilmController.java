package com.stylefeng.guns.rest.modular.film;

import com.stylefeng.guns.rest.common.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.java2d.pipe.ValidatePipe;

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

    /**
     * API网关:
     * 1.功能聚合(API聚合)
     *   优点：
     *       1. 留个接口，一次请求，同一时刻节省了五次http请求
     *       2. 同一个接口对外 暴露，降低了前后端分离的难度和复杂度
     *    缺点：
     *       1. 一次获取数据过多，容易出现问题
     * @return
     */
    // 获取首页信息
    @GetMapping(value = "getIndex")
    public ResponseVO getIndex() {
        // 获取banner信息，
        //获取正在热映的电影

        // 获取即将上映的电影

        // 票房排行榜

        // 受欢迎的榜单

        // 获取前100名

        return null;
    }
}
