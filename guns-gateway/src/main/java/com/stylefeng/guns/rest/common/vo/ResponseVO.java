package com.stylefeng.guns.rest.common.vo;

import com.stylefeng.guns.rest.common.enums.BizResponseEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : wangwei
 * @date : Created in 2019/11/18 3:15
 * @description: 统一返回实体类
 * @modified By:
 * @version: v1.0.0
 */
@Data
public class ResponseVO<T> implements Serializable {

    private static final long serialVersionUID = -7459474151330766822L;

    // 返回状态【0-成功，1-业务失败，999-表示系统异常】
    private Integer status;

    // 返回信息
    private String msg;

    // 返回数据实体;
    private T data;

    // 图片前缀
    private String imgPre;

    // 当前页
    private int nowPage;

    private int totalPage;

    private ResponseVO() {
    }

    public static <T> ResponseVO success(T data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(BizResponseEnum.SUCCESS.getStatus());
        responseVO.setData(data);

        return responseVO;
    }

    public static <T> ResponseVO success(String imgPre, T data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(BizResponseEnum.SUCCESS.getStatus());
        responseVO.setImgPre(imgPre);
        responseVO.setData(data);

        return responseVO;
    }

    public static <T> ResponseVO success(int nowPage, int totalPage, String imgPre, T data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(BizResponseEnum.SUCCESS.getStatus());
        responseVO.setNowPage(nowPage);
        responseVO.setTotalPage(totalPage);
        responseVO.setImgPre(imgPre);
        responseVO.setData(data);

        return responseVO;
    }

    public static <T> ResponseVO success(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(BizResponseEnum.SUCCESS.getStatus());
        responseVO.setMsg(msg);

        return responseVO;
    }

    public static <T> ResponseVO serviceFail(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(BizResponseEnum.FAILD.getStatus());
        responseVO.setMsg(msg);

        return responseVO;
    }

    public static <T> ResponseVO appError() {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(BizResponseEnum.ERROR.getStatus());
        responseVO.setMsg(BizResponseEnum.ERROR.getMsg());

        return responseVO;
    }
}
