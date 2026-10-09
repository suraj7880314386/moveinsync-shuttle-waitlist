package com.moveinsync.shuttle.config;

import com.moveinsync.shuttle.domain.AppUser;
import com.moveinsync.shuttle.domain.Role;
import com.moveinsync.shuttle.domain.Tier;
import com.moveinsync.shuttle.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Dev/demo users (idempotent). Disable with app.seed.enabled=false. CHANGE THESE PASSWORDS outside demos. */
@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DataSeeder implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public DataSeeder(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        seed("admin", "Admin@123", Role.ADMIN, "HQ", Tier.PLATINUM, "+919000000001");
        seed("approver", "Approver@123", Role.APPROVER, "HQ", Tier.GOLD, "+919000000002");
        seed("alice", "Pass@123", Role.EMPLOYEE, "ENG", Tier.PLATINUM, "+919000000011");
        seed("bob", "Pass@123", Role.EMPLOYEE, "ENG", Tier.GOLD, "+919000000012");
        seed("carol", "Pass@123", Role.EMPLOYEE, "ENG", Tier.STANDARD, "+919000000013");
        seed("dave", "Pass@123", Role.EMPLOYEE, "OPS", Tier.STANDARD, "+919000000014");
        seed("erin", "Pass@123", Role.EMPLOYEE, "OPS", Tier.SILVER, "+919000000015");
        seed("frank", "Pass@123", Role.EMPLOYEE, "HR", Tier.STANDARD, "+919000000016");
        seed("grace", "Pass@123", Role.EMPLOYEE, "HR", Tier.GOLD, "+919000000017");
    }

    private void seed(String username, String password, Role role, String cc, Tier tier, String phone) {
        if (!users.existsByUsername(username)) {
            users.save(new AppUser(username, encoder.encode(password), role, cc, tier, phone));
            log.info("event=seeded_user username={} role={}", username, role);
        }
    }
}
