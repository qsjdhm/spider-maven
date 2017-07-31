package com.spider.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhangyan on 17/7/19.
 * 地块原型
 */
public class Floor {

    public Floor() {

    }

    private String name = null;  // 地块名称
    private String fdcUrl = null;  // 楼盘页面政府网URL
    private String canSold = null;  // 可售套数
    private String address = null;  // 项目地址
    private String county = null;  // 所在区县
    private String scale = null;  // 项目规模
    private String totalPlotsNumber = null;  // 总栋数
    private String property = null;  // 物业公司
    private String pHousesId = null;  // 所属楼盘ID
    private String pHousesName = null;  // 所属楼盘名称
    private List<Plots> plotsList = new ArrayList<Plots>();  // 下级单元楼列表

}
