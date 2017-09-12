package com.spider;

import com.spider.action.*;
import com.spider.dao.RebMapper;
import com.spider.entity.Reb;
import com.spider.service.impl.system.SpiderProgressServiceImpl;
import com.spider.service.impl.system.SqlServiceImpl;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangyan on 17/7/16.
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

//        HousesAction housesAction = new HousesAction();
//        housesAction.syncListByPage(1);
// http://www.cnblogs.com/myitroad/p/5516963.html

        new TalkServer().start();
        Thread.sleep(100);//调整时间，让服务端准备好
        new Client().start();



//        new TCPServer();
//        new TCPlient();


        /**
         * 自动任务action调用实例
         */
//        TaskAction taskAction = new TaskAction();
//        taskAction.begin();



        /**
         * 同步异常补偿action调用实例
         */

//        RebAction rebAction = new RebAction();
//        rebAction.syncAllList();
//        rebAction.syncListByPage(2);
//        rebAction.syncDetailsByUrl("济南建邦置业有限公司", "http://www.jnfdc.gov.cn/kfqy/show/915c802f-f227-4cec-853d-e5161a90b0c4.shtml");


//        HousesAction housesAction = new HousesAction();
//        housesAction.syncAllList();
//        housesAction.syncListByPage(1);
//        housesAction.syncDetailsByUrl("中海国际社区", "http://zhonghaiguojishequ0531.fang.com");


//        FloorAction floorAction = new FloorAction();
//        floorAction.syncAllList("中海国际社区");
//        floorAction.syncListByPage("中海国际社区", 1);
//        floorAction.syncDetailsByUrl("中海国际社区", "中海国际社区B-2地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=c4d9a76b-b289-42b5-a65f-c99882645ff6");


//        PlotsAction  plotsAction = new PlotsAction();
//        plotsAction.syncAllList("中海国际社区B-2地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=c4d9a76b-b289-42b5-a65f-c99882645ff6");
//        plotsAction.syncListByPage("中海国际社区B-2地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=c4d9a76b-b289-42b5-a65f-c99882645ff6", 2);
//        plotsAction.syncDetailsByUrl("中海国际社区B-2地块", "67楼", "http://www.jnfdc.gov.cn/onsaling/bshow.shtml?bno=23c5bfad-f26f-4f8f-b1db-cf15b8a9e1ac");









    }



    static class TalkServer extends Thread {

        public void run() {
            try {
                ServerSocket server = new ServerSocket(1803);
                Socket socket = server.accept();
                //BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter os = new PrintWriter(socket.getOutputStream());
                BufferedReader is = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));

                String readline = null;
                System.out.println("Client:" + is.readLine());
                //readline = sin.readLine();
                os.println("dfghjkl;';lkjhgf");
//                while (!readline.equals("bye")) {
//                    os.println("dfghjkl;';lkjhgf");
//                    os.flush();
//                    System.out.println("Server1:" + readline);
//                    System.out.println("Client:" + is.readLine());
//
//                    //readline = sin.readLine();
//                }
                os.close();
                is.close();
                socket.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class Client extends Thread {
        public void run() {
            try{




                Socket client = new Socket("localhost", 1803);
                //BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter os = new PrintWriter(client.getOutputStream());
                BufferedReader is = new BufferedReader(new InputStreamReader(client.getInputStream()));
                //String readline = sin.readLine();
                os.println("hello, server. im client1");
                os.flush();
                os.println("hello, server. im client2");
                os.flush();
                os.println("hello, server. im client3");
                os.flush();
                System.out.println("Server2:" + is.readLine());
            }catch(Exception ex){ex.printStackTrace();}
        }
    }

}



