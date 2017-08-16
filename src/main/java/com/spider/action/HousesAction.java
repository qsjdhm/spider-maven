package com.spider.action;

import com.spider.Main;
import com.spider.entity.Houses;
import com.spider.service.impl.houses.HousesServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HousesAction {

    HousesServiceImpl housesService = new HousesServiceImpl();
    Logger logger = LogManager.getLogger(HousesAction.class.getName());


    /**
     * 同步楼盘的所有信息
     */
    public void syncAllList() {
        // 1. 循环调用service方法获取数据
        List<Houses> allHousesList = new ArrayList<Houses>();
        int number = 1;
        boolean isTimedOut = false;

        do {
            List<Houses> pageHousesList = new ArrayList<Houses>();
            try {
                pageHousesList = housesService.getListByPage(number);
                System.out.println("第"+number+"页楼盘列表----------");
                for(Houses houses : pageHousesList) {
                    System.out.println(houses.getName());
                }
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;
                    logger.error("同步楼盘第"+number+"页列表超时失败："+e);
                } else {
                    logger.error("同步楼盘第"+number+"页列表失败："+e);
                }
                e.printStackTrace();
            }

            // 如果这页数据为空，并且是由爬虫超时导致的，就继续获取第2页
            // 如果第2页数据为空并超时，继续第3页
            // 如果第3页数据为空，但是不是超时导致的，代表获取数据完成，结束循环
            if (pageHousesList.size()==0) {
                if (isTimedOut) {
                    number++;
                } else {
                    number = 0;
                }
            } else {
                number++;
                // 把每页的地块数据添加到全部的地块列表中
                for(Houses houses : pageHousesList) {
                    allHousesList.add(houses);
                }
            }
        } while (number > 0);
        // 2. 在循环过程中socket通知管理平台同步进度（包括每页同步遇到的超时异常，供管理平台进一步操作）
        // 3. 根据service抛出的超时异常、代码异常生成日志
        // 4. 根据每页数据写入数据库
    }

    /**
     * 根据页数同步此页楼盘的数据列表
     */
    public void syncListByPage(int number) {

        List<Houses> housesList = new ArrayList<Houses>();

        try {
            housesList = housesService.getListByPage(number);
            for(Houses houses : housesList) {
                System.out.println(houses.getName());
            }
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {
                logger.error("同步楼盘第"+number+"页列表超时失败："+e);
            }
            e.printStackTrace();
        }
    }

    /**
     * 根据某一个的url同步此个楼盘的所有信息
     */
    public void syncDetailsByUrl(String url) {

        try {
            Houses houses = housesService.getDetailsByUrl(url);
            System.out.println(houses.getName());
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {
                logger.error("同步楼盘url["+url+"]详情数据超时失败："+e);
            }
            e.printStackTrace();
        }
    }
}
