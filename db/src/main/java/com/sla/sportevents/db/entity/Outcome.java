package com.sla.sportevents.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Outcome {

    @Id
    @GeneratedValue
    @Column
    private Long id;                    // id (auto-generated)

    @Column
    private String description;         // (e.g.: “Arsenal to win”)

    @Column
    private Boolean settled;            // Boolean

    @Column
    private BigDecimal price;           // (e.g.: 2.0)

    @Column
    private String result;              //  (null/win/lose)

}
