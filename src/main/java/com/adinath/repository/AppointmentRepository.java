package com.adinath.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adinath.entity.Appointment;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByOrderByIdDesc();

    List<Appointment> findByDoctorAndAppointmentDate(
            String doctor,
            LocalDate appointmentDate);

    boolean existsByDoctorAndAppointmentDateAndTimeSlot(
            String doctor,
            LocalDate appointmentDate,
            String timeSlot);

    boolean existsByDoctorAndAppointmentDateAndTimeSlotAndStatusIn(
            String doctor,
            LocalDate appointmentDate,
            String timeSlot,
            List<String> status);

    long countByStatus(String status);

    long countByAppointmentDate(LocalDate appointmentDate);
}