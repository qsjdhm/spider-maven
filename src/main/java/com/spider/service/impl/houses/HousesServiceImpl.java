package com.spider.service.impl.houses;

import com.spider.entity.Floor;
import com.spider.entity.Houses;
import com.spider.service.houses.IHousesService;
import com.spider.utils.AnalysisHouseUtil;
import com.spider.utils.Constant;
import com.spider.utils.LogFile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HousesServiceImpl implements IHousesService {

    private FloorServiceImpl floorService = new FloorServiceImpl();

    /**
     * 返回当前页的楼盘数据
     * @param housesNumber 页数
     */
    @Override
    public List<Houses> getListByPage(int housesNumber) throws IOException {

        String url = "http://newhouse.jn.fang.com/house/dianshang/b9"+housesNumber;

        List<Houses> housesList = new ArrayList<Houses>();

        Document pageDoc = null;

        try {
            pageDoc = Jsoup.connect(url).timeout(5000).get();  // 承载抓取到的每页房产商DOM数据

            Elements lis = pageDoc.select("#newhouse_loupai_list li");

            for (Element li : lis) {
                Houses houses = new Houses();

                try {
                    // 下潜地块数据查询
                    houses = getDetailsByElement(li);
                    String fdcName = houses.getFdcName();
                    System.out.println("楼盘名称："+fdcName);
                    System.out.println("楼盘url："+url);
                    List<Floor> housesFloorList = floorService.getListByHousesName(fdcName);
                    houses.setFloorList(housesFloorList);

                    housesList.add(houses);
                } catch (IOException e) {
                    String name = li.select(".nlc_details .nlcd_name a").text();
                    System.out.println("获取楼盘["+name+"]数据出错");
                }
            }

        } catch (IOException e) {
            System.out.println("获取楼盘第"+housesNumber+"页["+url+"]失败："+e);
            e.printStackTrace();
        }

        return housesList;
    }


    public Houses getDetailsByElement(Element li) throws IOException {

        String sfwUrl = li.select(".nlc_details .nlcd_name a").attr("href");
        String name = li.select(".nlc_details .nlcd_name a").text();
        String cover = li.select(".nlc_img img").eq(1).attr("src");
        String address = li.select(".nlc_details .address a").text();

        Houses houses = getDetailsByUrl(sfwUrl);
        houses.setName(name);
        houses.setCover(cover);
        houses.setAddress(address);

        return houses;
    }

    public Houses getDetailsByUrl(String url) throws IOException {

        String sfwUrl = url;
        String name = null;
        String cover = null;
        String address = null;
        String averagePrice = null;
        String openingDate = null;
        String pRebName = null;

        Document detailedDoc = Jsoup.connect(url).timeout(5000).get();

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


}
