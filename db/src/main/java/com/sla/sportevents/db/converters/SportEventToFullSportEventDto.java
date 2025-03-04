package com.sla.sportevents.db.converters;

import com.sla.sportevents.db.dto.FullSportEventDto;
import com.sla.sportevents.db.dto.MarketDto;
import com.sla.sportevents.db.dto.SportEventDto;
import com.sla.sportevents.db.entity.SportEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SportEventToFullSportEventDto implements Converter<SportEvent, FullSportEventDto> {

    private final ConversionService conversionService;

    @Lazy
    public SportEventToFullSportEventDto(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public FullSportEventDto convert(SportEvent source) {

        List<MarketDto> marketDtos = source.getMarkets()
                .stream().map(market -> conversionService.convert(market, MarketDto.class)).toList();

        return FullSportEventDto.builder()
                .id(source.getId())
                .description(source.getDescription())
                .homeTeam(source.getHomeTeam())
                .awayTeam(source.getAwayTeam())
                .startTime(source.getStartTime())
                .sport(source.getSport())
                .country(source.getCountry())
                .competition(source.getCompetition())
                .settled(source.getSettled())
                .markets(marketDtos)
                .build();
    }
}
