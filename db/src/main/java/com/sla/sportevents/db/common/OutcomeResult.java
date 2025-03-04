package com.sla.sportevents.db.common;

public enum OutcomeResult {
    WIN,
    LOSE;

    public static OutcomeResult fromString(String input) {
        for (OutcomeResult outcomeResult : OutcomeResult.values()) {
            if (outcomeResult.toString().equalsIgnoreCase(input)) {
                return outcomeResult;
            }
        }

        return null;
    }
}
