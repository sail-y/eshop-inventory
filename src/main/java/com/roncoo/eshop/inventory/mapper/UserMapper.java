package com.roncoo.eshop.inventory.mapper;

import com.roncoo.eshop.inventory.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 测试用户信息Mapper
 * @author yangfan
 * @date 2018/02/18
 */
@Mapper
public interface UserMapper {
    User findUserInfo();
}
