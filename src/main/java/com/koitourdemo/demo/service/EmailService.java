package com.koitourdemo.demo.service;

import com.koitourdemo.demo.model.EmailDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender javaMailSender;

    public void sendWelcomeEmail(EmailDetail emailDetail){
        try{
            Context context = new Context();
            context.setVariable("name", emailDetail.getReceiver().getEmail());
            context.setVariable("button", "return to homepage");
            context.setVariable("link", emailDetail.getLink());

            String template = templateEngine.process("welcome-template", context);

            // creating simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            // setting up necessary details
            mimeMessageHelper.setFrom("tuongbcqse180199@fpt.edu.vn");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            System.out.println("ERROR SENT MAIL!");
        }
    }

    public void sendVerifyEmail(EmailDetail emailDetail){
        try{
            Context context = new Context();
            context.setVariable("name", emailDetail.getReceiver().getEmail());
            context.setVariable("button", "return to homepage");
            context.setVariable("link", emailDetail.getLink());

            String template = templateEngine.process("verify-template", context);

            // creating simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            // setting up necessary details
            mimeMessageHelper.setFrom("tuongbcqse180199@fpt.edu.vn");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            System.out.println("ERROR SENT MAIL!");
        }
    }

    public void sendKoiBillEmail(EmailDetail emailDetail) {
        try {
            Context context = new Context();
            context.setVariable("customerEmail", emailDetail.getReceiver().getEmail());
            context.setVariable("orderId", emailDetail.getOrderId());
            context.setVariable("createAt", emailDetail.getCreateAt());
            context.setVariable("totalPrice", emailDetail.getTotalPrice());
            context.setVariable("orderDetails", emailDetail.getOrderDetails());

            String processedTemplate = templateEngine.process("koi-bill-template", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom("tuongbcqse180199@fpt.edu.vn");
            helper.setTo(emailDetail.getReceiver().getEmail());
            helper.setSubject(emailDetail.getSubject());
            helper.setText(processedTemplate, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("ERROR SENDING MAIL: " + e.getMessage());
        }
    }

    public void sendTourBillEmail(EmailDetail emailDetail) {
        try {
            Context context = new Context();
            context.setVariable("customerEmail", emailDetail.getReceiver().getEmail());
            context.setVariable("orderId", emailDetail.getOrderId());
            context.setVariable("createAt", emailDetail.getCreateAt());
            context.setVariable("totalPrice", emailDetail.getTotalPrice());
            context.setVariable("orderDetails", emailDetail.getOrderDetails());

            String processedTemplate = templateEngine.process("tour-bill-template", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom("tuongbcqse180199@fpt.edu.vn");
            helper.setTo(emailDetail.getReceiver().getEmail());
            helper.setSubject(emailDetail.getSubject());
            helper.setText(processedTemplate, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("ERROR SENDING MAIL: " + e.getMessage());
        }
    }

}
