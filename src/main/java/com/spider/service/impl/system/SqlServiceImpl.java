package com.spider.service.impl.system;


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

    static RebMapper rebMapper = null;
    static SqlSession sqlSession = null;

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
    }

    public static void initMapper() {

    }

    public static RebMapper rebSql() {
        return rebMapper;
    }

    public static void comment() {
        sqlSession.commit();
    }



}
