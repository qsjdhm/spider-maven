package com.spider;

import com.spider.action.HousesAction;
import com.spider.action.RebAction;
import com.spider.entity.Houses;
import com.spider.service.impl.houses.RebServiceImpl;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zhangyan on 17/7/16.
 */
public class Main {
    public static void main(String[] args) throws IOException {



//        RebAction rebAction = new RebAction();
//        rebAction.syncAllList();

        HousesAction housesAction = new HousesAction();
        housesAction.syncAllList();

    }
}

