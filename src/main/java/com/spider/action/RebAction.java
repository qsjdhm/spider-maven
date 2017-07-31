package com.spider.action;

public class RebAction {

    /**
     * 供管理平台调用接口
     * 描述: 同步房产商的所有信息
     */
    public void syncAllList() {
        // 1. 循环调用service方法获取数据
        // 2. 在循环过程中socket通知管理平台同步进度（包括每页同步遇到的超时异常，供管理平台进一步操作）
        // 3. 根据service抛出的超时异常、代码异常生成日志
        // 4. 根据每页数据写入数据库
    }

    /**
     * 供管理平台调用接口
     * 描述: 根据某一页的url同步此页房产商的所有信息
     */
    public void syncPageListByUrl(String url) {

    }

    /**
     * 供管理平台调用接口
     * 描述: 根据某一个的url同步此个房产商的所有信息
     */
    public void syncDetailsByUrl(String url) {

    }

}
