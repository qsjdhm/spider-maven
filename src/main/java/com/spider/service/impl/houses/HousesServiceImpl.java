package com.spider.service.impl.houses;

import com.spider.entity.Floor;
import com.spider.entity.Houses;
import com.spider.service.houses.IHousesService;
import com.spider.utils.AnalysisHouseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class HousesServiceImpl implements IHousesService {

    Logger logger = LogManager.getLogger(HousesServiceImpl.class.getName());

    private FloorServiceImpl floorService = new FloorServiceImpl();


    /**
     * 返回当前页的楼盘数据
     */
    @Override
    public List<Houses> getListByPage(int housesNumber) throws IOException {

        String sfwUrl = "http://newhouse.jn.fang.com/house/dianshang/b9"+housesNumber;
        List<Houses> housesList = new ArrayList<Houses>();

        Document pageDoc = Jsoup.connect(sfwUrl).timeout(100).get();  // 承载抓取到的每页房产商DOM数据
        Elements lis = pageDoc.select("#newhouse_loupai_list li");

        for (Element li : lis) {
            if (li.select(".clearfix").size() > 0) {
                try {
                    // 下潜地块数据查询
                    Houses houses = getDetailsByElement(li);

                    String fdcName = houses.getFdcName();
                    logger.info("抓取楼盘["+fdcName+"]详情数据完成！");
                    logger.info("正在抓取楼盘["+fdcName+"]下潜数据...........");

                    // 获取此楼盘的所有地块数据
                    List<Floor> housesFloorList = getFloorListByHousesName(fdcName);
                    houses.setFloorList(housesFloorList);

                    housesList.add(houses);
                    logger.info("抓取楼盘["+fdcName+"]下潜数据完成！");
                } catch (IOException e) {
                    if (e.toString().indexOf("Read timed out") > -1) {
                        // 错误信息
                        String sfwHousesName = li.select(".nlc_details .nlcd_name a").text();
                        String fdcHousesName = AnalysisHouseUtil.extractValidHousesName(sfwHousesName);
                        String sfwHousesUrl = li.select(".nlc_details .nlcd_name a").attr("href");
                        System.out.println("获取楼盘搜房网名称["+sfwHousesName+"]楼盘政府网名称["+fdcHousesName+"]搜房网url["+sfwHousesUrl+"]详情和下潜数据出错");

                        // 错误时外部需进行以下操作获取楼盘详情数据
//                        Houses houses = getDetailsByUrl(sfwHousesUrl);
//                        String fdcName = houses.getFdcName();
//                        List<Floor> housesFloorList = getFloorListByHousesName(fdcHousesName);
//                        houses.setFloorList(housesFloorList);
//                        return houses;
                    }
                    e.printStackTrace();
                }
            }
        }

        return housesList;
    }


    /**
     * 根据dom获取此楼盘的详情数据
     * 为了是补全部分数据（根据错误url时无法使用）
     */
    @Override
    public Houses getDetailsByElement(Element li) throws IOException {

        String sfwDetailsUrl = li.select(".nlc_details .nlcd_name a").attr("href");
        Houses houses = getDetailsByUrl(sfwDetailsUrl);
        return houses;
    }

    /**
     * 根据url获取楼盘详情数据（可单独供action调用）
     */
    @Override
    public Houses getDetailsByUrl(String url) throws IOException {

        String sfwUrl = url;
        String name = null;
        String cover = null;
        String address = null;
        String averagePrice = null;
        String openingDate = null;
        String pRebName = null;

        Document detailedDoc = Jsoup.connect(url).timeout(500).get();

        // 如果是公寓类型
        if (detailedDoc.select(".inf_left1 strong").text().equals("")) {
            name = detailedDoc.select(".lp-name span").text();
            cover = detailedDoc.select(".banner-img").attr("src");
            address = detailedDoc.select(".lp-type").eq(3).select("i").text();
            averagePrice = detailedDoc.select(".l-price strong").text();
            openingDate = detailedDoc.select(".lp-type").eq(2).select("a").text();
            pRebName = detailedDoc.select("#txt_developer").attr("value").trim();
        } else {
            name = detailedDoc.select(".inf_left1 strong").text();
            cover = detailedDoc.select(".bannerbg_pos a img").attr("src");
            address = detailedDoc.select("#xfdsxq_B04_12 span").text();
            averagePrice = detailedDoc.select(".prib").text();
            openingDate = detailedDoc.select(".kaipan").text();
            pRebName = detailedDoc.select("#txt_developer").attr("value").trim();
        }

        Houses houses = new Houses();
        houses.setName(name);
        houses.setFdcName(AnalysisHouseUtil.extractValidHousesName(name));
        houses.setSfwUrl(sfwUrl);
        houses.setCover(cover);
        houses.setAddress(address);
        houses.setAveragePrice(averagePrice);
        houses.setOpeningDate(openingDate);
        houses.setpRebName(pRebName);

        return houses;
    }

    /**
     * 根据楼盘名称获取它的所有地块列表
     */
    @Override
    public List<Floor> getFloorListByHousesName(String name) throws IOException {
        // 此楼盘下所有地块列表
        List<Floor> allFloorList = new ArrayList<Floor>();
        int number = 1;
        boolean isTimedOut = false;

        do {
            List<Floor> pageFloorList = new ArrayList<Floor>();

            try {
                // 获取此楼盘的第number页数据
                pageFloorList = floorService.getListByPage(name, number);

                System.out.println(name+"的地块列表----------");
                for(Floor floor : pageFloorList) {
                    System.out.println("地块："+floor.getName());
                }
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;

                    // 错误信息
                    System.out.println("获取楼盘名称["+name+"]的地块列表第"+number+"页超时失败："+e);

                    // 错误时外部需进行以下操作获取此楼盘楼盘第number页的列表数据
//                    List<Floor> pageFloorList = floorService.getListByPage(name, number);
//                    return pageFloorList;
                } else {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw, true));

                    logger.error("抓取楼盘["+name+"]第["+number+"]页列表失败："+sw.getBuffer().toString());
                }
            }

            // 如果这页数据为空，并且是由爬虫超时导致的，就继续获取第2页
            // 如果第2页数据为空并超时，继续第3页
            // 如果第3页数据为空，但是不是超时导致的，代表获取数据完成，结束循环
            if (pageFloorList.size()==0) {
                if (isTimedOut) {
                    number++;
                } else {
                    number = 0;
                }
            } else {
                number++;
                // 把每页的地块数据添加到全部的地块列表中
                for(Floor floor : pageFloorList) {
                    allFloorList.add(floor);
                }
            }
        } while (number > 0);

        return allFloorList;
    }


}
