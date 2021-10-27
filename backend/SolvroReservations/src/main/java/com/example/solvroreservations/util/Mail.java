package com.example.solvroreservations.util;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * @author Filip Wisniewski
 * Model class containing information about e-mail to be sent
 * Includes information about sender, destination address and thymeleaf template name
 * thet should be used to generate html body using given attributes
 */
@Getter
@Setter
public class Mail {
    private final String from;
    private String to;
    private final String subject;
    private final String templateName;
    private final HashMap<String, Object> attributes;

    public Mail(String from, String subject, String templateName) {
        this.from = from;
        this.subject = subject;
        this.templateName = templateName;
        this.attributes = new HashMap<>();
    }

    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }
}
