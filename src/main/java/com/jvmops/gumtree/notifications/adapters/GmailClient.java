package com.jvmops.gumtree.notifications.adapters;

import com.jvmops.gumtree.notifications.model.ApartmentReport;
import com.jvmops.gumtree.notifications.model.EmailWithReport;
import com.jvmops.gumtree.notifications.ports.EmailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@Lazy
@Component
@Slf4j
@AllArgsConstructor
class GmailClient implements EmailSender {
    private JavaMailSender emailSender;
    private EmailTemplateProcessor emailTemplateProcessor;

    @Override
    public void initialEmail(ApartmentReport apartmentReport, String subscriberWannabe) {
        EmailWithReport initialEmail = emailTemplateProcessor.initialEmail(apartmentReport, subscriberWannabe);
        initialEmail(initialEmail, subscriberWannabe);
    }

    @Override
    public void notifySubscribers(ApartmentReport apartmentReport) {
        EmailWithReport subscriptionEmail = emailTemplateProcessor.subscriptionEmail(apartmentReport);
        notifySubscribers(subscriptionEmail);
    }

    private void initialEmail(EmailWithReport email, String subscriberWannabe) {
        String emailSubject = email.report().getTitle();
        String emailContent = email.html();
        // for logs
        String cityName = email.report().getCity().getName();
        try {
            MimeMessageWrapper message = prepareMessage(emailSubject, emailContent);
            sendInitialMessage(message, subscriberWannabe);
        } catch (MessagingException e) {
            log.error("Unable to send initial email for {} subscription to {}", cityName, subscriberWannabe, e);
        }
    }

    private void notifySubscribers(EmailWithReport email) {
        var emailSubject = email.report().getTitle();
        var subscribers = email.report().getCity().getSubscribers();
        // for logs
        var cityName = email.report().getCity().getName();
        var apartmentReportType = email.report().getApartmentReportType();

        if (isEmpty(subscribers)) {
            // this is handled before
            log.warn("No one is subscribed to {} report!", cityName);
        }

        try {
            MimeMessageWrapper message = prepareMessage(emailSubject, email.html());
            log.info("Sending {} {} report to: {}", cityName,  subscribers, apartmentReportType);
            notifySubscribers(message, subscribers);
        } catch (MessagingException e) {
            log.error("Unable to send {} {} report to {}",
                    cityName,
                    apartmentReportType,
                    subscribers,
                    e
            );
        }
    }

    private MimeMessageWrapper prepareMessage(String subject, String content) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        message.setContent(content, "text/html; charset=utf-8");

        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setSubject(subject);

        return new MimeMessageWrapper(message, helper);
    }

    private void sendInitialMessage(MimeMessageWrapper messageWrapper, String email) throws MessagingException {
        messageWrapper.helper.setTo(email);
        emailSender.send(messageWrapper.message);
    }

    private void notifySubscribers(MimeMessageWrapper messageWrapper, Set<String> emails) throws MessagingException {
        messageWrapper.helper.setBcc(emails.toArray(new String[0]));
        emailSender.send(messageWrapper.message);
    }

    private record MimeMessageWrapper(MimeMessage message, MimeMessageHelper helper){}
}
