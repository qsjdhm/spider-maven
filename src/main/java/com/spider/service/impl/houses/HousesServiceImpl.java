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
     * @param number 页数
     */
    @Override
    public List<Houses> getListByPage(int number) throws IOException {



        List<Houses> housesList = new ArrayList<Houses>();
        String url = "http://newhouse.jn.fang.com/house/dianshang/b9"+number;


        Document pageDoc = Jsoup.connect(url).timeout(5000).get();  // 承载抓取到的每页房产商DOM数据
        Elements lis = pageDoc.select("#newhouse_loupai_list li");

        for (Element li : lis) {
            Houses houses = getDetailsByElement(li);
            String fdcName = houses.getFdcName();

            // 下潜地块数据查询
            System.out.println(fdcName);

            // 楼盘下的地块列表
            List<Floor> allFloorList = new ArrayList<Floor>();

            int floorNumber = 1;

            do {

                List<Floor> pageFloorList = new ArrayList<Floor>();
                try {
                    pageFloorList = floorService.getListByPage(fdcName, floorNumber);

                    for(Floor floor : pageFloorList) {
                        System.out.println("action----------");
                        System.out.println(floor.getName());
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }

                if (pageFloorList.size()==0) {
                    floorNumber = 0;
                } else {
                    floorNumber++;
                }
            } while (floorNumber > 0);



            housesList.add(houses);
        }


        return housesList;
    }



    public Houses getDetailsByElement(Element li) throws IOException {

        String sfwUrl = li.select(".nlc_details .nlcd_name a").attr("href");
        String name = li.select(".nlc_details .nlcd_name a").text();
        String cover = li.select(".nlc_img img").eq(1).attr("src");
        String address = li.select(".nlc_details .address a").text();

        Houses houses = new Houses();

        try {
            houses = getDetailsByUrl(sfwUrl);
        } catch (IOException e) {

        }

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
