package com.spider.service.houses;

import com.spider.entity.Reb;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by zhangyan on 17/7/16.
 * 房产商业务接口
 */
public interface IRebService {
    /**
     * 从政府网获取所有房产商数据
     * @param number 页数
     */
    public List<Reb> getListByPage(int number) throws IOException;

    public List<Reb> getListByUrl(String url) throws IOException;

    public Reb getDetailsByElement(Element tr) throws IOException;

    public Reb getDetailsByUrl(String url) throws IOException;

}

