package com.sla.sportevents.db.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FilterCriteriaDto {

    private List<CriteriaDto> filterCriteria;

}
