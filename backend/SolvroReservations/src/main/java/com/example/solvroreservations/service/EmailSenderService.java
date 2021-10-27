package com.example.solvroreservations.service;

import com.example.solvroreservations.util.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Filip Wisniewski
 * Service logic class responsible for sending e-mails
 */
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    /**
     * Sends given email to its destination
     * @param mail contains all information about sender, subject and thymeleaf template which should be used
     * @see com.example.solvroreservations.util.Mail
     * @throws MessagingException thrown by MimeMessageHelper
     */
    public void send(Mail mail) throws MessagingException {
        Context ctx = new Context();
        ctx.setVariables(mail.getAttributes());
        String html = templateEngine.process(mail.getTemplateName() + ".html", ctx);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(html, true);
        mailSender.send(message);
    }
}