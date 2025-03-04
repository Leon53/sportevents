package com.sla.sportevents.db.common;

public enum MarketStatus {

    OPEN,
    CLOSE;

    public static MarketStatus fromString(String input) {
        for (MarketStatus marketStatus : MarketStatus.values()) {
            if (marketStatus.toString().equalsIgnoreCase(input)) {
                return marketStatus;
            }
        }

        return null;
    }
}
