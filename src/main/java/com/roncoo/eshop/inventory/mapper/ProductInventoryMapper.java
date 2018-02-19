package com.roncoo.eshop.inventory.mapper;

import com.roncoo.eshop.inventory.model.ProductInventory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yangfan
 * @date 2018/02/19
 */

@Mapper
public interface ProductInventoryMapper {

    /**
     * 更新库存数量
     */
    void updateProductInventory(ProductInventory productInventory);

    ProductInventory findProductInventory(Integer productId);
}
