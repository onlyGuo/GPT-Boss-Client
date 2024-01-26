package com.guoshengkai.gpt.cn.eneity.vo;

import com.guoshengkai.gpt.cn.eneity.vo.AppFun;
import com.guoshengkai.gpt.cn.eneity.vo.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WebInfo {

    private String name;

    private String logo;

    private String ip;

    private String icp;

    private int status;

    private List<Model> modules;

    private AppFun fun;
}
