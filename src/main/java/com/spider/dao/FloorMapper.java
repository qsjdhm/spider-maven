package com.spider.dao;

import com.spider.entity.Floor;

import java.util.List;

/**
 * Created by zhangyan on 17/8/30.
 */
public interface FloorMapper {
    Floor findByName(String name);
    List<Floor> findByHousesName(String housesName);
    void insertFloor(Floor floor);
    void updateFloor(Floor floor);
}
