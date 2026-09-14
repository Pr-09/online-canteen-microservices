package com.OnlineCanteen.AdminService.Service;

import com.OnlineCanteen.AdminService.Entity.UdharCode;
import com.OnlineCanteen.AdminService.Entity.UdharCustomer;
import com.OnlineCanteen.AdminService.Entity.UdharTransaction;
import com.OnlineCanteen.AdminService.Repository.UdharCodeRepository;
import com.OnlineCanteen.AdminService.Repository.UdharCustomerRepository;
import com.OnlineCanteen.AdminService.Repository.UdharTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UdharCustomerRepository udharCustomerRepository;
    @Autowired
    private UdharCodeRepository udharCodeRepository;
    @Autowired
    private UdharTransactionRepository udharTransactionRepository;


    public UdharCustomer saveCustomer(UdharCustomer customer) {
        if (customer.getCreditLimit() == null) {
            customer.setCreditLimit(500.0);
        }
        if (customer.getPendingAmount() == null) {
            customer.setPendingAmount(0.0);
        }
        // Check this isApproved method is put null inside it
        if(customer.isApproved()==false){
            customer.setApproved(true);
        }

        return udharCustomerRepository.save(customer);
    }

    public List<UdharCustomer> getAllCustomers() {
        return udharCustomerRepository.findAll();
    }

    /*
    single use secure OTP
    future replace with QR approval
  */
    // In this Api it need amount also for checking or generating code
    // This hand made
    @Transactional
    public UdharCode generateUdharCode(Long userId, Double amount){
//        UdharCustomer udharUser = udharCustomerRepository.findByUserId(userId);
        UdharCustomer udharUser =
                udharCustomerRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException("Customer not approved"));

        UdharCode udharCode=new UdharCode();
        SecureRandom secureRandom = new SecureRandom();
        int otp=100000+secureRandom.nextInt(900000);
        if(udharUser!=null){
          if(udharUser.getCreditLimit()-udharUser.getPendingAmount()>=amount){
//              Double temp=udharUser.getPendingAmount();
//              udharUser.setPendingAmount(temp+amount);
                udharCode.setUserId(userId);
                udharCode.setCode(otp+"");
                udharCode.setUsed(false);
                udharCode.setCreatedAt(LocalDateTime.now());
                udharCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));
                return udharCodeRepository.save(udharCode);

          }else {
              throw new RuntimeException("Valid User but udhar Amount Limit Exced Admin ");
          }
        }else {
            throw new RuntimeException("User is not ValidUdhar Code user Admin");
        }
    }




    // This method is Generated
//    @Transactional
//    public UdharCode generateUdharCode(Long userId, Double amount) {
//
//        UdharCustomer customer =
//                udharCustomerRepository.findByUserId(userId)
//                        .orElseThrow(() ->
//                                new RuntimeException("Customer not approved for udhar"));
//
//        SecureRandom random = new SecureRandom();
//
//        int otp = 100000 + random.nextInt(900000);
//
//        UdharCode code = new UdharCode();
//
//        code.setUserId(userId);
//        code.setCode(String.valueOf(otp));
//        code.setAmount(amount);
//        code.setUsed(false);
//        code.setCreatedAt(LocalDateTime.now());
//        code.setExpiresAt(LocalDateTime.now().plusMinutes(5));
//
//        return udharCodeRepository.save(code);
//    }






    /*
        validation called by ORDER-SERVICE
      */
    // if Some problem occurs than please check the optional in repository
    @Transactional
    public Boolean validateUdhar(Long userId,
                                 String codeValue,
                                 Double amount) {

        UdharCustomer customer =
                udharCustomerRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException("Customer not approved"));

        if (!customer.isApproved()) {
            throw new RuntimeException("Customer not approved");
        }

        UdharCode code =
                udharCodeRepository.findByCode(codeValue)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid code"));

        /*
          code must belong to same user
        */
        if (!code.getUserId().equals(userId)) {
            throw new RuntimeException("Code belongs to another user");
        }

        /*
          single use only
        */
        if (code.isUsed()) {
            throw new RuntimeException("Code already used");
        }

        /*
          expiry check
        */
        if (code.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Code expired");
        }

        /*
          credit limit check
        */
        double futurePending = customer.getPendingAmount() + amount;

        if (futurePending > customer.getCreditLimit()) {
            throw new RuntimeException("Credit limit exceeded");
        }

        /*
          mark code used
        */
        code.setUsed(true);
        udharCodeRepository.save(code);

        /*
          increase pending amount
        */
        customer.setPendingAmount(futurePending);
        udharCustomerRepository.save(customer);

        /*
          create pending transaction
        */
        UdharTransaction transaction = new UdharTransaction();

        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setStatus("PENDING");
        transaction.setCreatedAt(LocalDateTime.now());

        udharTransactionRepository.save(transaction);

        return true;
    }

    /*
      owner increases limit
    */
    @Transactional
    public UdharCustomer updateCreditLimit(Long userId,
                                           Double newLimit) {

        UdharCustomer customer =
                udharCustomerRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException("Customer not found"));

        customer.setCreditLimit(newLimit);

        return udharCustomerRepository.save(customer);
    }

    /*
      mark payment received
    */

    //This controller for udhar payment recive or not
    @Transactional
    public UdharCustomer markPaymentReceived(Long userId,
                                             Double amount) {

        UdharCustomer customer =
                udharCustomerRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException("Customer not found"));

        double pending = customer.getPendingAmount() - amount;

        if (pending < 0) {
            pending = 0;
        }

        customer.setPendingAmount(pending);

        udharCustomerRepository.save(customer);

        UdharTransaction transaction = new UdharTransaction();

        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setStatus("PAID");
        transaction.setCreatedAt(LocalDateTime.now());

        udharTransactionRepository.save(transaction);

        return customer;
    }

    /*
      owner sees user transaction history
    */
    public List<UdharTransaction> getTransactions(Long userId) {
        return udharTransactionRepository.findByUserId(userId);
    }
}