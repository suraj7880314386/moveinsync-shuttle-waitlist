package com.moveinsync.shuttle.domain;

/** Employee tier used ONLY as a waitlist tiebreaker. Lower rank = higher priority. */
public enum Tier {
    PLATINUM(0), GOLD(1), SILVER(2), STANDARD(3);

    private final int rank;

    Tier(int rank) { this.rank = rank; }

    public int rank() { return rank; }
}
