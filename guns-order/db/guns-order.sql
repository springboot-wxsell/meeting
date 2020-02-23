DROP TABLE IF EXISTS `meeting_order_t`;
CREATE TABLE meeting_order_t (
	UUID INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键编号',
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