package com.adinath.controller;

import com.adinath.entity.Contact;
import com.adinath.service.ContactService;
import com.adinath.entity.Appointment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;
import com.adinath.service.AdminService;
import com.adinath.service.AppointmentService;
import com.adinath.entity.Notice;
import com.adinath.service.NoticeService;
import java.security.Principal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Controller
@RequestMapping("/admin")
public class AdminController {

	private final AdminService adminService;
	private final AppointmentService appointmentService;
	private final ContactService contactService;
	private final NoticeService noticeService;
	public AdminController(
	        AdminService adminService,
	        AppointmentService appointmentService,
	        ContactService contactService,
	        NoticeService noticeService) {

	    this.adminService = adminService;
	    this.appointmentService = appointmentService;
	    this.contactService = contactService;
	    this.noticeService = noticeService;

	}
    @GetMapping("/login")
    public String loginPage() {

        return "admin/login";

    }


    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "appointments",
                appointmentService.getAllAppointments());

        model.addAttribute(
                "pendingCount",
                appointmentService.getPendingCount());

        model.addAttribute(
                "confirmedCount",
                appointmentService.getConfirmedCount());

        model.addAttribute(
                "completedCount",
                appointmentService.getCompletedCount());

        model.addAttribute(
                "todayCount",
                appointmentService.getTodayAppointmentsCount());
        model.addAttribute(
                "contacts",
                contactService.getAll());

        model.addAttribute(
                "contactCount",
                contactService.getCount());
        model.addAttribute(
                "activeNotice",
                noticeService.getActiveNotice());
        return "admin/dashboard";

    }

    @GetMapping("/appointment/confirm/{id}")
    public String confirmAppointment(
            @PathVariable Long id) {

       

        appointmentService.updateStatus(id, "CONFIRMED");

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/appointment/reject/{id}")
    public String rejectAppointment(
            @PathVariable Long id) {

        

        appointmentService.updateStatus(id, "REJECTED");

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/appointment/completed/{id}")
    public String completeAppointment(
            @PathVariable Long id) {

        appointmentService.updateStatus(
                id,
                "COMPLETED");

        return "redirect:/admin/dashboard";

    }

    @GetMapping("/appointment/delete/{id}")
    public String deleteAppointment(
            @PathVariable Long id) {

        appointmentService.delete(id);

        return "redirect:/admin/dashboard";

    }
    @ResponseBody
    @GetMapping("/appointment/{id}")
    public Appointment getAppointmentDetails(
            @PathVariable Long id) {

        return appointmentService.getAppointment(id);

    }
    @GetMapping("/contact/delete/{id}")
    public String deleteContact(
            @PathVariable Long id) {

        contactService.delete(id);

        return "redirect:/admin/dashboard";

    }

    @ResponseBody
    @GetMapping("/contact/{id}")
    public Contact getContact(
            @PathVariable Long id) {

        return contactService.getById(id);

    }
    @PostMapping("/notice/save")
    public String saveNotice(
            @RequestParam String message) {

        Notice notice = new Notice();

        notice.setMessage(message);

        noticeService.save(notice);

        return "redirect:/admin/dashboard";

    }

    @GetMapping("/notice/delete/{id}")
    public String deleteNotice(
            @PathVariable Long id) {

        noticeService.delete(id);

        return "redirect:/admin/dashboard";

    }
    @PostMapping("/settings/username")
    public String changeUsername(

            @RequestParam String username,

            Principal principal,

            HttpServletRequest request,

            HttpServletResponse response) {

        boolean updated = adminService.updateUsername(
                principal.getName(),
                username);

        if (updated) {

            new SecurityContextLogoutHandler().logout(
                    request,
                    response,
                    null);

            return "redirect:/admin/login?usernameChanged";

        }

        return "redirect:/admin/dashboard?usernameError";

    }

    @PostMapping("/settings/password")
    public String changePassword(

            @RequestParam String currentPassword,

            @RequestParam String newPassword,

            @RequestParam String confirmPassword,

            Principal principal,

            HttpServletRequest request,

            HttpServletResponse response) {

        if (!newPassword.equals(confirmPassword)) {

            return "redirect:/admin/dashboard?passwordMismatch";

        }

        boolean updated = adminService.updatePassword(

                principal.getName(),

                currentPassword,

                newPassword);

        if (updated) {

            new SecurityContextLogoutHandler().logout(

                    request,

                    response,

                    null);

            return "redirect:/admin/login?passwordChanged";

        }

        return "redirect:/admin/dashboard?currentPasswordWrong";

    }
}