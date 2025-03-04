package com.sla.sportevents.retrieval;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
@Transactional
class SportEventsRetrievalTests {

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
        Mockito.when(valueOperations.get(anyString())).thenReturn("");
    }


    @Test
    void shouldGetEventById() throws Exception {

        SportEvent sportEvent1 = SportEvent.builder()
                .description("Event 1")
                .sport("Football")
                .settled(false)
                .markets(List.of())
                .startTime(LocalDateTime.of(2025, 1, 31, 18, 0))
                .build();

        entityManager.persist(sportEvent1);

        this.mockMvc.perform(get("/events/%s".formatted(sportEvent1.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(sportEvent1.getId()))
                .andExpect(jsonPath("description").value("Event 1"))
                .andExpect(jsonPath("sport").value("Football"));
    }

    @Test
    void shouldReturnNotFoundForNonExistingId() throws Exception {

        this.mockMvc.perform(get("/events/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetNonSettled() throws Exception {

        SportEvent sportEvent1 = SportEvent.builder()
                .description("Event 1")
                .sport("Football")
                .settled(false)
                .markets(List.of())
                .startTime(LocalDateTime.of(2025, 1, 31, 18, 0))
                .build();

        entityManager.persist(sportEvent1);

        SportEvent sportEvent2 = SportEvent.builder()
                .description("Event 2")
                .sport("Football")
                .settled(false)
                .markets(List.of())
                .startTime(LocalDateTime.of(2025, 1, 31, 18, 0))
                .build();
        entityManager.persist(sportEvent2);

        this.mockMvc.perform(get("/events/non-settled")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasSize(2)))
                .andExpect(jsonPath("$[0].description").value("Event 1"));
    }

    @Test
    void shouldGetNonSettledBySport() throws Exception {

        SportEvent sportEvent1 = SportEvent.builder()
                .description("Event 1")
                .sport("Football")
                .settled(false)
                .markets(List.of())
                .startTime(LocalDateTime.of(2025, 1, 31, 18, 0))
                .build();

        entityManager.persist(sportEvent1);
        SportEvent sportEvent2 = SportEvent.builder()
                .description("Event 2")
                .sport("Basketball")
                .settled(false)
                .markets(List.of())
                .startTime(LocalDateTime.of(2025, 1, 31, 18, 0))
                .build();

        entityManager.persist(sportEvent2);

        SportEvent sportEvent3 = SportEvent.builder()
                .description("Event 3")
                .sport("Football")
                .settled(true)
                .markets(List.of())
                .startTime(LocalDateTime.of(2025, 1, 31, 18, 0))
                .build();

        entityManager.persist(sportEvent3);

        this.mockMvc.perform(get("/events/non-settled")
                        .param("sport", "Football")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].sport").value("Football"))
                .andExpect(jsonPath("$[0].description").value("Event 1"));
    }


}
