package com.guoshengkai.gpt.cn.eneity;

import com.guoshengkai.gpt.cn.core.annotation.po.FieldName;
import com.guoshengkai.gpt.cn.core.annotation.po.ID;
import com.guoshengkai.gpt.cn.core.annotation.po.TableName;
import com.guoshengkai.gpt.cn.core.beans.PO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@TableName(name = "MODEL_USE_RECORD")
public class ModelUseRecord extends PO {

    @ID
    private long id;

    @FieldName(name = "USER_ID")
    private int userId;

    @FieldName(name = "MODEL_NAME")
    private String modelName;

    @FieldName(name = "PROMPT_TOKEN_COUNT")
    private int promptTokenCount;

    @FieldName(name = "GENERATED_TOKEN_COUNT")
    private int generatedTokenCount;

    @FieldName(name = "PRICE")
    private BigDecimal price;

    @FieldName(name = "USER_DISCOUNT_RATE")
    private BigDecimal userDiscountRate;

    @FieldName(name = "USER_FINAL_PRICE")
    private BigDecimal userFinalPrice;

    @FieldName(name = "CREATE_TIME")
    private Date createTime;

}