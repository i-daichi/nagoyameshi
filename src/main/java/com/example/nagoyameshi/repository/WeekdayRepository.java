package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nagoyameshi.entity.Weekday;

@Repository
public interface WeekdayRepository extends JpaRepository<Weekday, Integer> {
}
