package com.spider.action;

import com.spider.entity.Houses;
import com.spider.entity.Reb;
import com.spider.service.impl.houses.HousesServiceImpl;
import com.spider.service.impl.houses.RebServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HousesAction {

    /**
     * 供管理平台调用接口
     * 描述: 同步楼盘的所有信息
     */
    public void syncAllList() {
        // 1. 循环调用service方法获取数据
        int number = 1;
        List<Houses> housesList = new ArrayList<Houses>();

        HousesServiceImpl housesService = new HousesServiceImpl();
        do {
            try {
                housesList = housesService.getListByPage(number);

                for(Houses houses : housesList) {
//                    System.out.println("action----------");
//                    System.out.println(houses.getName());
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

            if (housesList.size()==0) {
                number = 0;
            } else {
                number++;
            }
        } while (number > 0);
        // 2. 在循环过程中socket通知管理平台同步进度（包括每页同步遇到的超时异常，供管理平台进一步操作）
        // 3. 根据service抛出的超时异常、代码异常生成日志
        // 4. 根据每页数据写入数据库
    }
}
