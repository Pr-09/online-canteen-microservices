package com.OnlineCanteen.AdminService.Controller;

import com.OnlineCanteen.AdminService.Entity.UdharCode;
import com.OnlineCanteen.AdminService.Entity.UdharCustomer;
import com.OnlineCanteen.AdminService.Entity.UdharTransaction;
import com.OnlineCanteen.AdminService.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")

public class AdminController {
    @Autowired
    private AdminService adminService;

//    public AdminController(AdminService adminService) {
//        this.adminService = adminService;
//    }

    @PostMapping("/udhar/customer")
    public UdharCustomer saveCustomer(@RequestBody UdharCustomer customer) {
        return adminService.saveCustomer(customer);
    }
 // Changes in url
    @GetMapping("/udhar/customers")
    public List<UdharCustomer> getAllCustomers() {
        return adminService.getAllCustomers();
    }

    // We have to check this method before
     @PostMapping("/udhar/generate-code")
      public UdharCode generateCode(@RequestParam Long userId, @RequestParam Double amount) {
    return adminService.generateUdharCode(userId, amount);
}


    /*
         ORDER-SERVICE calls this
         temporary OTP validation
       */
    @PostMapping("/udhar/validate")
    public Boolean validateUdhar(
            @RequestParam Long userId,
            @RequestParam String code,
            @RequestParam Double amount
    ) {
        return adminService.validateUdhar(userId, code, amount);
    }

    /*
      owner manually increases customer limit
    */
    @PutMapping("/udhar/update-limit")
    public UdharCustomer updateLimit(
            @RequestParam Long userId,
            @RequestParam Double newLimit
    ) {
        return adminService.updateCreditLimit(userId, newLimit);
    }

    /*
      owner marks payment received
    */
    @PutMapping("/udhar/payment-received")
    public UdharCustomer paymentReceived(
            @RequestParam Long userId,
            @RequestParam Double amount
    ) {
        return adminService.markPaymentReceived(userId, amount);
    }

    /*
      owner sees customer history
    */
    @GetMapping("/udhar/transactions/{userId}")
    public List<UdharTransaction> getTransactions(
            @PathVariable Long userId
    ) {
        return adminService.getTransactions(userId);
    }
}
