package com.spider.action;

import com.spider.entity.Floor;
import com.spider.entity.Plots;
import com.spider.service.impl.houses.PlotsServiceImpl;
import com.spider.service.impl.system.SpiderProgressServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class plotsAction {

    SpiderProgressServiceImpl progressService = new SpiderProgressServiceImpl();
    PlotsServiceImpl plotsService = new PlotsServiceImpl();

    /**
     * 同步单元楼的所有信息
     * syncAllList("绿地城一期住宅项目", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=44ae60a6-352e-4bfb-8300-382eaa3835e4")
     */
    public void syncAllList(String floorName, String floorDetailsUrl) {

        // 1. 循环调用service方法获取数据
        List<Plots> allPlotsList = new ArrayList<Plots>();
        int number = 1;
        boolean isTimedOut = false;

        do {
            List<Plots> pagePlotsList = new ArrayList<Plots>();

            // 组织同步信息数据列表
            List locationList = new ArrayList();
            locationList.add(floorName);

            try {
                progressService.addProgress(
                        "单元楼", "分页", number,
                        "开始", "", locationList, null
                );

                pagePlotsList = plotsService.getListByPage(floorDetailsUrl, number);

                progressService.addProgress(
                        "单元楼", "分页", number,
                        "完成", "", locationList, null
                );
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;

                    String url = floorDetailsUrl.replace("show", "show_"+number);
                    progressService.addProgress(
                            "单元楼", "分页", number,
                            "超时异常", url, locationList, e
                    );
                }
                e.printStackTrace();
            }

            // 如果这页数据为空，并且是由爬虫超时导致的，就继续获取第2页
            // 如果第2页数据为空并超时，继续第3页
            // 如果第3页数据为空，但是不是超时导致的，代表获取数据完成，结束循环
            if (pagePlotsList.size()==0) {
                if (isTimedOut) {
                    number++;
                } else {
                    number = 0;
                }
            } else {
                number++;
                // 把每页的地块数据添加到全部的地块列表中
                for(Plots floor : pagePlotsList) {
                    allPlotsList.add(floor);
                }
            }
        } while (number > 0);
        // 2. 在循环过程中socket通知管理平台同步进度（包括每页同步遇到的超时异常，供管理平台进一步操作）
        // 3. 根据service抛出的超时异常、代码异常生成日志
        // 4. 根据每页数据写入数据库
    }


    /**
     * 根据页数同步此页单元楼的数据列表
     * syncListByPage("绿地城一期住宅项目", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=44ae60a6-352e-4bfb-8300-382eaa3835e4")
     */
    public void syncListByPage(String floorName, String floorDetailsUrl, int number) {

        List<Plots> plotsList = new ArrayList<Plots>();
        // 组织同步信息数据列表
        List locationList = new ArrayList();
        locationList.add(floorName);

        try {
            progressService.addProgress(
                    "单元楼", "分页", number,
                    "开始", "", locationList, null
            );

            plotsList = plotsService.getListByPage(floorDetailsUrl, number);

            progressService.addProgress(
                    "单元楼", "分页", number,
                    "完成", "", locationList, null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                String url = floorDetailsUrl.replace("show", "show_"+number);
                progressService.addProgress(
                        "单元楼", "分页", number,
                        "超时异常", url, locationList, e
                );
            }
            e.printStackTrace();
        }
    }


    /**
     * 根据某一个的url同步此单元楼的所有信息
     * syncDetailsByUrl("九英里颢苑", "2#楼", url)
     */
    public void syncDetailsByUrl(String floorName, String fdcPlotsName, String url) {

        Plots plots = new Plots();
        List locationList = new ArrayList();
        locationList.add(floorName);
        locationList.add(fdcPlotsName);

        try {
            progressService.addProgress(
                    "单元楼", "详情", 0,
                    "开始", "", locationList, null
            );

            plots = plotsService.getDetailsByUrl(url);

            progressService.addProgress(
                    "单元楼", "详情", 0,
                    "完成", "", locationList, null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                progressService.addProgress(
                        "单元楼", "详情", 0,
                        "超时异常", url, locationList, null
                );
            }
            e.printStackTrace();
        }
    }

}
