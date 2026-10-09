# Demo video script (~10 minutes)
Screen layout: left = VS Code (code), right = terminal / Postman. Run the app and keep `docs/TEST_FLOW.md` helpers loaded.

| Time | Show | Say |
|---|---|---|
| 0:00-0:45 | README top + ER diagram, project tree | Problem in one line: full shuttle, fair + instant promotion, quotas, groups, no double seat. Stack: Spring Boot 3.5, Java 17, JPA/H2, JWT. Layering controller->service->repository. |
| 0:45-1:30 | `mvn spring-boot:run`, seeded users log lines, `/actuator/health` | Boots with demo users; file DB so data survives restart. |
| 1:30-2:15 | Login admin/employee (section 1 of flow), show 401 without token, 403 employee creating trip | Stateless JWT, three roles: EMPLOYEE, APPROVER (quota changes, waitlist view), ADMIN. |
| 2:15-4:30 | Scenario A step by step: create trip(4), quotas, fill bus, 4 waitlisted, waitlist view | Explicit ordered waitlist. Cancel #1: one seat -> group of 2 skipped, next single promoted + SMS in app log. Cancel #2: seat exists but ENG quota blocks carol -> goes to grace. Cancel #3: group promoted all-or-nothing. |
| 4:30-5:15 | Open `PromotionPlanner.java` and `WaitlistOrder.java` | FIFO at 1 s, tier tiebreak only inside the second -> no starvation; skip counter then seat reservation for big groups. |
| 5:15-6:45 | Open `TripRepository.findByIdForUpdate` + `BookingService.requestSeats`; run Scenario B (7 parallel curls); then `mvn test -Dtest=SeatConcurrencyTest` | One DB row lock per trip serialises seat changes; capacity derived from CONFIRMED rows so no drift. Exactly 1 confirmed; 20-thread test and double-cancel test pass; group never split. |
| 6:45-7:45 | Scenario D no-show (pre-start it before this section) + `/api/admin/notifications` | Cut-off job frees the seat, promotes next, auto-checked-in; SMS stored in outbox before send, retried if provider fails. |
| 7:45-8:45 | `/actuator/metrics/shuttle.*`, `/actuator/prometheus`, availability twice (same `computedAt`), JSON log profile / requestId in logs | Monitoring counters (promotions, rejections, skips), cache evicted after commit + 10 s TTL, what can be stale. |
| 8:45-9:30 | Error responses (400/409/422/503 mapping), reconcile + backup endpoints, README trade-offs table | Non-leaking errors with requestId; self-healing reconcile, backup/restore runbook; pessimistic lock vs optimistic trade-off, complexity. |
| 9:30-10:00 | `mvn clean verify` result, GitHub commit history | Tests prove order, quota, groups, concurrency. Thanks. |
