package com.spider.service.impl.houses;

import com.spider.entity.Floor;
import com.spider.entity.Houses;
import com.spider.entity.Plots;
import com.spider.service.houses.IFloorService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FloorServiceImpl implements IFloorService {

    PlotsServiceImpl plotsService = new PlotsServiceImpl();


    // 根据楼盘名称获取地块列表
    public List<Floor> getListByHousesName(String fdcName) {
        // 楼盘下的地块列表
        List<Floor> allFloorList = new ArrayList<Floor>();

        int number = 1;
        boolean isError = false;

        do {

            List<Floor> pageFloorList = new ArrayList<Floor>();
            try {
                pageFloorList = getListByPage(fdcName, number);

                System.out.println(fdcName+"的地块列表----------");
                for(Floor floor : pageFloorList) {
                    System.out.println(floor.getName());
                }
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    // 判断e是超时异常，把isError设为true，表示这页数据为0是由超时异常导致的
                    isError = true;
                }
                System.out.println("获取"+fdcName+"的地块列表第"+number+"页失败："+e);
                e.printStackTrace();
            }

            // 如果有3页数据，获取第1页数据超时异常数据为0，就可以正常的接着获取第2页
            // 如果只有1页数据，并且是超时异常，接着获取第2页，如果第2页还是没有数据并且超时异常
            // 接着获取第3页，如果获取到数据为0并且没有异常就会跳出dowhile循环


            // 如果获取的这页数据为空，并且不是经过了异常，就表示全部数据获取完成或此楼盘没有地块列表
            if (pageFloorList.size()==0 && !isError) {
                number = 0;
            } else {
                // 页数累加获取下页地块列表
                number++;
                // 把每页的地块数据添加到全部的地块列表中
                for(Floor floor : pageFloorList) {
                    allFloorList.add(floor);
                }
            }
        } while (number > 0);

        return allFloorList;
    }



    public List<Floor> getListByPage(String fdcName, int number) throws IOException {

        List<Floor> floorList = new ArrayList<Floor>();

        String url = "http://www.jnfdc.gov.cn/onsaling/index_"+number+".shtml?zn=all&pu=all&pn="+fdcName+"&en=";

        Document pageDoc = Jsoup.connect(url).timeout(5000).get();
        Elements trs = pageDoc.select(".project_table tr");

        // 因为抓取到的数据不规范，所以要自己组织为规范的数据格式
        for (Element tr : trs) {
            // 只获取有效数据的值
            if (tr.select("td").size() > 1) {

                Floor floor = new Floor();

                try {

                    floor = getDetailsByElement(tr);
                    floor.setpHousesName(fdcName);
                    List<Plots> floorPlotsList = plotsService.getListByUrl(floor.getFdcUrl());

                    System.out.println(floor.getName()+"的单元楼-------------");
                    for (Plots floorPlots : floorPlotsList) {
                        floorPlots.setpFloorName(floor.getName());
                        System.out.println(floorPlots.getName());
                    }

                    floor.setPlotsList(plotsService.getListByUrl(floor.getFdcUrl()));

                    floorList.add(floor);
                } catch (IOException e) {
                    Elements tds = tr.select("td");
                    String floorName = tds.eq(1).attr("title");  // 地块名称
                    String fdcUrl = "http://www.jnfdc.gov.cn" + tds.eq(1).select("a").attr("href");  // 地块详情页面政府网URL

                    System.out.println("获取地块["+floorName+"]["+fdcUrl+"]数据出错");
                }
            }
        }

        return floorList;
    }

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

        Document detailedDoc = Jsoup.connect(url).timeout(5000).get();
        Elements trs = detailedDoc.select(".message_table tr");

        String name = trs.eq(1).select("td").eq(1).text();
        String fdcUrl = url;  // 地块详情页面政府网URL
        String canSold = null;  // 可售套数
        String address = trs.eq(1).select("td").eq(3).text();
        String county = trs.eq(2).select("td").eq(3).text();
        String scale = trs.eq(3).select("td").eq(1).text();
        String totalPlotsNumber = trs.eq(3).select("td").eq(3).text();
        String property = trs.eq(5).select("td").eq(1).text();


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
