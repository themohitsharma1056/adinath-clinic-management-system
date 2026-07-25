package com.adinath.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adinath.entity.DoctorAvailability;

public interface DoctorAvailabilityRepository
        extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctorName(String doctorName);

    List<DoctorAvailability> findByDoctorNameAndAvailableTrue(
            String doctorName);

    List<DoctorAvailability> findByDoctorNameAndAvailableDate(
            String doctorName,
            LocalDate availableDate);

    List<DoctorAvailability> findByDoctorNameAndAvailableDateAndAvailableTrue(
            String doctorName,
            LocalDate availableDate);

    List<DoctorAvailability> findAllByOrderByAvailableDateAsc();

}