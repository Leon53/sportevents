package com.sla.sportevents.db.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CriteriaDto {
    String field;
    String value;
}
