package com.sla.sportevents.db.converters;

import com.sla.sportevents.db.common.MarketStatus;
import com.sla.sportevents.db.dto.MarketDto;
import com.sla.sportevents.db.dto.OutcomeDto;
import com.sla.sportevents.db.entity.Market;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

@Component
public class MarketToMarketDto implements Converter<Market, MarketDto>{

    private final ConversionService conversionService;

    @Lazy
    public MarketToMarketDto(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public MarketDto convert(Market source) {
        List<OutcomeDto> outcomeDtos = source.getOutcomes()
                .stream().map(outcome -> conversionService.convert(outcome, OutcomeDto.class)).toList();

        return MarketDto.builder()
                .id(source.getId())
                .description(source.getDescription())
                .status(MarketStatus.fromString(source.getStatus()))
                .settled(source.getSettled())
                .outcomes(outcomeDtos)
                .build();
    }

}
