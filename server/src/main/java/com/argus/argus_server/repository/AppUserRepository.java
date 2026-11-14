package com.argus.argus_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.argus.argus_server.model.AppUser;

public interface AppUserRepository
        extends JpaRepository<AppUser, Integer> {
}
