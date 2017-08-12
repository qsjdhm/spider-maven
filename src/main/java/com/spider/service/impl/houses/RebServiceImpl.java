package com.spider.service.impl.houses;

import com.spider.entity.Reb;
import com.spider.utils.Constant;
import com.spider.service.houses.IRebService;
import com.spider.utils.LogFile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * Created by zhangyan on 2017/7/20.
 * 处理房产商业务功能
 */
public class RebServiceImpl implements IRebService {

    /**
     * 从政府网获取第number页房产商数据
     */
    @Override
    public List<Reb> getListByPage(int number) throws IOException {
        List<Reb> rebList = new ArrayList<Reb>();  // 承载房产商数据集合
        String url = "http://www.jnfdc.gov.cn/kfqy/index_"+number+".shtml";

        Document pageDoc = Jsoup.connect(url).timeout(5000).get();  // 承载抓取到的每页房产商DOM数据
        Elements trs = pageDoc.select(".project_table tr");

        // 因为抓取到的数据不规范，所以要自己组织为规范的数据格式
        for (Element tr : trs) {
            // 只获取有效数据的值
            if (tr.select("td").size() > 1) {
                // 抓取详细信息
                rebList.add(getDetailsByElement(tr));
            }
        }

        return rebList;
    }




    /**
     * 根据从政府网抓取的每一条房产商数据下潜获取房产商的详情数据
     * @param tr 抓取的每一条房产商DOM数据
     */
    @Override
    public Reb getDetailsByElement(Element tr)  {

        Elements tds = tr.select("td");
        String fdcUrl = "http://www.jnfdc.gov.cn/kfqy/" + tds.eq(1).select("a").attr("href");
        String name = tds.eq(1).select("a").text();
        String qualificationLevel = tds.eq(2).text();
        String qualificationId = tds.eq(3).text();
        String LegalPerson = tds.eq(4).text();

        Reb reb = new Reb();

        try {
            reb = getDetailsByUrl(fdcUrl);
        } catch (IOException e) {

        }

        // 如果从列表抓到的名称没有...，就用列表的名称
        if (name.indexOf("...") == -1) {
            reb.setName(name);
        }
        reb.setQualificationLevel(qualificationLevel);
        reb.setQualificationId(qualificationId);
        reb.setLegalPerson(LegalPerson);

        return reb;
    }


    /**
     * 根据某一个房产商详细页面的url获取这一个房产商的详细数据
     */
    @Override
    public Reb getDetailsByUrl(String url) throws IOException {

        Document detailedDoc = Jsoup.connect(url).timeout(5000).get();  // 承载抓取到的房产商详细数据
        Elements trs = detailedDoc.select(".message_table tr");

        String name = trs.eq(0).select("td").eq(1).text();
        String qualificationLevel = trs.eq(2).select("td").eq(1).text();
        String qualificationId = null;
        String LegalPerson = trs.eq(5).select("td").eq(3).text();
        String address = trs.eq(1).select("td").eq(1).text();
        String phone = trs.eq(3).select("td").eq(3).text();
        String mail = trs.eq(4).select("td").eq(3).text();  // 企业邮箱
        String registeredCapital = trs.eq(5).select("td").eq(1).text();  // 注册资金
        String type = trs.eq(6).select("td").eq(1).text();  // 企业类型
        String introduction = trs.eq(7).select("td").eq(1).text();  // 企业简介

        Reb reb = new Reb();
        reb.setName(name);
        reb.setFdcUrl(url);
        reb.setQualificationLevel(qualificationLevel);
        reb.setQualificationId(qualificationId);
        reb.setLegalPerson(LegalPerson);
        reb.setAddress(address);
        reb.setPhone(phone);
        reb.setMail(mail);
        reb.setRegisteredCapital(registeredCapital);
        reb.setType(type);
        reb.setIntroduction(introduction);

        return reb;
    }

}
