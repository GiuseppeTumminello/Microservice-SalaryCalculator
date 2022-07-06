package com.acoustic.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ResponseMicroserviceModel {

    private String description;
    private BigDecimal value;
}
