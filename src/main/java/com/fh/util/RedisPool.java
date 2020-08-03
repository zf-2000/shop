package com.fh.util;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private static JedisPool jedisPool = null;

    // 设置连接池信息
    private static void initPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);// 最大连接数
        jedisPoolConfig.setMinIdle(100);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(jedisPoolConfig,"127.0.0.1",6379);
    }

    // 静态块，在加载类的时候执行，只执行一次
    static {
        initPool();
    }

    // 提供公共方法，供外界调用
    public static Jedis getRedisPool(){
        return  jedisPool.getResource();
    }
}
