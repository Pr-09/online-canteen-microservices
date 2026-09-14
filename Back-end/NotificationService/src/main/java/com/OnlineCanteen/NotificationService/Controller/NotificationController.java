package com.OnlineCanteen.NotificationService.Controller;

import com.OnlineCanteen.NotificationService.Dto.EmailRequest;
import com.OnlineCanteen.NotificationService.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
//@CrossOrigin("*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /*
      generic mail
    */
    @PostMapping("/email")
    public String sendEmail(
            @RequestBody EmailRequest request
    ) {
        return notificationService.sendEmail(request);
    }

    /*
      order placed
    */
    @PostMapping("/order-placed")
    public String orderPlaced(
            @RequestParam String email,
            @RequestParam Integer estimatedTime
    ) {
        return notificationService.sendOrderPlacedMail(
                email,
                estimatedTime
        );
    }

    /*
      payment success
    */
    @PostMapping("/payment-success")
    public String paymentSuccess(
            @RequestParam String email
    ) {
        return notificationService.sendPaymentSuccessMail(email);
    }

    /*
      order ready
    */
    @PostMapping("/order-ready")
    public String orderReady(
            @RequestParam String email
    ) {
        return notificationService.sendOrderReadyMail(email);
    }

    /*
      udhar reminder
    */
    @PostMapping("/udhar-reminder")
    public String reminder(
            @RequestParam String email,
            @RequestParam Double amount
    ) {
        return notificationService.sendUdharReminder(
                email,
                amount
        );
    }
}
