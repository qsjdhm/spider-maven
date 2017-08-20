package com.spider.action;

import com.spider.entity.Reb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.spider.service.impl.houses.RebServiceImpl;
import com.spider.service.impl.system.SpiderProgressServiceImpl;

public class RebAction {

    SpiderProgressServiceImpl progressService = new SpiderProgressServiceImpl();

    RebServiceImpl rebService = new RebServiceImpl();

    /**
     * 同步房产商的所有信息
     */
    public void syncAllList() {
        // 1. 循环调用service方法获取数据
        List<Reb> allRebList = new ArrayList<Reb>();
        int number = 1;
        boolean isTimedOut = false;

        do {
            List<Reb> pageRebList = new ArrayList<Reb>();
            try {
                progressService.addProgress(
                        "房产商", "分页", number,
                        "开始", "", new ArrayList(), null
                );

                pageRebList = rebService.getListByPage(number);

                progressService.addProgress(
                        "房产商", "分页", number,
                        "完成", "", new ArrayList(), null
                );
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;

                    String url = "http://www.jnfdc.gov.cn/kfqy/index_"+number+".shtml";
                    progressService.addProgress(
                            "房产商", "分页", number,
                            "超时异常", url, new ArrayList(), e
                    );
                }
                e.printStackTrace();
            }

            // 如果这页数据为空，并且是由爬虫超时导致的，就继续获取第2页
            // 如果第2页数据为空并超时，继续第3页
            // 如果第3页数据为空，但是不是超时导致的，代表获取数据完成，结束循环
            if (pageRebList.size()==0) {
                if (isTimedOut) {
                    number++;
                } else {
                    number = 0;
                }
            } else {
                number++;
                // 把每页的地块数据添加到全部的地块列表中
                for(Reb reb : pageRebList) {
                    allRebList.add(reb);
                }
            }
        } while (number > 0);
        // 2. 在循环过程中socket通知管理平台同步进度（包括每页同步遇到的超时异常，供管理平台进一步操作）
        // 3. 根据service抛出的超时异常、代码异常生成日志
        // 4. 根据每页数据写入数据库


    }


    /**
     * 根据页数同步此页房产商的数据列表
     */
    public void syncListByPage(int number) {

        List<Reb> rebList = new ArrayList<Reb>();
        try {
            progressService.addProgress(
                    "房产商", "分页", number,
                    "开始", "", new ArrayList(), null
            );

            rebList = rebService.getListByPage(number);

            progressService.addProgress(
                    "房产商", "分页", number,
                    "完成", "", new ArrayList(), null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                String url = "http://www.jnfdc.gov.cn/kfqy/index_"+number+".shtml";
                progressService.addProgress(
                        "房产商", "分页", number,
                        "超时异常", url, new ArrayList(), e
                );
            }
            e.printStackTrace();
        }
    }

    /**
     * 根据某一个的url同步此个房产商的所有信息
     */
    public void syncDetailsByUrl(String name, String url) {

        Reb reb = new Reb();
        List locationList = new ArrayList();
        locationList.add(name);

        try {
            progressService.addProgress(
                    "房产商", "详情", 0,
                    "开始", "", locationList, null
            );

            reb = rebService.getDetailsByUrl(url);

            progressService.addProgress(
                    "房产商", "详情", 0,
                    "完成", "", locationList, null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                progressService.addProgress(
                        "房产商", "详情", 0,
                        "超时异常", url, locationList, e
                );
            }
            e.printStackTrace();
        }
    }

}
