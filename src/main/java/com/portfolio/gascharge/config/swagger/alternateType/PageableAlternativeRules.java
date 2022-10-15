package com.portfolio.gascharge.config.swagger.alternateType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class PageableAlternativeRules {

    @ApiModelProperty(value = "페이지 번호", example = "0")
    private Integer page;

    @ApiModelProperty(value = "페이지 크기", example = "5")
    private Integer size;

    @ApiModelProperty(value = "정렬(컬럼명,ASC|DESC)", example = "name,DESC")
    private List<String> sort;
}
