package com.spider.service.impl.houses;

import com.spider.entity.Floor;
import com.spider.entity.Plots;
import com.spider.service.houses.IPlotsService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PlotsServiceImpl implements IPlotsService {




    public List<Plots> getListByPage(String url, int number) throws IOException {


        List<Plots> plotsList = new ArrayList<Plots>();

        url = url.replace("show", "show_"+number);


        Document pageDoc = Jsoup.connect(url).timeout(5000).get();
        Elements trs = pageDoc.select(".project_table tr");


        // 因为抓取到的数据不规范，所以要自己组织为规范的数据格式
        for (Element tr : trs) {
            // 只获取有效数据的值
            if (tr.select("td").size() > 1) {

                Plots plots = new Plots();

                try {
                    plots = getDetailsByElement(tr);
                    plotsList.add(plots);
                } catch (IOException e) {
                    Elements tds = tr.select("td");
                    String plotsName = tds.eq(1).attr("title");  // 单元楼名称
                    String fdcUrl = "http://www.jnfdc.gov.cn/onsaling/" + tds.eq(1).select("a").attr("href");  // 单元楼页面政府网URL

                    System.out.println("获取单元楼["+plotsName+"]["+fdcUrl+"]数据出错");
                }
            }
        }


        return plotsList;
    }

    public Plots getDetailsByElement(Element tr) throws IOException {
        Elements tds = tr.select("td");
        String name = tds.eq(1).attr("title");  // 单元楼名称
        String fdcUrl = "http://www.jnfdc.gov.cn/onsaling/" + tds.eq(1).select("a").attr("href");  // 单元楼页面政府网URL
        String salePermit = tds.eq(2).attr("title");  // 商品房预售许可证

        Plots plots = getDetailsByUrl(fdcUrl);

        plots.setName(name);
        plots.setFdcUrl(fdcUrl);
        plots.setSalePermit(salePermit);

        return plots;
    }


    public Plots getDetailsByUrl(String url) throws IOException {

        // 根据url继续下潜抓取详细信息
        Document detailedDoc = Jsoup.connect(url).timeout(5000).get();
        Elements trs = detailedDoc.select(".message_table tr");

        String name = trs.eq(1).select("td").eq(1).text();  // 单元楼名称
        String fdcUrl = url;  // 单元楼页面政府网URL
        String area = trs.eq(4).select("td").eq(3).text()+"(万m²)";  // 建筑面积
        String decoration = trs.eq(5).select("td").eq(3).text();  // 装修标准
        String use = trs.eq(6).select("td").eq(1).text();  // 规划用途
        String mortgage = trs.eq(6).select("td").eq(3).text();  // 有无抵押
        String salePermit = trs.eq(7).select("td").eq(1).text();  // 商品房预售许可证
        String landUseCertificate = trs.eq(7).select("td").eq(3).text();  // 国有土地使用证
        String planningPermit = trs.eq(8).select("td").eq(1).text();  // 建设工程规划许可证
        String constructionPermit = trs.eq(8).select("td").eq(3).text();  // 建设工程施工许可证


        Plots plots = new Plots();
        plots.setName(name);
        plots.setFdcUrl(fdcUrl);
        plots.setArea(area);
        plots.setDecoration(decoration);
        plots.setUse(use);
        plots.setMortgage(mortgage);
        plots.setSalePermit(salePermit);
        plots.setLandUseCertificate(landUseCertificate);
        plots.setPlanningPermit(planningPermit);
        plots.setConstructionPermit(constructionPermit);

        return plots;
    }



}
