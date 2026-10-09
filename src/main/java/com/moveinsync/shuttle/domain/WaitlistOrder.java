package com.moveinsync.shuttle.domain;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;

/**
 * THE waitlist order (single source of truth):
 *   1. request time, FIFO, at 1-second resolution      -> nobody can be overtaken by a later second
 *   2. employee tier (PLATINUM first)                   -> tiebreaker ONLY inside the same second
 *   3. server-assigned sequence number                  -> total order, deterministic
 * Because tier only matters inside one second, a low-tier employee can never starve.
 */
public final class WaitlistOrder {
    private WaitlistOrder() {}

    public static final Comparator<Booking> COMPARATOR = Comparator
            .comparing((Booking b) -> b.getRequestedAt().truncatedTo(ChronoUnit.SECONDS))
            .thenComparingInt((Booking b) -> b.getTier().rank())
            .thenComparingLong(Booking::getSequenceNo);
}
