package com.sla.sportevents.db.service;

import com.sla.sportevents.db.dto.FilterCriteriaDto;
import com.sla.sportevents.db.dto.FullSportEventDto;
import com.sla.sportevents.db.dto.SportEventDto;

import java.util.List;

public interface SportEventService {

    FullSportEventDto create(FullSportEventDto sportEventDto);
    FullSportEventDto update(FullSportEventDto sportEventDto);
    FullSportEventDto delete(Long id);
    FullSportEventDto read(Long id);
    List<SportEventDto> findNonSettledByCriteria(FilterCriteriaDto filterCriteriaDto);
}
