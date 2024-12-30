    package com.DrakeN.trading.Controller;


    import com.DrakeN.trading.Domain.VerificationType;
    import com.DrakeN.trading.Enitty.User;
    import com.DrakeN.trading.Enitty.VerificationCode;
    import com.DrakeN.trading.Service.EmailService;
    import com.DrakeN.trading.Service.UserService;
    import com.DrakeN.trading.Service.VerificationCodeService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/user")
    public class UserController {


        private final UserService userService;


        private final EmailService emailService;


        private final VerificationCodeService verificationCodeService;

        private String jwt;

        public UserController(UserService userService, EmailService emailService, VerificationCodeService verificationCodeService) {
            this.userService = userService;
            this.emailService = emailService;
            this.verificationCodeService = verificationCodeService;
        }


        @GetMapping("/profile")
        public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
            User user = userService.findUserProfileByJwt(jwt);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }



        @PostMapping("/verification/{verificationType}/send-otp")
        public ResponseEntity<String> sendVerification(@RequestHeader("Authorization") String jwt, @PathVariable VerificationType verificationType) throws Exception {


            User user = userService.findUserProfileByJwt(jwt);
            VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
            if(verificationCode == null){
                     verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);

            }
            if(verificationType.equals(VerificationType.EMAIL)){
                emailService.sendVerficationOtpEmail(user.getEmail(), verificationCode.getOtp());
            }

            return new ResponseEntity<>("OTP send successfully, please check your Email", HttpStatus.OK);
        }

        @PatchMapping("/enable-two-factor/{otp}")
        public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt,
                                                                  @PathVariable String otp) throws Exception {
            User user = userService.findUserProfileByJwt(jwt);
            VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
            String sentTo = verificationCode.getType().equals(VerificationType.EMAIL) ? verificationCode.getEmail():verificationCode.getPhoneNumber();

            boolean isVerify = verificationCode.getOtp().equals(otp);
            if(isVerify){
                User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getType(),sentTo,user);
                verificationCodeService.deleteVerificationCode(verificationCode);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            }

            throw new Exception("Invalid Otp, please check your Email again for Otp");


        }
    }
