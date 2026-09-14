package com.OnlineCanteen.PaymentService.Service;



import com.OnlineCanteen.PaymentService.Dto.PaymentRequest;
import com.OnlineCanteen.PaymentService.Dto.VerifyPaymentRequest;
import com.OnlineCanteen.PaymentService.Entity.Payment;
import com.OnlineCanteen.PaymentService.Repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${RAZORPAY_ID}")
    private String keySecret;

    @Value("${RAZORPAY_SECRET}")
    private String webhookSecret;

    /*
      Create Razorpay order
    */
    @Transactional
    public Payment createPaymentOrder(PaymentRequest request) throws Exception {

        JSONObject options = new JSONObject();

        /*
          Razorpay uses paisa
          ₹500 = 50000
        */

        options.put("amount", request.getAmount() * 100);

        options.put("currency", "INR");

        options.put("receipt", "receipt_" + request.getOrderId());

        Order razorpayOrder = razorpayClient.orders.create(options);

        Payment payment = new Payment();

        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());

        payment.setRazorpayOrderId(razorpayOrder.get("id"));
        payment.setStatus("CREATED");

        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    /*
      Frontend success callback verification
    */
    @Transactional
    public String verifyPayment(VerifyPaymentRequest request) throws Exception {

        Payment payment =
                paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                        .orElseThrow(() ->
                                new RuntimeException("Payment record not found"));

        /*
          Idempotency protection
        */
        if ("SUCCESS".equals(payment.getStatus())) {
            return "Payment already verified";
        }

        JSONObject options = new JSONObject();

        options.put("razorpay_order_id", request.getRazorpayOrderId());
        options.put("razorpay_payment_id", request.getRazorpayPaymentId());
        options.put("razorpay_signature", request.getRazorpaySignature());

        boolean verified = Utils.verifyPaymentSignature(options, keySecret);

        if (!verified) {
            throw new RuntimeException("Invalid payment signature");
        }

        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setSignature(request.getRazorpaySignature());
        payment.setStatus("SUCCESS");
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        try {

            /*
              inform ORDER-SERVICE
            */
            restTemplate.put(
                    "http://ORDER-SERVICE/api/order/"
                            + payment.getOrderId()
                            + "/payment-success",
                    null
            );

        } catch (Exception e) {

            /*
              order update retry future
              rabbitmq/kafka later
            */
            payment.setStatus("ORDER_UPDATE_PENDING");
            paymentRepository.save(payment);
        }

        return payment.getOrderId().toString();
    }

    /*
      Webhook backup path
    */
    @Transactional
    public String processWebhook(String payload,
                                 String signature) throws Exception {

        boolean valid =
                Utils.verifyWebhookSignature(
                        payload,
                        signature,
                        webhookSecret
                );

        if (!valid) {
            throw new RuntimeException("Invalid webhook signature");
        }

        JSONObject json = new JSONObject(payload);

        String event = json.getString("event");
        String customerEmail= null;

        if ("payment.captured".equals(event)) {

            JSONObject paymentEntity =
                    json.getJSONObject("payload")
                            .getJSONObject("payment")
                            .getJSONObject("entity");

            String razorpayOrderId =
                    paymentEntity.getString("order_id");

            String razorpayPaymentId =
                    paymentEntity.getString("id");

            Payment payment =
                    paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                            .orElseThrow(() ->
                                    new RuntimeException("Payment not found"));

            /*
              duplicate webhook protection
            */
            if ("SUCCESS".equals(payment.getStatus())) {
                return "Already processed";
            }

            payment.setRazorpayPaymentId(razorpayPaymentId);
            payment.setStatus("SUCCESS");
            payment.setUpdatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            try {

                restTemplate.put(
                        "http://ORDER-SERVICE/api/order/"
                                + payment.getOrderId()
                                + "/payment-success",
                        null
                );
                restTemplate.postForObject(
                        "http://NOTIFICATION-SERVICE/api/notification/payment-success?email="
                                + "rajput.prince.dev@gmail.com",
                        null,
                        String.class
                );

            }
            catch (Exception e) {

                payment.setStatus("ORDER_UPDATE_PENDING");
                paymentRepository.save(payment);
                System.out.println("Payment pending From webhook error ");
            }
        }

        return "Webhook processed";
    }

    /*
      Payment failure handling
    */
    @Transactional
    public String markPaymentFailed(String razorpayOrderId) {

        Payment payment =
                paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                        .orElseThrow(() ->
                                new RuntimeException("Payment not found"));

        payment.setStatus("FAILED");
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        try {

            restTemplate.put(
                    "http://ORDER-SERVICE/api/order/"
                            + payment.getOrderId()
                            + "/payment-failed",
                    null
            );

        } catch (Exception e) {
            // can retry later
        }

        return "Payment failed updated";
    }
}