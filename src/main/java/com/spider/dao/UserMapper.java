package com.spider.dao;

import com.spider.entity.User;

public interface UserMapper {
    User findById(int id);
}
