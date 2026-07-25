package com.adinath.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.adinath.entity.Appointment;
import com.adinath.entity.DoctorAvailability;
import com.adinath.repository.AppointmentRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorAvailabilityService doctorAvailabilityService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DoctorAvailabilityService doctorAvailabilityService) {

        this.appointmentRepository = appointmentRepository;
        this.doctorAvailabilityService = doctorAvailabilityService;

    }

    public boolean validateAppointment(Appointment appointment) {
    	if (appointment.getDoctor() == null
    	        || appointment.getAppointmentDate() == null
    	        || appointment.getTimeSlot() == null
    	        || appointment.getDepartment() == null
    	        || appointment.getPatientName() == null
    	        || appointment.getPatientName().trim().isEmpty()) {

    	    return false;

    	}
    	String mobile = appointment.getMobileNumber();

    	if (mobile == null
    	        || !mobile.matches("^[6-9]\\d{9}$")) {

    	    return false;

    	}
        List<DoctorAvailability> availabilityList =
                doctorAvailabilityService.getDoctorAvailability(
                        appointment.getDoctor(),
                        appointment.getAppointmentDate());

        if (availabilityList.isEmpty()) {
            return false;
        }

        DoctorAvailability availability = availabilityList.get(0);
        if (!availability.getAvailable()) {

            return false;

        }
        LocalTime selectedTime =
                LocalTime.parse(appointment.getTimeSlot());

        if (selectedTime.isBefore(availability.getStartTime())
                || selectedTime.isAfter(
                        availability.getEndTime().minusMinutes(30))) {

            return false;

        }

        return !appointmentRepository
                .existsByDoctorAndAppointmentDateAndTimeSlot(
                        appointment.getDoctor(),
                        appointment.getAppointmentDate(),
                        appointment.getTimeSlot());

    }

    public Appointment saveAppointment(Appointment appointment) {

        appointment.setStatus("PENDING");

        return appointmentRepository.save(appointment);

    }

    public List<Appointment> getAllAppointments() {

        return appointmentRepository.findAllByOrderByIdDesc();

    }

    public Appointment getAppointment(Long id) {

        return appointmentRepository.findById(id).orElse(null);

    }

    public void deleteAppointment(Long id) {

        appointmentRepository.deleteById(id);

    }

    public Appointment updateAppointment(Appointment appointment) {

        return appointmentRepository.save(appointment);

    }

    public List<Appointment> getAppointmentsByDoctorAndDate(
            String doctor,
            LocalDate appointmentDate) {

        return appointmentRepository.findByDoctorAndAppointmentDate(
                doctor,
                appointmentDate);

    }
    public boolean isSlotBooked(String doctor,
            LocalDate date,
            String slot) {

return appointmentRepository
.existsByDoctorAndAppointmentDateAndTimeSlotAndStatusIn(
    doctor,
    date,
    slot,
    List.of(
            "PENDING",
            "CONFIRMED"));

}
    public void updateStatus(Long id, String status) {

        Appointment appointment = getAppointment(id);

        if (appointment == null) {
            return;
        }

        appointment.setStatus(status);

        appointmentRepository.save(appointment);

    }

    public void delete(Long id) {

        appointmentRepository.deleteById(id);

    }
    public long getPendingCount() {

        return appointmentRepository.countByStatus("PENDING");

    }

    public long getConfirmedCount() {

        return appointmentRepository.countByStatus("CONFIRMED");

    }

    public long getCompletedCount() {

        return appointmentRepository.countByStatus("COMPLETED");

    }

    public long getTodayAppointmentsCount() {

        return appointmentRepository.countByAppointmentDate(
                LocalDate.now());

    }
  
}
