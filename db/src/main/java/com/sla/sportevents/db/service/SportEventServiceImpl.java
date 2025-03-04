package com.sla.sportevents.db.service;

import com.sla.sportevents.db.common.Cached;
import com.sla.sportevents.db.common.exceptions.BadRequestException;
import com.sla.sportevents.db.common.exceptions.ItemNotFoundException;
import com.sla.sportevents.db.dto.CriteriaDto;
import com.sla.sportevents.db.dto.FilterCriteriaDto;
import com.sla.sportevents.db.dto.FullSportEventDto;
import com.sla.sportevents.db.dto.SportEventDto;
import com.sla.sportevents.db.entity.SportEvent;
import com.sla.sportevents.db.repository.SportEventRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sla.sportevents.db.common.SportEventConstants.SearchCriteria.SPORT;

@Service
public class SportEventServiceImpl implements SportEventService {

    private final SportEventRepository sportEventRepository;

    private final ConversionService conversionService;

    public SportEventServiceImpl(SportEventRepository sportEventRepository,
                                 ConversionService conversionService) {
        this.sportEventRepository = sportEventRepository;
        this.conversionService = conversionService;
    }

    @Override
    @Transactional
    public FullSportEventDto create(FullSportEventDto sportEventDto) {
        if (sportEventDto.getId() != null) {
            throw new BadRequestException("Sport Event Id is not null. Please use the Update endpoint instead.");
        }
        SportEvent sportEvent = conversionService.convert(sportEventDto, SportEvent.class);
        SportEvent savedSportEvent = sportEventRepository.save(sportEvent);

        return conversionService.convert(savedSportEvent, FullSportEventDto.class);
    }

    @Override
    @Transactional
    @Cached(key = "events_", evict = true)
    public FullSportEventDto update(FullSportEventDto sportEventDto) {
        if (sportEventDto.getId() == null) {
            throw new BadRequestException("Sport Event Id is null. Please use the Create endpoint instead.");
        }

        SportEvent existingSportEvent = sportEventRepository.findById(sportEventDto.getId()).orElse(null);
        if (existingSportEvent == null) {
            throw new ItemNotFoundException("Item with id %s is not found".formatted(sportEventDto.getId()));
        }

        SportEvent sportEvent = conversionService.convert(sportEventDto, SportEvent.class);
        SportEvent savedSportEvent = sportEventRepository.save(sportEvent);

        return conversionService.convert(savedSportEvent, FullSportEventDto.class);
    }

    @Override
    @Cached(key = "events_", evict = true)
    public FullSportEventDto delete(Long id) {
        SportEvent event = sportEventRepository.findById(id).orElse(null);
        if (event != null) {
            sportEventRepository.deleteById(id);
        } else {
            throw new ItemNotFoundException("Item with id %s is not found".formatted(id));
        }

        return conversionService.convert(event, FullSportEventDto.class);
    }

    @Override
    @Cached(key = "events_")
    public FullSportEventDto read(Long id) {
        SportEvent sportEvent = sportEventRepository.findById(id).orElse(null);
        if (sportEvent == null) {
            throw new ItemNotFoundException("Item with id %s is not found".formatted(id));
        }
        return conversionService.convert(sportEvent, FullSportEventDto.class);
    }

    @Override
    public List<SportEventDto> findNonSettledByCriteria(FilterCriteriaDto filterCriteriaDto) {
        String sport = getCriteriaValue(filterCriteriaDto.getFilterCriteria(), SPORT);

        return sportEventRepository.findNonSettledByCriteria(sport).stream()
                .map(sportEvent -> conversionService.convert(sportEvent, SportEventDto.class))
                .toList();
    }

    private String getCriteriaValue(List<CriteriaDto> criteria, String field) {
        return criteria.stream().filter(criteriaDto -> field.equals(criteriaDto.getField()))
                .findFirst().orElse(CriteriaDto.builder().build()).getValue();
    }
}
