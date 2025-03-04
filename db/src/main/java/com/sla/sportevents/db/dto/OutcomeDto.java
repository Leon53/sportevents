package com.sla.sportevents.db.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sla.sportevents.db.common.OutcomeResult;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutcomeDto {

    private Long id;                    // id (auto-generated)

    private String description;         // (e.g.: “Arsenal to win”)

    private Boolean settled;            // Boolean

    private BigDecimal price;           // (e.g.: 2.0)

    private OutcomeResult result;       //  (null/win/lose)
}
