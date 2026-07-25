package com.adinath.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.adinath.entity.DoctorAvailability;
import com.adinath.repository.DoctorAvailabilityRepository;

@Service
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository repository;

    public DoctorAvailabilityService(
            DoctorAvailabilityRepository repository) {

        this.repository = repository;
    }

    public DoctorAvailability save(DoctorAvailability availability) {
        return repository.save(availability);
    }

    public List<DoctorAvailability> getAll() {
        return repository.findAll();
    }

    public List<DoctorAvailability> getDoctorAvailability(
            String doctorName,
            LocalDate date) {

        return repository.findByDoctorNameAndAvailableDate(
                doctorName,
                date);

    }

    public List<DoctorAvailability> getAvailableDates(
            String doctorName) {

        return repository.findByDoctorNameAndAvailableTrue(
                doctorName);

    }

    public void saveAll(List<DoctorAvailability> list) {

        repository.saveAll(list);

    }

    public void delete(Long id) {

        repository.deleteById(id);

    }
    public DoctorAvailability getById(Long id) {

        return repository.findById(id).orElse(null);

    }

    public DoctorAvailability update(DoctorAvailability availability) {

        return repository.save(availability);

    }
    public List<DoctorAvailability> getAllAvailability() {

        return repository.findAllByOrderByAvailableDateAsc();

    }

   
}