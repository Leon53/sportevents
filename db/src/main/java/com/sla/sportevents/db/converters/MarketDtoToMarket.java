package com.sla.sportevents.db.converters;

import com.sla.sportevents.db.dto.MarketDto;
import com.sla.sportevents.db.entity.Market;
import com.sla.sportevents.db.entity.Outcome;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

@Component
public class MarketDtoToMarket implements Converter<MarketDto, Market>{

    private final ConversionService conversionService;

    @Lazy
    public MarketDtoToMarket(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Market convert(MarketDto source) {

        List<Outcome> outcomes = source.getOutcomes()
                .stream().map(outcomeDto -> conversionService.convert(outcomeDto, Outcome.class)).toList();

        return Market.builder()
                .id(source.getId())
                .description(source.getDescription())
                .status(String.valueOf(source.getStatus()))
                .settled(source.getSettled())
                .outcomes(outcomes)
                .build();
    }
}
