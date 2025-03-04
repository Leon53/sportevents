package com.sla.sportevents.storage.rest;

import com.sla.sportevents.db.common.exceptions.BadRequestException;
import com.sla.sportevents.db.common.exceptions.ItemNotFoundException;
import com.sla.sportevents.db.dto.FullSportEventDto;
import com.sla.sportevents.storage.config.RestApiRoutes;

import com.sla.sportevents.db.dto.SportEventDto;
import com.sla.sportevents.db.service.SportEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<?> create(@RequestBody FullSportEventDto fullSportEventDto) {
        return handleNullableResult(sportEventsService.create(fullSportEventDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody FullSportEventDto fullSportEventDto) {
        return handleNullableResult(sportEventsService.update(fullSportEventDto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return handleNullableResult(sportEventsService.delete(id), HttpStatus.OK);
    }

    private ResponseEntity<?> handleNullableResult(Object result, HttpStatus status) {

        if (result != null) {
            return ResponseEntity
                    .status(status)
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
