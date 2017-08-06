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



    public List<Plots> getListByUrl(String baseUrl) throws IOException {

        List<Plots> allPlotsList = new ArrayList<Plots>();

        int number = 1;
        boolean isError = false;

        do {

            List<Plots> pagePlotsList = new ArrayList<Plots>();
            try {
                pagePlotsList = getListByPage(baseUrl, number);
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    // 判断e是超时异常，把isError设为true，表示这页数据为0是由超时异常导致的
                    isError = true;
                }

                throw new IOException(e);
            }

            // 如果有3页数据，获取第1页数据超时异常数据为0，就可以正常的接着获取第2页
            // 如果只有1页数据，并且是超时异常，接着获取第2页，如果第2页还是没有数据并且超时异常
            // 接着获取第3页，如果获取到数据为0并且没有异常就会跳出dowhile循环


            // 如果获取的这页数据为空，并且不是经过了异常，就表示全部数据获取完成或此楼盘没有地块列表
            if (pagePlotsList.size()==0 && !isError) {
                number = 0;
            } else {
                // 页数累加获取下页地块列表
                number++;
                // 把每页的地块数据添加到全部的地块列表中
                for(Plots plots : pagePlotsList) {
                    allPlotsList.add(plots);
                }
            }
        } while (number > 0);

        return allPlotsList;
    }



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
