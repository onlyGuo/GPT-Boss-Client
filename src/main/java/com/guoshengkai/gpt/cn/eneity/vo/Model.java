package com.guoshengkai.gpt.cn.eneity.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Model {

    private String name;

    private String description;

    private BigDecimal promptPrice;

    private BigDecimal completionPrice;

    private BigDecimal userDiscount = BigDecimal.ONE;
}
