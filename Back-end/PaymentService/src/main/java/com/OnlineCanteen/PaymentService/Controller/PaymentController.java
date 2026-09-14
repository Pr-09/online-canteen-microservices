package com.OnlineCanteen.PaymentService.Controller;


import com.OnlineCanteen.PaymentService.Dto.PaymentRequest;
import com.OnlineCanteen.PaymentService.Dto.VerifyPaymentRequest;
import com.OnlineCanteen.PaymentService.Entity.Payment;
import com.OnlineCanteen.PaymentService.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /*
      Create Razorpay payment order
    */
    @PostMapping("/create-order")
    public Payment createOrder(
            @RequestBody PaymentRequest request
    ) throws Exception {
        return paymentService.createPaymentOrder(request);
    }

    /*
      Frontend payment verification
    */
    @PostMapping("/verify")
    public String verifyPayment(
            @RequestBody VerifyPaymentRequest request
    ) throws Exception {

        return paymentService.verifyPayment(request);
    }

    /*
      Razorpay webhook backup
    */
    @PostMapping("/webhook")
    public String webhook(@RequestBody String payload, @RequestHeader("X-Razorpay-Signature") String signature
    ) throws Exception {

        return paymentService.processWebhook(payload, signature);
    }

    /*
      frontend failure callback
    */
    @PutMapping("/failed")
    public String failedPayment(
            @RequestParam String razorpayOrderId
    ) {

        return paymentService.markPaymentFailed(razorpayOrderId);
    }
}
