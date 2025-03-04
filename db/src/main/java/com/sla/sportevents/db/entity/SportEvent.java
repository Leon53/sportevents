package com.sla.sportevents.db.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportEvent {


    @Id
    @GeneratedValue
    @Column
    private Long id;                    // id (auto-generated)

    @Column
    private String description;         // (e.g.: “Arsenal v Manchester United”)

    @Column
    private String homeTeam;            // (e.g.: “Arsenal”)

    @Column
    private String awayTeam;            // (e.g.: “Manchester United”)

    @Column
    private LocalDateTime startTime;    // (e.g.: 31/01/2025 18:00)

    @Column
    private String sport;               // (e.g.: “Football”)

    @Column
    private String country;             // (e.g.: “England”)

    @Column
    private String competition;         // (e.g.: “Premier League”)

    @Column
    private Boolean settled;            // settled (Boolean)

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "sportevent_id")
    private List<Market> markets;       // markets

}
