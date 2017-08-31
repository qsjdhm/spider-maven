package com.spider.action;

import com.spider.entity.Floor;
import com.spider.entity.Houses;
import com.spider.service.impl.houses.FloorServiceImpl;
import com.spider.service.impl.houses.HousesServiceImpl;
import com.spider.service.impl.system.SpiderProgressServiceImpl;
import com.spider.service.impl.system.SqlServiceImpl;
import com.spider.utils.SysConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HousesAction {

    SpiderProgressServiceImpl progressService = new SpiderProgressServiceImpl();
    SqlServiceImpl sqlService = new SqlServiceImpl();
    HousesServiceImpl housesService = new HousesServiceImpl();
    FloorServiceImpl floorService = new FloorServiceImpl();


    /**
     * 同步楼盘的所有信息
     * syncAllList()
     */
    public void syncAllList() {
        // 1. 循环调用service方法获取数据
        List<Houses> allHousesList = new ArrayList<Houses>();
        int number = 1;
        boolean isTimedOut = false;

        do {
            List<Houses> pageHousesList = new ArrayList<Houses>();
            try {
                progressService.addProgress(
                        "楼盘", "分页", number,
                        "开始", "", new ArrayList(), null
                );

                pageHousesList = housesService.getListByPage(number);

                progressService.addProgress(
                        "楼盘", "分页", number,
                        "完成", "", new ArrayList(), null
                );
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;

                    String url = new SysConstant().HOUSES_LIST_URL + "/b9"+number;
                    progressService.addProgress(
                            "楼盘", "分页", number,
                            "超时异常", url, new ArrayList(), e
                    );
                }
                e.printStackTrace();
            }

            // 如果这页数据为空，并且是由爬虫超时导致的，就继续获取第2页
            // 如果第2页数据为空并超时，继续第3页
            // 如果第3页数据为空，但是不是超时导致的，代表获取数据完成，结束循环
            if (pageHousesList.size()==0) {
                if (isTimedOut) {
                    number++;
                } else {
                    number = 0;
                }
            } else {
                number++;
                // 把每页的地块数据添加到全部的地块列表中
                for(Houses houses : pageHousesList) {
                    allHousesList.add(houses);

                    // 对比数据是否需要更新
                    Houses findHouses = sqlService.housesSql().findByName(houses.getName());

                    if (findHouses == null) {
                        // 插入数据
                        sqlService.housesSql().insertHouses(houses);
                    } else if (!houses.getHash().equals(findHouses.getHash())) {
                        // 更新数据
                        sqlService.housesSql().updateHouses(houses);
                    }
                }

                // 每一页数据就提交到数据库保存起来
                sqlService.comment();
            }
        } while (number > 0);
        // 2. 在循环过程中socket通知管理平台同步进度（包括每页同步遇到的超时异常，供管理平台进一步操作）
        // 3. 根据service抛出的超时异常、代码异常生成日志
        // 4. 根据每页数据写入数据库
    }

    /**
     * 根据页数同步此页楼盘的数据列表
     * syncListByPage(2)
     */
    public void syncListByPage(int number) {

        try {
            progressService.addProgress(
                    "楼盘", "分页", number,
                    "开始", "", new ArrayList(), null
            );

            List<Houses> housesList = housesService.getListByPage(number);

            /**
             * 存储楼盘信息
             */
            for(Houses houses : housesList) {
                // 对比数据是否需要更新
                Houses findHouses = sqlService.housesSql().findByName(houses.getName());

                if (findHouses == null) {
                    // 插入数据
                    sqlService.housesSql().insertHouses(houses);
                } else if (!houses.getHash().equals(findHouses.getHash())) {
                    // 更新数据
                    sqlService.housesSql().updateHouses(houses);
                }


                /**
                 * 存储此楼盘的地块信息
                 */
                List<Floor> housesFloorList = houses.getFloorList();
                // 把每页的地块数据添加到全部的地块列表中
                for(Floor floor : housesFloorList) {

                    // 对比数据是否需要更新
                    Floor findFloor = sqlService.floorSql().findByName(floor.getName());

                    if (findFloor == null) {
                        // 插入数据
                        sqlService.floorSql().insertFloor(floor);
                    } else if (!floor.getHash().equals(findFloor.getHash())) {
                        // 更新数据
                        sqlService.floorSql().updateFloor(floor);
                    }
                }

                // 每一页数据就提交到数据库保存起来
                sqlService.comment();
            }

            // 每一页数据就提交到数据库保存起来
            sqlService.comment();






            progressService.addProgress(
                    "楼盘", "分页", number,
                    "完成", "", new ArrayList(), null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                String url = new SysConstant().HOUSES_LIST_URL + "/b9"+number;
                progressService.addProgress(
                        "楼盘", "分页", number,
                        "超时异常", url, new ArrayList(), e
                );
            }
            e.printStackTrace();
        }
    }

    /**
     * 根据某一个的url同步此个楼盘的所有信息
     * syncDetailsByUrl("中海国际社区", "http://zhonghaiguojishequ0531.fang.com")
     */
    public void syncDetailsByUrl(String name, String url) {

        Houses houses = new Houses();
        List locationList = new ArrayList();
        locationList.add(name);

        try {
            progressService.addProgress(
                    "楼盘", "详情", 0,
                    "开始", "", locationList, null
            );

            houses = housesService.getDetailsByUrl(url);
            // 组织楼盘下潜数据
            // 获取此楼盘的所有地块数据
            List<Floor> housesFloorList = housesService.getFloorListByHousesName(name);
            houses.setFloorList(housesFloorList);


            /**
             * 存储楼盘信息
             */
            // 对比数据是否需要更新
            Houses findHouses = sqlService.housesSql().findByName(houses.getName());

            if (findHouses == null) {
                // 插入数据
                sqlService.housesSql().insertHouses(houses);
            } else if (!houses.getHash().equals(findHouses.getHash())) {
                // 更新数据
                sqlService.housesSql().updateHouses(houses);
            }

            sqlService.comment();


            /**
             * 存储此楼盘的地块信息
             */
            // 把每页的地块数据添加到全部的地块列表中
            for(Floor floor : housesFloorList) {

                // 对比数据是否需要更新
                Floor findFloor = sqlService.floorSql().findByName(floor.getName());

                if (findFloor == null) {
                    // 插入数据
                    sqlService.floorSql().insertFloor(floor);
                } else if (!floor.getHash().equals(findFloor.getHash())) {
                    // 更新数据
                    sqlService.floorSql().updateFloor(floor);
                }
            }

            // 每一页数据就提交到数据库保存起来
            sqlService.comment();





            /**
             * 存储此楼盘的单元楼信息
             */

            progressService.addProgress(
                    "楼盘", "详情", 0,
                    "完成", "", locationList, null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                progressService.addProgress(
                        "楼盘", "详情", 0,
                        "超时异常", url, locationList, e
                );
            }
            e.printStackTrace();
        }
    }
}
