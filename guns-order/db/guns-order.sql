DROP TABLE IF EXISTS `meeting_order_t`;
CREATE TABLE meeting_order_t (
	UUID VARCHAR ( 50 ) PRIMARY KEY COMMENT '主键编号',
	cinema_id INT COMMENT '影院编号',
	field_id INT COMMENT '放映场次编号',
	film_id INT COMMENT '影片编号',
	seats_ids VARCHAR ( 50 ) COMMENT '已售座位编号',
	seats_name VARCHAR ( 200 ) COMMENT '已售座位名称',
	film_price DECIMAL ( 8, 2 ) COMMENT '品牌编号',
	order_price DECIMAL ( 8, 2 ) COMMENT '订单总金额',
	order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
	order_user INT COMMENT '下单人',
	order_status INT DEFAULT 0 COMMENT '0-待支付,1-已支付,2-已关闭'
) COMMENT '订单信息表' ENGINE = INNODB AUTO_INCREMENT = 2 CHARACTER
SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;


INSERT INTO `guns_rest`.`meeting_order_t` ( `UUID`, `cinema_id`, `field_id`, `film_id`, `seats_ids`, `seats_name`, `film_price`, `order_price`, `order_user`)
VALUES
	( 1, 1, 1, 2, '1,2,3,4', '第一排1座,第一排2座,第一排3座,第一排4座', 63.20, 126.41, 1 );

INSERT INTO `guns_rest`.`meeting_order_t` ( `UUID`, `cinema_id`, `field_id`, `film_id`, `seats_ids`, `seats_name`, `film_price`, `order_price`, `order_user` )
VALUES
	( 2, 1, 1, 2, '5,7,8', '第二排5座,第二排7座,第二排8座', 63.20, 126.41, 1 );
