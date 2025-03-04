package com.sla.sportevents.db.converters;

import com.sla.sportevents.db.dto.OutcomeDto;
import com.sla.sportevents.db.entity.Outcome;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class OutcomeDtoToOutcome implements Converter<OutcomeDto, Outcome> {

    @Override
    public Outcome convert(OutcomeDto source) {
        return Outcome.builder()
                .id(source.getId())
                .description(source.getDescription())
                .price(source.getPrice())
                .settled(source.getSettled())
                .result(String.valueOf(source.getResult()))
                .build();
    }
}
