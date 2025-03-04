package com.sla.sportevents.db.converters;

import com.sla.sportevents.db.dto.FullSportEventDto;
import com.sla.sportevents.db.dto.SportEventDto;
import com.sla.sportevents.db.entity.Market;
import com.sla.sportevents.db.entity.SportEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FullSportEventDtoToSportEvent implements Converter<FullSportEventDto, SportEvent> {

    private final ConversionService conversionService;

    @Lazy
    public FullSportEventDtoToSportEvent(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public SportEvent convert(FullSportEventDto source) {

        List<Market> markets = source.getMarkets()
                .stream().map(marketDto -> conversionService.convert(marketDto, Market.class)).toList();

        return SportEvent.builder()
                .id(source.getId())
                .description(source.getDescription())
                .homeTeam(source.getHomeTeam())
                .awayTeam(source.getAwayTeam())
                .startTime(source.getStartTime())
                .sport(source.getSport())
                .country(source.getCountry())
                .competition(source.getCompetition())
                .settled(source.getSettled())
                .markets(markets)
                .build();
    }
}
