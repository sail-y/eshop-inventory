package com.roncoo.eshop.inventory.request;

/**
 * @author yangfan
 * @date 2018/02/19
 */
public interface Request {

    void process();

    Integer getProductId();

    boolean isForceRefresh();
}
