package com.spider.action;

import com.spider.entity.Floor;
import com.spider.service.impl.houses.FloorServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FloorAction {

    FloorServiceImpl floorService = new FloorServiceImpl();


    /**
     * 同步地块的地块的所有信息
     */
    public void syncAllList() {

        // 1. 循环调用service方法获取数据
        List<Floor> allFloorList = new ArrayList<Floor>();
        int number = 1;
        boolean isTimedOut = false;
        String housesName = "中海国际";

        do {
            List<Floor> pageFloorList = new ArrayList<Floor>();
            try {
                pageFloorList = floorService.getListByPage(housesName, number);
                System.out.println("第"+number+"页地块列表----------");
                for(Floor floor : pageFloorList) {
                    System.out.println(floor.getName());
                }
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;

                    System.out.println("同步楼盘["+housesName+"]第"+number+"页列表超时失败："+e);
                }
                e.printStackTrace();
            }

            // 如果这页数据为空，并且是由爬虫超时导致的，就继续获取第2页
            // 如果第2页数据为空并超时，继续第3页
            // 如果第3页数据为空，但是不是超时导致的，代表获取数据完成，结束循环
            if (pageFloorList.size()==0) {
                if (isTimedOut) {
                    number++;
                } else {
                    number = 0;
                }
            } else {
                number++;
                // 把每页的地块数据添加到全部的地块列表中
                for(Floor floor : pageFloorList) {
                    allFloorList.add(floor);
                }
            }
        } while (number > 0);
        // 2. 在循环过程中socket通知管理平台同步进度（包括每页同步遇到的超时异常，供管理平台进一步操作）
        // 3. 根据service抛出的超时异常、代码异常生成日志
        // 4. 根据每页数据写入数据库
    }


    /**
     * 根据页数同步此页地块的数据列表
     */
    public void syncListByPage(String fdcName, int number) {

        fdcName = "中海国际";
        List<Floor> floorList = new ArrayList<Floor>();

        try {
            floorList = floorService.getListByPage(fdcName, number);
            for(Floor floor : floorList) {
                System.out.println(floor.getName());
            }
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {
                System.out.println("同步楼盘["+fdcName+"]地块第"+number+"页列表超时失败："+e);
            }
            e.printStackTrace();
        }
    }

    /**
     * 根据某一个的url同步此地块的所有信息
     */
    public void syncDetailsByUrl(String url) {

        try {
            Floor floor = floorService.getDetailsByUrl(url);
            System.out.println(floor.getName());
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {
                System.out.println("同步地块url["+url+"]详情数据超时失败："+e);
            }
            e.printStackTrace();
        }
    }


}
