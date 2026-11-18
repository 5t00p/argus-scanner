package com.argus.argus_server.config;

import com.argus.argus_server.model.AppUser;
import com.argus.argus_server.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** On an empty database, creates the first admin so someone can log in and add the other users. */
@Component
public class AdminSeeder implements CommandLineRunner {

    private final AppUserRepository users;
    private final String adminPassword;

    public AdminSeeder(AppUserRepository users, @Value("${argus.admin.password:changeme}") String adminPassword) {
        this.users = users;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (users.count() > 0) {
            return;
        }
        AppUser admin = new AppUser();
        admin.setName("Admin");
        admin.setEmail("admin@argus.local");
        admin.setRole("admin");
        admin.setPassword(adminPassword);
        users.save(admin);
        System.out.println("Argus: created first admin account admin@argus.local");
    }
}
