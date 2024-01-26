package com.guoshengkai.gpt.cn.api;

import com.guoshengkai.gpt.cn.client.BossSocketClient;
import com.guoshengkai.gpt.cn.conf.NoInit;
import com.guoshengkai.gpt.cn.conf.NoLogin;
import com.guoshengkai.gpt.cn.core.CommAttr;
import com.guoshengkai.gpt.cn.core.Global;
import com.guoshengkai.gpt.cn.core.SpringBootApplicationUtil;
import com.guoshengkai.gpt.cn.core.dao.impl.BaseDaoImpl;
import com.guoshengkai.gpt.cn.eneity.vo.WebInfo;
import com.guoshengkai.gpt.cn.exception.ValidationException;
import com.guoshengkai.gpt.cn.util.CodeUtil;
import com.guoshengkai.gpt.cn.util.PropsUtil;
import com.guoshengkai.gpt.cn.util.ThreadUtil;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/system")
public class SystemSettingController {


    @Resource
    private BossSocketClient bossSocketClient;

    @GetMapping("web-info")
    @NoLogin
    @NoInit
    public WebInfo getWebInfo(){
        WebInfo webInfo = Global.get("webInfo", WebInfo.class);
        if (null != webInfo){
            webInfo.setModules(Global.models == null ? new ArrayList<>() : Global.models);
            webInfo.setFun(Global.fun);
        }
        return webInfo;
    }

    @GetMapping("manager-key")
    @NoLogin
    @NoInit
    public String getManagerKey(){
        String s = PropsUtil.get("System.inited");
        if (s.equals("TRUE")){
            throw new ValidationException("已经初始化过了, 为了安全起见，不允许再次获取ManagerKey");
        }
        return PropsUtil.get("MANAGER_KEY");
    }

    @PostMapping("init-db")
    @NoLogin
    @NoInit
    public void initDb(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()){
            writer.write("开始检查数据库配置...\n");
            writer.flush();
            ThreadUtil.sleep(200);

            String host = PropsUtil.get("EVN.MYSQL.HOST");
            writer.write("\nMYSQL.HOST：" + host + "\n");
            if (StringUtils.isEmpty(host)){
                writer.write("校验未通过，请先配置数据库，详细请查看文档：https://boss.icoding.ink/doc/db\n");
                writer.flush();
                return;
            }
            writer.flush();
            ThreadUtil.sleep(50);

            String name = PropsUtil.get("EVN.MYSQL.DB_NAME");
            writer.write("\nMYSQL.DB_NAME：" + name + "\n");
            if (StringUtils.isEmpty(name)){
                writer.write("校验未通过，请先配置数据库，详细请查看文档：https://boss.icoding.ink/doc/db\n");
                writer.flush();
                return;
            }
            writer.flush();
            ThreadUtil.sleep(50);

            String username = PropsUtil.get("EVN.MYSQL.USERNAME");
            writer.write("\nMYSQL.USERNAME：" + username + "\n");
            if (StringUtils.isEmpty(username)){
                writer.write("校验未通过，请先配置数据库，详细请查看文档：https://boss.icoding.ink/doc/db\n");
                writer.flush();
                return;
            }
            writer.flush();
            ThreadUtil.sleep(50);

            String password = PropsUtil.get("EVN.MYSQL.PASSWORD");
            writer.write("\nMYSQL.PASSWORD：" + CodeUtil.desensitization(password) + "\n");
            if (StringUtils.isEmpty(password)){
                writer.write("校验未通过，请先配置数据库，详细请查看文档：https://boss.icoding.ink/doc/db\n");
                writer.flush();
                return;
            }
            writer.flush();
            ThreadUtil.sleep(50);

            // 检查数据库连接是否正常
            writer.write("\n开始检查数据库连接...\n");
            writer.flush();
            ThreadUtil.sleep(250);
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setJdbcUrl(String.format("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=" +
                    "utf-8&useSSL=false&allowPublicKeyRetrieval=true", host, name));
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            try {
                dataSource.getConnection();
            } catch (SQLException e) {
                writer.write("MYSQL:ERROR：" + e.getErrorCode() + ":" + e.getSQLState() + " => " + e.getMessage() + "\n");
                writer.write("数据库连接失败，请检查配置是否正确，详细请查看文档：https://boss.icoding.ink/doc/db\n");
                writer.flush();
                return;
            } finally {
                if (!dataSource.isClosed()){
                    dataSource.close();
                }
            }
            writer.write("数据库连接成功, 准备执行初始化脚本...\n");
            writer.flush();
            ThreadUtil.sleep(200);
            List<BaseDaoImpl> daos = SpringBootApplicationUtil.getBeanListBySuper(BaseDaoImpl.class, BaseDaoImpl.class);
            for (BaseDaoImpl dao : daos) {
                writer.write("\n检查数据库表：" + dao.tableName() + "\n");
                writer.flush();
                dao.initTable((log) -> {
                    writer.write("SQL => " + log + "\n\n");
                    writer.flush();
                });
                ThreadUtil.sleep(200);
            }

            writer.write("数据库初始化完毕！点击”完成“按钮开始体验吧！\n");
            writer.flush();
            getWebInfo().setStatus(CommAttr.APP_STATUS.INLINE);
            PropsUtil.set("System.inited", "TRUE");
            bossSocketClient.sendTo("setting/inited");
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
