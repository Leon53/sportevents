package com.sla.sportevents.db.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sla.sportevents.db.common.CacheId;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullSportEventDto implements CacheId {

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

    private List<MarketDto> markets;       // markets

}
