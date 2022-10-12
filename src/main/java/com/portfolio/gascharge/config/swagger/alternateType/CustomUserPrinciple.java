package com.portfolio.gascharge.config.swagger.alternateType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CustomUserPrinciple {

    @ApiModelProperty(value = "유저 id")
    private Long id;
}
