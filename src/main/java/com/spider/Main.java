package com.spider;

import com.spider.action.*;
import com.spider.dao.RebMapper;
import com.spider.entity.Reb;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangyan on 17/7/16.
 */
public class Main {
    public static void main(String[] args) throws IOException {



        RebAction rebAction = new RebAction();
        rebAction.syncListByPage(2);








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
}

