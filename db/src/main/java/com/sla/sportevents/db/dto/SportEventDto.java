package com.sla.sportevents.db.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sla.sportevents.db.common.CacheId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class SportEventDto {

    private Long id;                    // id (auto-generated)

    private String description;         // (e.g.: “Arsenal v Manchester United”)

    private String homeTeam;            // (e.g.: “Arsenal”)

    private String awayTeam;            // (e.g.: “Manchester United”)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime startTime;    // (e.g.: 31/01/2025 18:00)

    private String sport;               // (e.g.: “Football”)

    private String country;             // (e.g.: “England”)

    private String competition;         // (e.g.: “Premier League”)

    private Boolean settled;            // settled (Boolean)

}
