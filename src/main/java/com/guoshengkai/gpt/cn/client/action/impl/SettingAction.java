package com.guoshengkai.gpt.cn.client.action.impl;

import com.alibaba.fastjson.JSONArray;
import com.guoshengkai.gpt.cn.client.action.SocketController;
import com.guoshengkai.gpt.cn.client.action.SocketMapping;
import com.guoshengkai.gpt.cn.conf.MybatisSessionFactoryConfig;
import com.guoshengkai.gpt.cn.core.Global;
import com.guoshengkai.gpt.cn.core.util.FileUtil;
import com.guoshengkai.gpt.cn.eneity.vo.WebInfo;
import com.guoshengkai.gpt.cn.eneity.vo.AppFun;
import com.guoshengkai.gpt.cn.eneity.vo.Model;
import com.guoshengkai.gpt.cn.util.HTTP;
import com.guoshengkai.gpt.cn.util.PropsUtil;
import com.guoshengkai.gpt.cn.util.ServerUtil;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SocketController
public class SettingAction {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private MybatisSessionFactoryConfig mybatisSessionFactoryConfig;

    @SocketMapping("setting/site")
    public void setSiteSetting(WebInfo webInfo) {
        Global.put("webInfo", webInfo);
        if (StringUtils.hasText(webInfo.getLogo())){
            byte[] bytes = ServerUtil.getByte("api/v1/file/display/" + webInfo.getLogo(), null);
            FileUtil.writeFile(bytes, new File("data/files/" + webInfo.getLogo()));
        }
    }

    @SneakyThrows
    @SocketMapping("setting/props")
    public void setProps(Map<String, String> props) {
        props.forEach((k, v) -> {
            PropsUtil.set(k, v);
        });
        jdbcTemplate.setDataSource(PropsUtil.getDataSource());
        mybatisSessionFactoryConfig.resetDatasource(PropsUtil.getDataSource());
    }

    @SocketMapping("setting/models")
    public void setModels(JSONArray models){
        Global.models = models.toJavaList(Model.class);
    }

    @SocketMapping("setting/fun")
    public void setFun(AppFun fun){
        Global.fun = fun;
    }

}
