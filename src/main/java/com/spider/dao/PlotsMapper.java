package com.spider.dao;

import com.spider.entity.Plots;

import java.util.List;

/**
 * Created by zhangyan on 17/8/30.
 */
public interface PlotsMapper {
    Plots findByName(String name);
    List<Plots> findByFloorName(String floorName);
    void insertPlots(Plots plots);
    void updatePlots(Plots plots);
}
