package com.spider.dao;

import com.spider.entity.Houses;

/**
 * Created by zhangyan on 17/8/30.
 */
public interface HousesMapper {
    Houses findByName(String name);
    void insertHouses(Houses houses);
    void updateHouses(Houses houses);
}
