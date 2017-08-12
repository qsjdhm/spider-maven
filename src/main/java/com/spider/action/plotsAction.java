package com.spider.action;

import com.spider.entity.Floor;
import com.spider.entity.Plots;
import com.spider.service.impl.houses.PlotsServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class plotsAction {

    PlotsServiceImpl plotsService = new PlotsServiceImpl();

    /**
     * 同步单元楼的所有信息
     */
    public void syncAllList() {

        // 1. 循环调用service方法获取数据
        List<Plots> allPlotsList = new ArrayList<Plots>();
        int number = 1;
        boolean isTimedOut = false;
        String floorDetailsUrl = "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=44ae60a6-352e-4bfb-8300-382eaa3835e4";

        do {
            List<Plots> pagePlotsList = new ArrayList<Plots>();
            try {
                pagePlotsList = plotsService.getListByPage(floorDetailsUrl, number);
                System.out.println("第"+number+"页单元楼列表----------");
                for(Plots plots : pagePlotsList) {
                    System.out.println(plots.getName());
                }
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;

                    System.out.println("同步单元楼["+floorDetailsUrl+"]第"+number+"页列表超时失败："+e);
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
     */
    public void syncListByPage(String floorDetailsUrl, int number) {

        floorDetailsUrl = "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=44ae60a6-352e-4bfb-8300-382eaa3835e4";
        List<Plots> plotsList = new ArrayList<Plots>();

        try {
            plotsList = plotsService.getListByPage(floorDetailsUrl, number);
            for(Plots plots : plotsList) {
                System.out.println(plots.getName());
            }
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {
                System.out.println("同步单元楼["+floorDetailsUrl+"]第"+number+"页列表超时失败："+e);
            }
            e.printStackTrace();
        }
    }


    /**
     * 根据某一个的url同步此单元楼的所有信息
     */
    public void syncDetailsByUrl(String url) {

        try {
            Plots plots = plotsService.getDetailsByUrl(url);
            System.out.println(plots.getName());
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {
                System.out.println("同步单元楼url["+url+"]详情数据超时失败："+e);
            }
            e.printStackTrace();
        }
    }

}
