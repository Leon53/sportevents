package com.sla.sportevents.db.repository;

import com.sla.sportevents.db.entity.SportEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class SportEventRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SportEventRepository sportEventRepository;

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
    public void testFindNonSettledByCriteriaEmpty_success() {

        SportEvent sportEvent1 = SportEvent.builder().description("Event 1").settled(false).build();
        entityManager.persist(sportEvent1);
        SportEvent sportEvent2 = SportEvent.builder().description("Event 2").settled(true).build();
        entityManager.persist(sportEvent2);

        List<SportEvent> result = sportEventRepository.findNonSettledByCriteria(null);
        assertEquals(1, result.size());
        assertEquals("Event 1", result.get(0).getDescription());
        assertEquals(false, result.get(0).getSettled());

    }

    @Test
    public void testFindNonSettledByCriteriaSport_success() {

        SportEvent sportEvent1 = SportEvent.builder().description("Event 1").settled(false).sport("Football").build();
        entityManager.persist(sportEvent1);
        SportEvent sportEvent2 = SportEvent.builder().description("Event 2").settled(false).sport("Basketball").build();
        entityManager.persist(sportEvent2);
        SportEvent sportEvent3 = SportEvent.builder().description("Event 3").settled(true).sport("Football").build();
        entityManager.persist(sportEvent3);

        List<SportEvent> result = sportEventRepository.findNonSettledByCriteria("Football");
        assertEquals(1, result.size());
        assertEquals("Event 1", result.get(0).getDescription());
        assertEquals(false, result.get(0).getSettled());

    }

}
