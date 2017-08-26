package com.spider.dao;

import com.spider.entity.Reb;

/**
 * Created by zhangyan on 17/8/26.
 */
public interface RebMapper {
    Reb findById(int id);
    void insertReb(Reb reb);
}
