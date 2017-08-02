package com.spider;

import com.spider.action.HousesAction;
import com.spider.action.RebAction;
import com.spider.entity.Houses;
import com.spider.service.impl.houses.RebServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangyan on 17/7/16.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        HousesAction housesAction = new HousesAction();
        housesAction.syncAllList();

    }
}

