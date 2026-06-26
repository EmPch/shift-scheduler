package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntry, Long> {
}