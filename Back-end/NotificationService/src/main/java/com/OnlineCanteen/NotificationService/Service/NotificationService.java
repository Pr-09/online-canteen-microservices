package com.OnlineCanteen.NotificationService.Service;

import com.OnlineCanteen.NotificationService.Dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender javaMailSender;

    /*
      generic email sender
    */
    public String sendEmail(EmailRequest request) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(request.getTo());
        mail.setSubject(request.getSubject());
        mail.setText(request.getMessage());

        javaMailSender.send(mail);

        return "Email sent successfully";
    }

    /*
      order placed mail
    */
    public String sendOrderPlacedMail(String email,
                                      Integer estimatedTime) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(email);

        mail.setSubject("Order Placed Successfully");

        mail.setText(
                "Your order has been placed successfully.\n"
                        + "Estimated delivery time: "
                        + estimatedTime
                        + " minutes."
        );

        javaMailSender.send(mail);

        return "Order mail sent";
    }

    /*
      payment success
    */
    public String sendPaymentSuccessMail(String email) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(email);

        mail.setSubject("Payment Successful");

        mail.setText(
                "Your payment was successful.\n"
                        + "Your order is confirmed."
        );

        javaMailSender.send(mail);

        return "Payment success mail sent";
    }

    /*
      order ready
    */
    public String sendOrderReadyMail(String email) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(email);

        mail.setSubject("Order Ready");

        mail.setText(
                "Your food is ready.\n"
                        + "Please collect your order."
        );

        javaMailSender.send(mail);

        return "Order ready mail sent";
    }

    /*
      udhar reminder
    */
    public String sendUdharReminder(String email,
                                    Double amount) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(email);

        mail.setSubject("Pending Payment Reminder");

        mail.setText(
                "You have pending udhar amount of ₹"
                        + amount
                        + ". Please clear it."
        );

        javaMailSender.send(mail);

        return "Reminder sent";
    }
}
