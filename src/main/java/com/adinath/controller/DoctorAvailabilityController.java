package com.adinath.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import com.adinath.entity.DoctorAvailability;
import com.adinath.service.DoctorAvailabilityService;

@RestController
@RequestMapping("/admin/doctor")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService service;

    public DoctorAvailabilityController(
            DoctorAvailabilityService service) {

        this.service = service;

    }

    @PostMapping("/save")
    public String saveAvailability(

            @RequestParam String doctor,

            @RequestParam List<LocalDate> dates,

            @RequestParam LocalTime startTime,

            @RequestParam LocalTime endTime,

            @RequestParam Boolean available) {

        List<DoctorAvailability> list = new ArrayList<>();

        for (LocalDate date : dates) {

            DoctorAvailability d = new DoctorAvailability();

            d.setDoctorName(doctor);
            d.setAvailableDate(date);
            d.setStartTime(startTime);
            d.setEndTime(endTime);
            d.setAvailable(available);

            list.add(d);

        }

        service.saveAll(list);

        return "Saved";

    }

    @GetMapping("/available-dates")
    public List<LocalDate> getDates(
            @RequestParam String doctor) {

        return service.getAvailableDates(doctor)

                .stream()

                .map(DoctorAvailability::getAvailableDate)

                .collect(Collectors.toList());

    }

    @GetMapping("/all")
    public List<DoctorAvailability> getAllAvailability() {

        return service.getAllAvailability();

    }

    @GetMapping("/{id}")
    public DoctorAvailability getAvailability(
            @PathVariable Long id) {

        return service.getById(id);

    }

    @PostMapping("/update")
    public String updateAvailability(

            @RequestParam Long id,

            @RequestParam String doctor,

            @RequestParam LocalDate date,

            @RequestParam LocalTime startTime,

            @RequestParam LocalTime endTime,

            @RequestParam Boolean available) {

        DoctorAvailability availability =
                service.getById(id);

        if (availability == null) {

            return "Not Found";

        }

        availability.setDoctorName(doctor);
        availability.setAvailableDate(date);
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);
        availability.setAvailable(available);

        service.update(availability);

        return "Updated";

    }

    @DeleteMapping("/delete/{id}")
    public void deleteAvailability(
            @PathVariable Long id) {

        service.delete(id);

    }

}