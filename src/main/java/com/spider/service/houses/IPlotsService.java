package com.spider.service.houses;

import com.spider.entity.Floor;
import com.spider.entity.Plots;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangyan on 17/8/6.
 * 单元楼业务接口
 */
public interface IPlotsService {



    public List<Plots> getListByPage(String url, int number) throws IOException;

    public Plots getDetailsByElement(Element tr) throws IOException;

    public Plots getDetailsByUrl(String url) throws IOException;

}
