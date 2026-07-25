package com.adinath.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.adinath.entity.Contact;
import com.adinath.mail.MailService;
import com.adinath.service.ContactService;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;
    private final MailService mailService;

    public ContactController(
            ContactService contactService,
            MailService mailService) {

        this.contactService = contactService;
        this.mailService = mailService;

    }

    @GetMapping("")
    public String contactPage(Model model) {

        if (!model.containsAttribute("contact")) {

            model.addAttribute("contact", new Contact());

        }

        return "contact";

    }

    @PostMapping("/save")
    public String saveContact(
            @ModelAttribute Contact contact,
            RedirectAttributes redirectAttributes) {

        Contact savedContact =
                contactService.save(contact);

        try {

            String subject =
                    "New Contact Message - Adinath Clinic";

            String body =
                    "New Contact Message\n\n"

                    + "Name : " + savedContact.getFullName() + "\n"

                    + "Email : " + savedContact.getEmail() + "\n"

                    + "Mobile : " + savedContact.getMobileNumber() + "\n"

                    + "Subject : " + savedContact.getSubject() + "\n\n"

                    + "Message :\n" + savedContact.getMessage();

            mailService.sendMail(
                    "adinathclinic14@gmail.com",
                    subject,
                    body);

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        redirectAttributes.addFlashAttribute(
                "success",
                "Your message has been sent successfully.");

        return "redirect:/contact";

    }
}