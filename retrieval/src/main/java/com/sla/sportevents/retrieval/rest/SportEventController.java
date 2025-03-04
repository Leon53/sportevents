package com.sla.sportevents.retrieval.rest;

import com.sla.sportevents.db.common.exceptions.BadRequestException;
import com.sla.sportevents.db.common.exceptions.ItemNotFoundException;
import com.sla.sportevents.db.dto.CriteriaDto;
import com.sla.sportevents.db.dto.FilterCriteriaDto;
import com.sla.sportevents.db.dto.SportEventDto;
import com.sla.sportevents.db.service.SportEventService;
import com.sla.sportevents.retrieval.config.RestApiRoutes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sla.sportevents.db.common.SportEventConstants.SearchCriteria.SPORT;

@RestController
@RequestMapping(path = RestApiRoutes.EVENTS)
public class SportEventController {

    private final SportEventService sportEventsService;

    public SportEventController(SportEventService sportEventsService) {
        this.sportEventsService = sportEventsService;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity
                .status(mapHttpStatus(e))
                .body(e.getMessage());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return handleNullableResult(sportEventsService.read(id));
    }

    @GetMapping(path = "/non-settled")
    public ResponseEntity<?> getNonSettled(@RequestParam(required = false) String sport) {
        FilterCriteriaDto filterCriteriaDto = FilterCriteriaDto.builder()
                .filterCriteria(List.of(CriteriaDto.builder()
                                .field(SPORT)
                                .value(sport)
                                .build()))
                .build();
        return handleNullableResult(sportEventsService.findNonSettledByCriteria(filterCriteriaDto));
    }

    private ResponseEntity<?> handleNullableResult(Object result) {

        if (result != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(result);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    private HttpStatus mapHttpStatus(Exception e) {
        if (e instanceof BadRequestException) {
            return HttpStatus.BAD_REQUEST;
        } else if (e instanceof ItemNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
