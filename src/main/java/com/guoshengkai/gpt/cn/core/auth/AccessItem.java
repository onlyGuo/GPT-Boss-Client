package com.guoshengkai.gpt.cn.core.auth;

/**
 * Copyright (c) 2021 HEBEI CLOUD IOT FACTORY BIGDATA CO.,LTD.
 * Legal liability shall be investigated for unauthorized use
 *
 * 权限枚举
 * @Author: Guo Shengkai
 * @Date: Create in 2021/04/08 9:05
 */
public enum AccessItem {

    /**
     * 待办工作
     */
    TROUBLE_INVE(
            "TROUBLE_INVE",
            "隐患排查",
            "WAITING",
            "代办工作", new AccessAction[]{AccessAction.NONE}),
    TASK_CHECK(
            "TASK_CHECK",
            "检查任务",
            "WAITING",
            "代办工作", new AccessAction[]{AccessAction.NONE}),
    LATE_APPLI(
            "LATE_APPLI",
            "逾期申请",
            "WAITING",
            "代办工作", new AccessAction[]{AccessAction.NONE}),
    PHOTO_EXAM(
            "PHOTO_EXAM",
            "随手拍审核",
            "WAITING",
            "代办工作", new AccessAction[]{AccessAction.NONE}),

    /**
     * 风险分级管控
     */
    TROUBLE_EVAL_DRIVER(
            "TROUBLE_EVAL_DRIVER",
            "风险辨识与评价-设备设施单元",
            "TROUBLE_EVAL",
            "风险分级管控",
            new AccessAction[]{AccessAction.ADD, AccessAction.SEE, AccessAction.EDIT, AccessAction.DEL}),
    TROUBLE_EVAL_ACTION(
            "TROUBLE_EVAL_ACTION",
            "风险辨识与评价-作业活动单元",
            "TROUBLE_EVAL",
            "风险分级管控",
            new AccessAction[]{AccessAction.ADD, AccessAction.SEE, AccessAction.EDIT, AccessAction.DEL}),
    TROUBLE_EVAL_ENV(
            "TROUBLE_EVAL_ENV",
            "风险辨识与评价-作业环境单元",
            "TROUBLE_EVAL",
            "风险分级管控",
            new AccessAction[]{AccessAction.ADD, AccessAction.SEE, AccessAction.EDIT, AccessAction.DEL}),
    WORK_AREA_MGR(
            "WORK_AREA_MGR",
            "作业场所管理",
            "TROUBLE_EVAL",
            "风险分级管控",
            new AccessAction[]{AccessAction.ADD, AccessAction.DRAW}),
    TROUBLE_IMPORT(
            "TROUBLE_IMPORT",
            "风控信息导入",
            "TROUBLE_EVAL",
            "风险分级管控",
            new AccessAction[]{AccessAction.IMPORT, AccessAction.EXPORT, AccessAction.DEL}),

    /**
     * 隐患排查治理
     */
    TROUBLE(
            "TROUBLE",
            "隐患管理",
            "TROUBLE_MGR",
            "隐患排查治理",
            new AccessAction[]{AccessAction.ADD, AccessAction.SEE, AccessAction.EDIT, AccessAction.SUPER_EXEC}),
    CHECK_TASK(
            "CHECK_TASK",
            "检查计划管理",
            "TROUBLE_MGR",
            "隐患排查治理",
            new AccessAction[]{AccessAction.ADD, AccessAction.SEE, AccessAction.EDIT, AccessAction.SWITCH}),
    CHECK_POINT(
            "CHECK_POINT",
            "检查点管理",
            "TROUBLE_MGR",
            "隐患排查治理",
            new AccessAction[]{AccessAction.ADD, AccessAction.SEE, AccessAction.EDIT, AccessAction.QR_CODE}),
    PHOTO(
            "PHOTO",
            "随手拍管理",
            "TROUBLE_MGR",
            "隐患排查治理",
            new AccessAction[]{AccessAction.SEE, AccessAction.EXEC, AccessAction.DEL}),
    RECORDTYPE(
            "RECORDTYPE",
            "档案分类管理",
            "TROUBLE_MGR",
            "档案分类治理",
            new AccessAction[]{AccessAction.ADD, AccessAction.SEE, AccessAction.EDIT, AccessAction.DEL}),
    RECORD(
            "RECORD",
            "档案管理",
            "TROUBLE_MGR",
            "档案治理",
            new AccessAction[]{AccessAction.ADD, AccessAction.SEE, AccessAction.EDIT, AccessAction.DEL}),

    /**
     * 报表与台账
     */
    TROUBLE_ORDER(
            "TROUBLE_ORDER",
            "隐患台账",
            "TABLE",
            "报表与台账",
            new AccessAction[]{AccessAction.EXPORT, AccessAction.SEE}),
    SCL_EVAL_LOG(
            "SCL_EVAL_LOG",
            "SCL安全检查评价记录",
            "TABLE",
            "报表与台账",
            new AccessAction[]{AccessAction.SEE, AccessAction.EXEC, AccessAction.DEL}),
    JHA_EVAL_LOG(
            "JHA_EVAL_LOG",
            "JHA作业危害分析评价记录",
            "TABLE",
            "报表与台账",
            new AccessAction[]{AccessAction.SEE, AccessAction.EXEC, AccessAction.DEL}),

    /**
     * 基础数据配置
     */
    COMPANY_BASE(
            "COMPANY_BASE",
            "企业基础信息管理",
            "BASE",
            "基础数据配置",
            new AccessAction[]{AccessAction.NONE}),
    STRUCTURE(
            "STRUCTURE_BASE",
            "组织架构管理",
            "BASE",
            "基础数据配置",
            new AccessAction[]{AccessAction.NONE}),
    USER(
            "USER",
            "用户管理",
            "BASE",
            "基础数据配置",
            new AccessAction[]{AccessAction.ADD, AccessAction.SEE, AccessAction.EDIT,
                    AccessAction.DEL, AccessAction.FROZEN, AccessAction.RESET}),
    ROLE(
            "ROLE",
            "角色管理",
            "BASE",
            "基础数据配置",
            new AccessAction[]{AccessAction.ADD, AccessAction.SEE, AccessAction.EDIT, AccessAction.DEL }),


    /**
     * 平台级别的操作，所有企业用户均没有这个权限
     */
    PLAN(
            "PLAN",
            "平台级别的操作",
            "PLAN",
            "平台级别的操作",
            new AccessAction[]{AccessAction.NONE});

    private String value;
    private String name;
    private String model;
    private String modelName;
    private AccessAction[] actions;

    AccessItem(String value, String name, String model, String modelName,
               AccessAction[] actions){
        this.value = value;
        this.name = name;
        this.model = model;
        this.modelName = modelName;
        this.actions = actions;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public AccessAction[] getActions() {
        return actions;
    }

    public void setActions(AccessAction[] actions) {
        this.actions = actions;
    }
}
