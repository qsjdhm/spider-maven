package com.spider.dao;

import com.spider.entity.Reb;

/**
 * Created by zhangyan on 17/8/26.
 */
public interface RebMapper {
    Reb findByName(String name);
    void insertReb(Reb reb);
    void updateReb(Reb reb);

    Reb select();
    void insert();
    void update();
}
