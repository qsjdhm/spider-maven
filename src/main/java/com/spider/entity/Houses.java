package com.spider.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhangyan on 17/7/19.
 * 楼盘原型
 */
public class Houses {

    public Houses() {

    }

    private String housesName = null;  // 楼盘名称
    private String fdcHousesName = null;  // 政府网查询时用的楼盘名称
    private String sfwUrl = null;  // 搜房网URL
    private String cover = null;  // 封面
    private String address = null;  // 地址
    private String averagePrice = null;  // 均价
    private String openingDate = null;  // 开盘日期
    private String pRebName = null;  // 所属房产商名称
    private List<Floor> floorList = new ArrayList<Floor>();  // 下级地块列表


}
