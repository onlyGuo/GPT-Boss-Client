package com.guoshengkai.gpt.cn.conf;

import com.guoshengkai.gpt.cn.core.Global;
import jakarta.annotation.Resource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class MybatisSessionFactoryConfig {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    public void resetDatasource(DataSource dataSource) throws Exception{
        Environment environment = sqlSessionFactory.getConfiguration().getEnvironment();
        sqlSessionFactory.getConfiguration().setEnvironment(new Environment(environment.getId(), environment.getTransactionFactory(), dataSource));
    }
}
