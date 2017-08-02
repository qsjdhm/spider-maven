package com.spider.service.impl.houses;

import com.spider.entity.Floor;
import com.spider.service.houses.IFloorService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FloorServiceImpl implements IFloorService {

    public List<Floor> getListByPage(String fdcName, int number) throws IOException {

        List<Floor> floorList = new ArrayList<Floor>();

        String url = "http://www.jnfdc.gov.cn/onsaling/index_"+number+".shtml?zn=all&pu=all&pn="+fdcName+"&en=";

        Document pageDoc = Jsoup.connect(url).timeout(5000).get();
        Elements trs = pageDoc.select(".project_table tr");

        // 因为抓取到的数据不规范，所以要自己组织为规范的数据格式
        for (Element tr : trs) {
            // 只获取有效数据的值
            if (tr.select("td").size() > 1) {


                Floor floor = getDetailsByElement(tr);
                floor.setpHousesName(fdcName);

                // 抓取详细信息
                floorList.add(floor);
            }
        }

        return floorList;
    }

    //    public List<Houses> getListByUrl(String url) throws IOException;

    public Floor getDetailsByElement(Element tr) throws IOException {

        Elements tds = tr.select("td");
        String floorName = tds.eq(1).attr("title");  // 地块名称
        String fdcUrl = "http://www.jnfdc.gov.cn" + tds.eq(1).select("a").attr("href");  // 地块详情页面政府网URL
        String canSold = tds.eq(4).text();  // 可售套数
        String address = tds.eq(2).text();  // 项目地址

        Floor floor = getDetailsByUrl(fdcUrl);

        floor.setName(floorName);
        floor.setFdcUrl(fdcUrl);
        floor.setCanSold(canSold);
        floor.setAddress(address);

        return floor;
    }

    public Floor getDetailsByUrl(String url) throws IOException {

        String name = null;  // 地块名称
        String fdcUrl = url;  // 地块详情页面政府网URL
        String canSold = null;  // 可售套数
        String address = null;  // 项目地址
        String county = null;  // 所在区县
        String scale = null;  // 项目规模
        String totalPlotsNumber = null;  // 总栋数
        String property = null;  // 物业公司

        Document detailedDoc = Jsoup.connect(url).timeout(5000).get();
        Elements trs = detailedDoc.select(".message_table tr");

        name = trs.eq(1).select("td").eq(1).text();
        address = trs.eq(1).select("td").eq(3).text();
        county = trs.eq(2).select("td").eq(3).text();
        scale = trs.eq(3).select("td").eq(1).text();
        totalPlotsNumber = trs.eq(3).select("td").eq(3).text();
        property = trs.eq(5).select("td").eq(1).text();


        Floor floor = new Floor();
        floor.setName(name);
        floor.setFdcUrl(fdcUrl);
        floor.setCanSold(canSold);
        floor.setAddress(address);
        floor.setCounty(county);
        floor.setScale(scale);
        floor.setTotalPlotsNumber(totalPlotsNumber);
        floor.setProperty(property);

        return floor;
    }
}
