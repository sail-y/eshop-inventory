package com.roncoo.eshop.inventory.dao;

/**
 * redis操作
 * @author yangfan
 * @date 2018/02/18
 */
public interface RedisDAO {
    void set(String key, String value);

    String get(String key);

    void delete(String key);
}
