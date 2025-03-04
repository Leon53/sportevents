package com.sla.sportevents.db.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sla.sportevents.db.common.MarketStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketDto {

    private Long id;                    // id (auto-generated)

    private String description;         // (e.g.: “Match Betting”)

    private MarketStatus status;        // (OPEN/CLOSE)

    private Boolean settled;            // Boolean

    private List<OutcomeDto> outcomes;       // outcomes

}
