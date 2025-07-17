package tobyspring.splearn.adapter.integration;

import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.domain.Email;

public class DummyEmailSender implements EmailSender {

    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender send email: " + email);
    }
}
