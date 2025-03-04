package com.sla.sportevents.db.converters;

import com.sla.sportevents.db.common.OutcomeResult;
import com.sla.sportevents.db.dto.OutcomeDto;
import com.sla.sportevents.db.entity.Outcome;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class OutcomeToOutcomeDto implements Converter<Outcome, OutcomeDto>{

    @Override
    public OutcomeDto convert(Outcome source) {
        return OutcomeDto.builder()
                .id(source.getId())
                .description(source.getDescription())
                .price(source.getPrice())
                .settled(source.getSettled())
                .result(OutcomeResult.fromString(source.getResult()))
                .build();
    }
}
