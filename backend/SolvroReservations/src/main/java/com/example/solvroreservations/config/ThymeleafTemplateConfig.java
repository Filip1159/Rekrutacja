package com.example.solvroreservations.config;

import com.example.solvroreservations.util.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author Filip Wisniewski
 * Additional spring config
 * Specifies how thymeleaf templates are being resolved while generating emails
 * And create & delete mail beans
 * @see com.example.solvroreservations.service.EmailSenderService for usage
 */
@Configuration
public class ThymeleafTemplateConfig {
    private final String FROM;

    public ThymeleafTemplateConfig(@Value("spring.mail.username") String FROM) {
        this.FROM = FROM;
    }

    /**
     * @return custom thymeleaf template engine for sending emails
     */
    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    /**
     * @return mail with cancellation token template
     */
    @Bean
    public Mail deleteMail() {
        return new Mail(FROM, "Confirm your cancellation request", "delete");
    }

    /**
     * @return mail with reservation id template
     */
    @Bean
    public Mail createMail() {
        return new Mail(FROM, "Your reservation was made", "create");
    }
}