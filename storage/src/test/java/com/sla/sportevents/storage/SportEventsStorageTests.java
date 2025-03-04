package com.sla.sportevents.storage;

import static org.hamcrest.Matchers.containsString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sla.sportevents.db.common.MarketStatus;
import com.sla.sportevents.db.dto.FullSportEventDto;
import com.sla.sportevents.db.dto.MarketDto;
import com.sla.sportevents.db.dto.OutcomeDto;
import com.sla.sportevents.db.entity.SportEvent;
import jakarta.persistence.EntityManager;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
@Transactional
class SportEventsStorageTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations valueOperations;

    @BeforeEach
    public void setUp() {
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Mockito.doNothing().when(valueOperations).set(anyString(), anyString());
    }


    @Test
    void shouldCreateEvent() throws Exception {

        FullSportEventDto fullSportEventDto = FullSportEventDto.builder()
                .description("Arsenal vs Manchester United")
                .homeTeam("Arsenal")
                .awayTeam("Manchester United")
                .startTime(LocalDateTime.of(2025, 1, 31, 18, 0))
                .sport("Football")
                .country("England")
                .competition("Premier League")
                .settled(false)
                .markets(List.of(MarketDto.builder()
                        .description("Match Betting")
                        .status(MarketStatus.OPEN)
                        .settled(false)
                        .outcomes(List.of(OutcomeDto.builder()
                                .description("Arsenal to win")
                                .price(BigDecimal.valueOf(100.0))
                                .settled(false)
                                .build()))
                        .build()))
                .build();

        String body = objectMapper.writeValueAsString(fullSportEventDto);

        this.mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("description").value("Arsenal vs Manchester United"));
    }

    @Test
    void shouldNotCreateEventWithId() throws Exception {

        FullSportEventDto fullSportEventDto = FullSportEventDto.builder()
                .id(1L)
                .description("Arsenal vs Manchester United")
                .markets(List.of())
                .build();

        this.mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(fullSportEventDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Sport Event Id is not null. Please use the Update endpoint instead.")));
    }

    @Test
    void shouldUpdateEventWithId() throws Exception {

        SportEvent sportEvent1 = SportEvent.builder()
                .description("Event 1")
                .sport("Football")
                .settled(false)
                .markets(List.of())
                .startTime(LocalDateTime.of(2025, 1, 31, 18, 0))
                .build();
        entityManager.persist(sportEvent1);

        FullSportEventDto fullSportEventDto = FullSportEventDto.builder()
                .id(sportEvent1.getId())
                .description("Arsenal vs Manchester United")
                .markets(List.of())
                .build();

        this.mockMvc.perform(put("/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(fullSportEventDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("description").value("Arsenal vs Manchester United"));
    }

    @Test
    void shouldNotUpdateEventWithoutId() throws Exception {

        FullSportEventDto fullSportEventDto = FullSportEventDto.builder()
                .description("Arsenal vs Manchester United")
                .markets(List.of())
                .build();

        this.mockMvc.perform(put("/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(fullSportEventDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Sport Event Id is null. Please use the Create endpoint instead.")));
    }

    @Test
    void shouldDeleteEventWithId() throws Exception {

        SportEvent sportEvent1 = SportEvent.builder()
                .description("Event 1")
                .sport("Football")
                .settled(false)
                .markets(List.of())
                .startTime(LocalDateTime.of(2025, 1, 31, 18, 0))
                .build();

        entityManager.persist(sportEvent1);

        this.mockMvc.perform(delete("/events/%s".formatted(sportEvent1.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("description").value("Event 1"));
    }

    @Test
    void shouldNotDeleteEventWithNonExistingId() throws Exception {

        this.mockMvc.perform(delete("/events/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound());
    }


}
