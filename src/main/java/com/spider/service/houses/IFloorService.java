package com.spider.service.houses;

import com.spider.entity.Floor;
import com.spider.entity.Houses;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangyan on 17/8/2.
 * 地块业务接口
 */
public interface IFloorService {


    public List<Floor> getListByHousesName(String fdcName);

    public List<Floor> getListByPage(String fdcName, int number) throws IOException;

    public Floor getDetailsByElement(Element tr) throws IOException;

    public Floor getDetailsByUrl(String url) throws IOException;

}
