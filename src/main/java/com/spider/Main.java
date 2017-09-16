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

import java.io.*;
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
//        Thread.sleep(100);//调整时间，让服务端准备好
//        new Client().start();

        PlotsAction  plotsAction = new PlotsAction();
        plotsAction.syncAllList("中海国际社区B-2地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=c4d9a76b-b289-42b5-a65f-c99882645ff6");
//


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
                ServerSocket server = new ServerSocket(1888);
                Socket socket = server.accept();

                int a = 0;

                // 初始化输出流
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                // 从socket获取数据
                InputStream is = socket.getInputStream();
                DataInputStream dis = new DataInputStream(is);
                //System.out.println("client_msg:"+dis.readUTF());

                while(true){
                    // 打印客户端发送的数据
                    String abc = dis.readUTF();
                    System.out.println("client_msg:"+abc);
                    System.out.println("-----------------------");

                    if (abc.equals("close")) {

                        break;
                    }

//                    Thread.sleep(1000);
//                    // 向客户端输出数据
//                    out.writeUTF(getRandomStr());
//                    out.flush();
                }
                out.close();
                dis.close();
                socket.close();
                System.out.println("socket成功关闭！！！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static String getRandomStr(){
            String str = "";
            int ID = (int) (Math.random()*30);
            int x = (int) (Math.random()*200);
            int y = (int) (Math.random()*300);
            int z = (int) (Math.random()*10);
            str = "ID:"+ID+"/x:"+x+"/y:"+y+"/z:"+z;
            return str;
        }
    }

    static class Client extends Thread {
        public void run() {

            try{

                // 连接socket服务器
                Socket socket = new Socket("localhost",1083);

                // 初始化输出流
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                while(true){
                    // 先发送数据请求
                    out.writeUTF("gei dian bai");

                    // 从socket获取数据
                    InputStream is = socket.getInputStream();
                    DataInputStream dis  = new DataInputStream(is);
                    System.out.println("server_msg:"+dis.readUTF());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }


    }

}



