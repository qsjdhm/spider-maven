package com.spider.service.impl.system;

import com.spider.service.impl.houses.RebServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by zhangyan on 2017/7/25.
 * 爬虫同步进度业务
 */
public class SpiderProgressServiceImpl {

    private static Logger logger = LogManager.getLogger(SpiderProgressServiceImpl.class.getName());


    // 当前进度列表
    private static List<Map<String, Object>> progressList = new ArrayList<Map<String, Object>>();

    SpiderProgressServiceImpl () {
        ServerSocket serverSocket = null;    //用serversocket来启动服务器，并指定端口号
        try {
            serverSocket = new ServerSocket(10000);
            System.out.println("服务器启动。。。");

            while (true) {
                // 一旦有堵塞, 则表示服务器与客户端获得了连接
                Socket client = serverSocket.accept();
                // 处理这次连接
                new HandlerThread(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    http://blog.csdn.net/defonds/article/details/7971259

    /**
     * 外部会逐条往progressList添加进度信息
     * 外部根据返回数据拼接成想要的字符串
     * @param label         标题（房产商、楼盘）
     * @param type          分页、详情
     * @param page          当前第几页
     * @param state         同步状态（开始、完毕、超时异常）
     * @param url           同步源地址
     * @param locationList  所属列表（哪个楼盘、哪个地块、哪个单元楼）
     * @param e             异常信息
     */

    public static void addProgress(String label, String type, int page, String state, String url, List<String> locationList, Exception e) {
        Map<String, Object> progress = new HashMap<String, Object>();

        // 再次同步会根据url进行同步，其他信息都只是提示
        progress.put("label", label);
        progress.put("type", type);
        progress.put("page", page);
        progress.put("state", state);
        progress.put("url", url);
        progress.put("locationList", locationList);
        progressList.add(progress);



        // 1. 组织同步进度信息
        String info = "同步"+label;
        if (type.equals("分页")) {
            for (String loaction : locationList) {
                info += "["+loaction+"]";
            }
            info += "第["+page+"]页数据"+state;
        } else if (type.equals("详情")) {
            for (String loaction : locationList) {
                info += "["+loaction+"]";
            }
            info += "详情数据"+state;
        }


        // 2. 处理log4j日志
        if (state.equals("超时异常")) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String errorStr = info + System.getProperty("line.separator") + "url["+url+"]" + System.getProperty("line.separator") + sw.getBuffer().toString();
            logger.error(errorStr);
        } else {
            logger.info(info);
        }

        // 3. 应该把所有参数通过websocket通知前台


        System.out.println(info);
    }

    /**
     * 外部获取全部的错误列表
     */
    public static List<Map<String, Object>> getProgressList() {
        return progressList;
    }

    /**
     * 外部获取全部的进度列表
     * @param isClear 是否需要清除进度列表
     */
    public static List<Map<String, Object>> getProgressList(boolean isClear) {
        List<Map<String, Object>> tempProgressList = new ArrayList<Map<String, Object>>(progressList);

        // 需要清空数据
        if (isClear) {
            clearProgressList();
        }
        return tempProgressList;
    }

    /**
     * 清空进度列表
     */
    public static void clearProgressList() {
        progressList.clear();
    }


}
