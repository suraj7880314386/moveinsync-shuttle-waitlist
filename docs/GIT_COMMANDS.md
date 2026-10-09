# Git: structured commits and public GitHub push

Create an EMPTY public repo on github.com first (no README/.gitignore), e.g. `shuttle-waitlist`.
Run from the project root in the VS Code terminal:

```bash
git init -b main
git config user.name  "Your Name"
git config user.email "you@example.com"

git add pom.xml .gitignore src/main/resources src/main/java/com/moveinsync/shuttle/ShuttleWaitlistApplication.java src/main/java/com/moveinsync/shuttle/config
git commit -m "chore: bootstrap Spring Boot project, configuration and properties"

git add src/main/java/com/moveinsync/shuttle/domain src/main/java/com/moveinsync/shuttle/repository
git commit -m "feat(domain): booking/trip/quota entities, repositories, SeatLedger, WaitlistOrder, PromotionPlanner"

git add src/test/java/com/moveinsync/shuttle/domain
git commit -m "test(domain): prove waitlist order/tiebreaker and promotion rules (quota, groups, starvation)"

git add src/main/java/com/moveinsync/shuttle/exception src/main/java/com/moveinsync/shuttle/dto src/main/java/com/moveinsync/shuttle/metrics
git commit -m "feat(core): API error model, request/response DTOs, business metrics"

git add src/main/java/com/moveinsync/shuttle/notification
git commit -m "feat(notifications): transactional outbox, SMS gateway port, retrying dispatcher"

git add src/main/java/com/moveinsync/shuttle/service/BookingService.java src/main/java/com/moveinsync/shuttle/service/TripService.java src/main/java/com/moveinsync/shuttle/service/WaitlistPromotionService.java src/main/java/com/moveinsync/shuttle/service/WaitlistQueryService.java src/main/java/com/moveinsync/shuttle/service/SeatLedgerLoader.java
git commit -m "feat(service): locked booking/cancel/check-in/reclaim flows with fair auto-promotion"

git add src/main/java/com/moveinsync/shuttle/service/AvailabilityService.java src/main/java/com/moveinsync/shuttle/service/TripCacheEvictor.java src/main/java/com/moveinsync/shuttle/service/TripChangedEvent.java
git commit -m "feat(cache): Caffeine availability cache with after-commit eviction"

git add src/main/java/com/moveinsync/shuttle/security src/main/java/com/moveinsync/shuttle/service/UserService.java
git commit -m "feat(security): JWT authentication, BCrypt, role-based access (EMPLOYEE/APPROVER/ADMIN)"

git add src/main/java/com/moveinsync/shuttle/web
git commit -m "feat(api): REST controllers, global exception handler, request-id logging filter"

git add src/main/java/com/moveinsync/shuttle/scheduler src/main/java/com/moveinsync/shuttle/service/ReconciliationService.java src/main/java/com/moveinsync/shuttle/service/BackupService.java
git commit -m "feat(resilience): no-show reclaim job, reconciliation self-heal, H2 backup"

git add src/test
git commit -m "test: concurrency (last seat, double cancel, groups), quota, no-show and security tests"

git add README.md docs
git commit -m "docs: README with design, trade-offs and runbook; test flow; demo script"

git status                 # should be clean
git log --oneline          # ~12 meaningful commits

git remote add origin https://github.com/<your-username>/shuttle-waitlist.git
git push -u origin main
```
If asked to authenticate: use a GitHub Personal Access Token as the password (or `gh auth login`).
If a later fix is needed: `git commit -am "fix: <what>"` and `git push`.
