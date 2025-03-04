package com.sla.sportevents.db.repository;

import com.sla.sportevents.db.entity.SportEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SportEventRepository extends CrudRepository<SportEvent, Long> {

    @Query(value = "SELECT se.* " +
            "FROM sport_event se " +
            "WHERE (:sport is null OR se.sport = :sport ) " +
            "AND NOT se.settled " +
            "ORDER BY start_time ", nativeQuery = true)
    List<SportEvent> findNonSettledByCriteria(String sport);

}
