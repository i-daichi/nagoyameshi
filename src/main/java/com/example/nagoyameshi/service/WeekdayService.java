package com.example.nagoyameshi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Weekday;
import com.example.nagoyameshi.repository.WeekdayRepository;

@Service
public class WeekdayService {
    private final WeekdayRepository weekdayRepository;

    @Autowired
    public WeekdayService(WeekdayRepository weekdayRepository) {
        this.weekdayRepository = weekdayRepository;
    }

    public List<Weekday> findAllById(List<Integer> idList){
        return weekdayRepository.findAllById(idList);
    }

    public List<Weekday> findAll() {
        return weekdayRepository.findAll();
    }
}
