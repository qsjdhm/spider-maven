package com.spider.action;

import com.spider.entity.Reb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.spider.service.impl.houses.RebServiceImpl;

public class RebAction {

    /**
     * 供管理平台调用接口
     * 描述: 同步房产商的所有信息
     */
    public void syncAllList() {
        // 1. 循环调用service方法获取数据
        int number = 1;
        List<Reb> rebList = new ArrayList<Reb>();

        RebServiceImpl rebService = new RebServiceImpl();
        do {
            try {
                rebList = rebService.getListByPage(number);

                for(Reb reb : rebList) {
                    System.out.println(reb.getName());
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

            if (rebList.size()==0) {
                number = 0;
            } else {
                number++;
            }
        } while (number > 0);
        // 2. 在循环过程中socket通知管理平台同步进度（包括每页同步遇到的超时异常，供管理平台进一步操作）
        // 3. 根据service抛出的超时异常、代码异常生成日志
        // 4. 根据每页数据写入数据库
    }


    /**
     * 供管理平台调用接口
     * 描述: 根据某一页的url同步此页房产商的所有信息
     */
    public void syncPageListByUrl(String url) {

        List<Reb> rebList = new ArrayList<Reb>();

        RebServiceImpl rebService = new RebServiceImpl();
        try {
            rebList = rebService.getListByUrl(url);

            for(Reb reb : rebList) {
                System.out.println(reb.getName());
            }
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

    /**
     * 供管理平台调用接口
     * 描述: 根据某一个的url同步此个房产商的所有信息
     */
    public void syncDetailsByUrl(String url) {

        Reb reb = new Reb();

        RebServiceImpl rebService = new RebServiceImpl();
        try {
            reb = rebService.getDetailsByUrl(url);
            System.out.println(reb.getName());
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

}
