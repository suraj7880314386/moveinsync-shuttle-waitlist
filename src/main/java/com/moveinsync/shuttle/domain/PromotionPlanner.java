package com.moveinsync.shuttle.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Pure, side-effect-free (apart from mutating the passed entities/ledger) promotion algorithm.
 *
 * Walk the waitlist in fair order. For each candidate:
 *  - cost-centre quota exhausted      -> skip (a seat may exist but the cap blocks it; does NOT block others)
 *  - not enough vehicle seats (group) -> skip; remember it was "overtaken" if someone behind it gets promoted
 *  - candidate already overtaken maxSkips times and still does not fit -> STOP: reserve the free seats for it
 *    (head-of-line reservation = anti-starvation for large groups)
 *  - otherwise promote ALL of its seats at once (all-or-nothing).
 */
public final class PromotionPlanner {
    private final int maxSkips;

    public PromotionPlanner(int maxSkips) { this.maxSkips = maxSkips; }

    public PassResult plan(List<Booking> waitlisted, SeatLedger ledger, Instant now, boolean autoCheckIn) {
        List<Booking> ordered = new ArrayList<>(waitlisted);
        ordered.sort(WaitlistOrder.COMPARATOR);

        List<Booking> promoted = new ArrayList<>();
        List<Booking> overtaken = new ArrayList<>();
        int quotaBlocked = 0;
        int capacityBlocked = 0;
        boolean reservation = false;

        for (Booking candidate : ordered) {
            if (ledger.freeSeats() <= 0) {
                break;
            }
            if (!ledger.fitsQuota(candidate.getCostCentre(), candidate.getSeats())) {
                quotaBlocked++;
                continue;
            }
            if (!ledger.fitsCapacity(candidate.getSeats())) {
                if (candidate.getSkipCount() >= maxSkips) {
                    reservation = true;
                    break;
                }
                capacityBlocked++;
                overtaken.add(candidate);
                continue;
            }
            ledger.allocate(candidate.getCostCentre(), candidate.getSeats());
            candidate.confirm(now, autoCheckIn);
            promoted.add(candidate);
            overtaken.forEach(Booking::incrementSkipCount);
            overtaken.clear();
        }
        return new PassResult(promoted, quotaBlocked, capacityBlocked, reservation);
    }
}
