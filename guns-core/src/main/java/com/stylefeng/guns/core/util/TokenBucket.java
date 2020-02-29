package com.stylefeng.guns.core.util;

/**
 * @author : wangwei
 * @date : Created in 2020/3/1 0:29
 * @description: 令牌桶
 * @modified By:
 * @version: ${version}
 */
public class TokenBucket {

    // 桶的容量
    private int bucketNums = 100;

    // 流入的速率
    private int rate = 1;

    // 当前令牌数量
    private int nowTokens;

    private long timestamp = getNowTime();

    private long getNowTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取时间
     * @param tokens
     * @return
     */
    private int min(int tokens) {
        if (bucketNums > tokens) {
            return tokens;
        } else {
            return bucketNums;
        }
    }

    /**
     * 获取令牌
     * @return
     */
    public boolean getToken() {
        // 记录来拿令牌的时间
        long nowTime = getNowTime();
        // 添加令牌【判断该有多少令牌】
        nowTokens = nowTokens + (int) ((nowTime - timestamp) * rate);
        // 添加以后的令牌数量与桶的容量那个小
        nowTokens = min(nowTokens);
        System.out.println("当前令牌数量: " + nowTokens);
        // 修改拿令牌的时间
        timestamp = nowTime;
        // 判断令牌是否足够
        if (nowTokens < 1) {
            return false;
        } else {
            nowTokens -= 1;
            return true;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        TokenBucket tokenBucket = new TokenBucket();
        for (int i = 0; i < 200; i++) {
            if (i == 10) {
                Thread.sleep(500);
            }
            System.out.println("第" + i + "次请求结果: " + tokenBucket.getToken());
        }
    }


}
