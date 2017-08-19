package com.spider.action;

import com.spider.entity.Floor;
import com.spider.service.impl.houses.FloorServiceImpl;
import com.spider.service.impl.system.SpiderProgressServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FloorAction {

    SpiderProgressServiceImpl progressService = new SpiderProgressServiceImpl();
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

            // 组织同步信息数据列表
            List locationList = new ArrayList();
            locationList.add(housesName);

            try {
                progressService.addProgress(
                        "地块", "分页", number,
                        "开始", "", locationList, null
                );

                pageFloorList = floorService.getListByPage(housesName, number);

                progressService.addProgress(
                        "地块", "分页", number,
                        "完成", "", locationList, null
                );
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;

                    String url = "http://www.jnfdc.gov.cn/onsaling/index_"+number+".shtml?zn=all&pu=all&pn="+housesName+"&en=";
                    progressService.addProgress(
                            "地块", "分页", number,
                            "超时异常", url, locationList, e
                    );
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

        // 组织同步信息数据列表
        List locationList = new ArrayList();
        locationList.add(fdcName);

        try {
            progressService.addProgress(
                    "地块", "分页", number,
                    "开始", "", locationList, null
            );

            floorList = floorService.getListByPage(fdcName, number);

            progressService.addProgress(
                    "地块", "分页", number,
                    "完成", "", locationList, null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                String url = "http://www.jnfdc.gov.cn/onsaling/index_"+number+".shtml?zn=all&pu=all&pn="+fdcName+"&en=";
                progressService.addProgress(
                        "地块", "分页", number,
                        "超时异常", url, locationList, e
                );
            }
            e.printStackTrace();
        }
    }

    /**
     * 根据某一个的url同步此地块的所有信息
     */
    public void syncDetailsByUrl(String url) {

        try {
//            List locationList = new ArrayList();
//            locationList.add(fdcName);
//            locationList.add(fdcFloorName);
//            progressService.addProgress(
//                    "地块", "详情", 0,
//                    "开始", "", locationList, null
//            );

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
