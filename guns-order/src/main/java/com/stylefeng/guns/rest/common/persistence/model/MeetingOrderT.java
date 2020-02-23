package com.stylefeng.guns.rest.common.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author david
 * @since 2020-02-23
 */
@TableName("meeting_order_t")
public class MeetingOrderT extends Model<MeetingOrderT> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键编号
     */
    @TableId(value = "UUID")
    private String uuid;
    /**
     * 影院编号
     */
    @TableField("cinema_id")
    private Integer cinemaId;
    /**
     * 放映场次编号
     */
    @TableField("field_id")
    private Integer fieldId;
    /**
     * 影片编号
     */
    @TableField("film_id")
    private Integer filmId;
    /**
     * 已售座位编号
     */
    @TableField("seats_ids")
    private String seatsIds;
    /**
     * 已售座位名称
     */
    @TableField("seats_name")
    private String seatsName;
    /**
     * 品牌编号
     */
    @TableField("film_price")
    private BigDecimal filmPrice;
    /**
     * 订单总金额
     */
    @TableField("order_price")
    private BigDecimal orderPrice;
    /**
     * 下单时间
     */
    @TableField("order_time")
    private Date orderTime;
    /**
     * 下单人
     */
    @TableField("order_user")
    private Integer orderUser;
    /**
     * 0-待支付,1-已支付,2-已关闭
     */
    @TableField("order_status")
    private Integer orderStatus;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Integer cinemaId) {
        this.cinemaId = cinemaId;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public String getSeatsIds() {
        return seatsIds;
    }

    public void setSeatsIds(String seatsIds) {
        this.seatsIds = seatsIds;
    }

    public String getSeatsName() {
        return seatsName;
    }

    public void setSeatsName(String seatsName) {
        this.seatsName = seatsName;
    }

    public BigDecimal getFilmPrice() {
        return filmPrice;
    }

    public void setFilmPrice(BigDecimal filmPrice) {
        this.filmPrice = filmPrice;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(Integer orderUser) {
        this.orderUser = orderUser;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "MeetingOrderT{" +
        "uuid=" + uuid +
        ", cinemaId=" + cinemaId +
        ", fieldId=" + fieldId +
        ", filmId=" + filmId +
        ", seatsIds=" + seatsIds +
        ", seatsName=" + seatsName +
        ", filmPrice=" + filmPrice +
        ", orderPrice=" + orderPrice +
        ", orderTime=" + orderTime +
        ", orderUser=" + orderUser +
        ", orderStatus=" + orderStatus +
        "}";
    }
}
