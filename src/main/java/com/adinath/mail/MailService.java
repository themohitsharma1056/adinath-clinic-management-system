package com.adinath.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.adinath.entity.Appointment;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {

        this.mailSender = mailSender;

    }

    public void sendMail(
            String to,
            String subject,
            String body) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

    }

    public void sendAppointmentNotification(Appointment appointment) {

        String subject = "New Appointment Request - Adinath Clinic";

        String body =
                "A new appointment has been booked.\n\n"

                + "Patient Name : " + appointment.getPatientName() + "\n"

                + "Mobile : " + appointment.getMobileNumber() + "\n"

                + "Email : "
                + (appointment.getEmail() == null
                        || appointment.getEmail().isBlank()
                                ? "Not Provided"
                                : appointment.getEmail())
                + "\n"

                + "Department : " + appointment.getDepartment() + "\n"

                + "Doctor : " + appointment.getDoctor() + "\n"

                + "Appointment Date : " + appointment.getAppointmentDate() + "\n"

                + "Time : " + appointment.getTimeSlot() + "\n"

                + "Reason : " + appointment.getReason() + "\n"

                + "Additional Details : "
                + (appointment.getAdditionalDetails() == null
                        || appointment.getAdditionalDetails().isBlank()
                                ? "-"
                                : appointment.getAdditionalDetails());

        sendMail(
                "adinathclinic14@gmail.com",
                subject,
                body);

    }

}