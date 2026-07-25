package com.adinath.controller;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.adinath.service.NoticeService;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adinath.entity.DoctorAvailability;
import com.adinath.service.DoctorAvailabilityService;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.adinath.mail.MailService;
import com.adinath.entity.Appointment;
import com.adinath.service.AppointmentService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/appointment")
public class AppointmentController {

	private final AppointmentService appointmentService;
	private final DoctorAvailabilityService doctorAvailabilityService;
	private final MailService mailService;
	private final NoticeService noticeService;
	public AppointmentController(
	        AppointmentService appointmentService,
	        DoctorAvailabilityService doctorAvailabilityService,
	        MailService mailService,
	        NoticeService noticeService) {

	    this.appointmentService = appointmentService;
	    this.doctorAvailabilityService = doctorAvailabilityService;
	    this.mailService = mailService;
	    this.noticeService = noticeService;

	}

	@GetMapping("")
	public String appointmentPage(Model model) {

	    if (!model.containsAttribute("appointment")) {

	        model.addAttribute("appointment", new Appointment());

	    }
	    model.addAttribute(
	            "activeNotice",
	            noticeService.getActiveNotice());
	    return "appointment";
	}

	@PostMapping("/save")
	public String saveAppointment(
	        @ModelAttribute Appointment appointment,
	        RedirectAttributes redirectAttributes,
	        Model model) {

	    if (!appointmentService.validateAppointment(appointment)) {

	        model.addAttribute(
	                "error",
	                "Selected date or time slot is no longer available.");

	        model.addAttribute(
	                "appointment",
	                appointment);

	        return "appointment";

	    }

	    Appointment savedAppointment =
	            appointmentService.saveAppointment(appointment);

	    try {

	        mailService.sendAppointmentNotification(savedAppointment);

	    } catch (Exception e) {

	        System.out.println(
	                "Email Error : " + e.getMessage());

	    }

	    redirectAttributes.addFlashAttribute(
	            "success",
	            "Appointment Request Submitted Successfully.");

	    return "redirect:/appointment";

	}

    @ResponseBody
    @GetMapping("/doctors")
    public List<String> doctors(@RequestParam String department) {

        if (department.equals("DENTAL")) {
            return List.of(
                    "Dr. Rajkumar Sharma",
                    "Dr. Sandeep Shrivastav");
        }

        return List.of("Harsh Sharma");
    }

    @ResponseBody
    @GetMapping("/reasons")
    public List<String> reasons(@RequestParam String department) {

        if (department.equals("DENTAL")) {

            return List.of(
            		"General Check-up",
                    "Tooth Pain",
                    "Root Canal Treatment",
                    "Teeth Cleaning (Scaling)",
                    "Tooth Extraction",
                    "Denture",
                    "Crown / Cap",
                    "Braces Consultation",
                    "Other");
        }

        return List.of(
                "Eye Check-up",
                "Vision Test",
                "Contact Lens",
                "Cataract Consultation",
                "Glaucoma Screening",
                "Computer Vision Problem",
                "Spectacles / Fancy Glasses",
                "Other");
    }
    @ResponseBody
    @GetMapping("/available-dates")
    public List<String> availableDates(
            @RequestParam String doctor) {

        return doctorAvailabilityService

                .getAvailableDates(doctor)

                .stream()

                .map(DoctorAvailability::getAvailableDate)

                .map(LocalDate::toString)

                .collect(Collectors.toList());

    }
    @ResponseBody
    @GetMapping("/time-slots")
    public List<String> timeSlots(
            @RequestParam String doctor,
            @RequestParam LocalDate date) {

        List<DoctorAvailability> availabilityList =
                doctorAvailabilityService
                        .getDoctorAvailability(
                                doctor,
                                date);

        if (availabilityList.isEmpty()) {

            return List.of();

        }

        DoctorAvailability availability =
                availabilityList.get(0);

        LocalTime current =
                availability.getStartTime();

        LocalTime end =
                availability.getEndTime();

        List<String> slots = new ArrayList<>();

        while (current.isBefore(end)) {

            
            if (date.equals(LocalDate.now())) {

                LocalTime now = LocalTime.now();

                if (current.isBefore(now.plusMinutes(15))) {

                    current = current.plusMinutes(30);

                    continue;

                }

            }

            String slot = current.toString();

            if (!appointmentService.isSlotBooked(
                    doctor,
                    date,
                    slot)) {

                slots.add(slot);

            }

            current = current.plusMinutes(30);

        }

        return slots;

    }
}