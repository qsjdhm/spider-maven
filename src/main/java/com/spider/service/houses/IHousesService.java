package com.spider.service.houses;

import com.spider.entity.Houses;
import com.spider.entity.Reb;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangyan on 17/8/2.
 * 楼盘业务接口
 */
public interface IHousesService {

    /**
     * 返回当前页的楼盘数据
     * @param number 页数
     */
    public List<Houses> getListByPage(int number) throws IOException;

//    public List<Houses> getListByUrl(String url) throws IOException;
//
    public Houses getDetailsByElement(Element li) throws IOException;

    public Houses getDetailsByUrl(String url) throws IOException;
}
