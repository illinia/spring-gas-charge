package com.portfolio.gascharge.config.swagger.alternateType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class CustomPageable {

    @ApiModelProperty(value = "페이지 번호")
    private Integer page;

    @ApiModelProperty(value = "페이지 크기")
    private Integer size;

    @ApiModelProperty(value = "정렬(컬럼명,ASC|DESC)")
    private List<String> sort;
}
