package com.sla.sportevents.db.service;

import com.sla.sportevents.db.SportEventTestConfiguration;
import com.sla.sportevents.db.converters.SportEventToSportEventDto;
import com.sla.sportevents.db.dto.CriteriaDto;
import com.sla.sportevents.db.dto.FilterCriteriaDto;
import com.sla.sportevents.db.dto.SportEventDto;
import com.sla.sportevents.db.entity.SportEvent;
import com.sla.sportevents.db.repository.SportEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.sla.sportevents.db.common.SportEventConstants.SearchCriteria.SPORT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = {SportEventTestConfiguration.class})
public class SportEventServiceTest {

    @Mock
    private SportEventRepository sportEventRepository;

    private ConversionService conversionService;

    private SportEventService sportEventService;

    @BeforeEach
    void setUp() {
        GenericConversionService genericConversionService = new GenericConversionService();
        genericConversionService.addConverter(SportEvent.class, SportEventDto.class, new SportEventToSportEventDto(conversionService));

        conversionService = genericConversionService;
        sportEventService = new SportEventServiceImpl(sportEventRepository, conversionService);
    }

    @Test
    public void testFindNonSettledByCriteria() {
        long eventId = 1L;
        SportEvent mockEvent = SportEvent.builder()
                .id(eventId)
                .sport("Football")
                .description("Test Event")
                .markets(List.of())
                .settled(false).build();

        Mockito.when(sportEventRepository.findNonSettledByCriteria("Football")).thenReturn(List.of(mockEvent));;

        FilterCriteriaDto filterCriteriaDto = FilterCriteriaDto
                .builder()
                .filterCriteria(List.of(CriteriaDto.builder()
                        .field(SPORT)
                        .value("Football")
                        .build()))
                .build();

        List<SportEventDto> result = sportEventService.findNonSettledByCriteria(filterCriteriaDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(eventId, result.get(0).getId());
        assertEquals("Test Event", result.get(0).getDescription());
    }

}
