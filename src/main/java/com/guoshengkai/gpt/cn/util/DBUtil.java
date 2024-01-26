package com.guoshengkai.gpt.cn.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Copyright (c) 2021 HEBEI CLOUD IOT FACTORY BIGDATA CO.,LTD.
 * Legal liability shall be investigated for unauthorized use
 *
 * @Author: Guo Shengkai
 * @Date: Create in 2022/5/18 11:21
 */
@Repository
public class DBUtil {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryForList(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        return jdbcTemplate.queryForList(sql, args);
    }

    public Map<String, Object> query(String sql) {
        return jdbcTemplate.queryForMap(sql);
    }

    public Map<String, Object> query(String sql, Object... args) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, args);
        if (sqlRowSet.getRow() > 0) {
            throw new RuntimeException("Increase the number of rows, but only one row is expected");
        }
        if (!sqlRowSet.next()) {
            return null;
        }
        SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
        Map<String, Object> map = new HashMap<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            map.put(metaData.getColumnLabel(i), sqlRowSet.getObject(i));
        }
        return map;
    }

    public int execute(String sql) {
        return jdbcTemplate.update(sql);
    }

    public int execute(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }

    //    public void initDb(){
//        jdbcTemplate.setDatabaseProductName("information_schema");
//        if(jdbcTemplate.queryForList("""
//                    SELECT SCHEMA_NAME dbName
//                    FROM information_schema.SCHEMATA
//                    WHERE SCHEMA_NAME = ?""", dbName).isEmpty()){
//            jdbcTemplate.execute("CREATE DATABASE `" + dbName + "`");
//        }
//        jdbcTemplate.setDatabaseProductName(dbName);
////        Connection connection = null;
////        Statement statement = null;
////        try {
////            jdbcTemplate.setDatabaseProductName("information_schema");
////            DriverDataSource dataSource = (DriverDataSource)jdbcTemplate.getDataSource();
////
////            connection = dataSource.getConnection();
////            statement = connection.createStatement();
////            ResultSet resultSet = statement.executeQuery("""
////                    SELECT SCHEMA_NAME dbName
////                    FROM information_schema.SCHEMATA
////                    WHERE SCHEMA_NAME = `""" + dbName + "`"
////            );
////            if(!resultSet.next()){
////                statement.execute("CREATE DATABASE `" + dbName + "`");
////            }
////            resultSet.close();
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }finally {
////            jdbcTemplate.setDatabaseProductName(dbName);
////            if (null != statement){
////                try {
////                    statement.close();
////                } catch (SQLException e) {
////                    throw new RuntimeException(e);
////                }
////            }
////            if (null != connection){
////                try {
////                    connection.close();
////                } catch (SQLException e) {
////                    throw new RuntimeException(e);
////                }
////            }
////        }
//    }

    private String getSchema() {
        return System.getProperty("EVN.MYSQL.DB_NAME");
    }

    public boolean tableExists(String tableName) {
        String sql = "select count(1) from information_schema.tables where table_name = ? AND table_schema = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName, getSchema());
        return count != null && count > 0;
    }

    public boolean columnExists(String tableName, String columnName) {
        String sql = "select count(1) from information_schema.columns where table_name = ? and column_name = ? AND table_schema = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName, columnName, getSchema());
        return count != null && count > 0;
    }

    public boolean columnExists(String tableName, String columnName, String columnType) {
        String sql = "select count(1) from information_schema.columns where table_name = ? and column_name = ? and column_type = ? AND table_schema = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName, columnName, columnType, getSchema());
        return count != null && count > 0;
    }

    public boolean columnExists(String tableName, String columnName, String columnType, String columnComment) {
        String sql = "select count(1) from information_schema.columns where table_name = ? and column_name = ? " +
                "and column_type = ? and column_comment = ? AND table_schema = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName, columnName, columnType, columnComment, getSchema());
        return count != null && count > 0;
    }

    public boolean columnExists(String tableName, String columnName, String columnType, String columnComment, String columnDefault) {
        String sql = "select count(1) from information_schema.columns where table_name = ? and column_name" +
                " = ? and column_type = ? and column_comment = ? and column_default = ? AND table_schema = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName, columnName, columnType,
                columnComment, columnDefault, getSchema());
        return count != null && count > 0;
    }

    public void addColumn(String tableName, String columnName, String columnType) {
        String sql = "alter table " + tableName + " add column " + columnName + " " + columnType;
        jdbcTemplate.execute(sql);
    }

    public void addColumn(String tableName, String columnName, String columnType, String columnComment) {
        String sql = "alter table " + tableName + " add column " + columnName + " " + columnType +
                " comment '" + columnComment + "'";
        jdbcTemplate.execute(sql);
    }

    public void addColumn(String tableName, String columnName, String columnType, String columnComment, String columnDefault) {
        String sql = "alter table " + tableName + " add column " + columnName + " " + columnType +
                " comment '" + columnComment + "' default " + columnDefault;
        jdbcTemplate.execute(sql);
    }

    public void dropColumn(String tableName, String columnName) {
        String sql = "alter table " + tableName + " drop column " + columnName;
        jdbcTemplate.execute(sql);
    }

    public void updateColumn(String tableName, String columnName, String columnType) {
        String sql = "alter table " + tableName + " modify column " + columnName + " " + columnType;
        jdbcTemplate.execute(sql);
    }

    public void updateColumn(String tableName, String columnName, String columnType, String columnComment) {
        String sql = "alter table " + tableName + " modify column " + columnName + " " + columnType +
                " comment '" + columnComment + "'";
        jdbcTemplate.execute(sql);
    }

    public void updateColumn(String tableName, String columnName, String columnType, String columnComment, String columnDefault) {
        String sql = "alter table " + tableName + " modify column " + columnName + " " + columnType +
                " comment '" + columnComment + "' default " + columnDefault;
        jdbcTemplate.execute(sql);
    }

    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public DataSource getDataSource() {
        return jdbcTemplate.getDataSource();
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void dropTable(String tableName) {
        String sql = "drop table if exists `" + tableName + "`";
        jdbcTemplate.execute(sql);
    }

    public long insert(String sql, Object[] values) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            int i = 0;
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (Object value : values) {
                ps.setObject(++i, value);
            }
            return ps;
        }, holder);
        return Objects.requireNonNull(holder.getKey()).longValue();
    }

    public List<Map<String, Object>> list(String sql, List<Object> params) {
        if (params == null || params.isEmpty()) {
            return jdbcTemplate.queryForList(sql);
        }
        return jdbcTemplate.queryForList(sql, params);
    }

    public long selectNumber(String sql, List<Object> params) {
        if (params == null || params.isEmpty()) {
            Long aLong = jdbcTemplate.queryForObject(sql, Long.class);
            return aLong == null ? 0 : aLong;
        }
        Long aLong = jdbcTemplate.queryForObject(sql, Long.class, params);
        return aLong == null ? 0 : aLong;
    }
}