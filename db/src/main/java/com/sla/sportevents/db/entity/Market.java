package com.sla.sportevents.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Market {

    @Id
    @GeneratedValue
    @Column
    private Long id;                    // id (auto-generated)

    @Column
    private String description;         // (e.g.: “Match Betting”)

    @Column
    private String status;              // (OPEN/CLOSE)

    @Column
    private Boolean settled;            // Boolean

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "market_id")
    private List<Outcome> outcomes;       // outcomes

}
