package com.spider.service.impl.system;


import com.spider.dao.FloorMapper;
import com.spider.dao.HousesMapper;
import com.spider.dao.PlotsMapper;
import com.spider.dao.RebMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

/**
 * Created by zhangyan on 2017/8/30.
 * 数据库业务
 */
public class SqlServiceImpl {

    static SqlSession sqlSession = null;

    static RebMapper rebMapper = null;
    static HousesMapper housesMapper = null;
    static FloorMapper floorMapper = null;
    static PlotsMapper plotsMapper = null;


    public SqlServiceImpl() {
        // 打开数据库
        SqlSessionFactory sessionFactory = null;
        try {
            sessionFactory = new SqlSessionFactoryBuilder()
                    .build(Resources.getResourceAsReader("mybatis-config.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sqlSession = sessionFactory.openSession();

        // 实例化映射关系
        rebMapper = sqlSession.getMapper(RebMapper.class);
        housesMapper = sqlSession.getMapper(HousesMapper.class);
        floorMapper = sqlSession.getMapper(FloorMapper.class);
        plotsMapper = sqlSession.getMapper(PlotsMapper.class);
    }


    public static RebMapper rebSql() {
        return rebMapper;
    }

    public static HousesMapper housesSql() {
        return housesMapper;
    }

    public static FloorMapper floorSql() {
        return floorMapper;
    }

    public static PlotsMapper plotsSql() {
        return plotsMapper;
    }

    public static void comment() {
        sqlSession.commit();
    }



}
