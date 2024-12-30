    package com.DrakeN.trading.Controller;


    import com.DrakeN.trading.Domain.VerificationType;
    import com.DrakeN.trading.Enitty.ForgotPasswordToken;
    import com.DrakeN.trading.Enitty.User;
    import com.DrakeN.trading.Enitty.VerificationCode;
    import com.DrakeN.trading.Response.Request.ResetPasswordRequest;
    import com.DrakeN.trading.Response.Response.ApiResponse;
    import com.DrakeN.trading.Response.Response.AuthResponse;
    import com.DrakeN.trading.Response.Request.ForgotPasswordRequest;
    import com.DrakeN.trading.Service.EmailService;
    import com.DrakeN.trading.Service.ForgotPasswordService;
    import com.DrakeN.trading.Service.UserService;
    import com.DrakeN.trading.Service.VerificationCodeService;
    import com.DrakeN.trading.Ulti.otpUltis;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.UUID;

    @RestController
    @RequestMapping("/api/user")
    public class UserController {


        private final UserService userService;


        private final EmailService emailService;
        private final ForgotPasswordService forgotPasswordService;


        private final VerificationCodeService verificationCodeService;



        public UserController(UserService userService, EmailService emailService, ForgotPasswordService forgotPasswordService, VerificationCodeService verificationCodeService) {
            this.userService = userService;
            this.emailService = emailService;
            this.forgotPasswordService = forgotPasswordService;
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

        @PostMapping("/reset-password/send-otp")
        public ResponseEntity<AuthResponse> sendResetPasswordToken(@RequestBody ForgotPasswordRequest request) throws Exception {

            User user = userService.findUserByEmail(request.getSendTo());
            String otp= otpUltis.generateOtp();
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            ForgotPasswordToken token = forgotPasswordService.findByUserId(user.getId());
            if(token == null){
                token = forgotPasswordService.createToken(user,id,otp,request.getType(), request.getSendTo());
            }

            if(request.getType().equals(VerificationType.EMAIL)){
                emailService.sendVerficationOtpEmail(user.getEmail(), token.getOtp());
            }

            AuthResponse response = new AuthResponse();
            response.setSession(token.getId());
            response.setMessage("Password Reset token send successfully, please check your Email");




            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @PatchMapping("/reset-password/verify-otp")
        public ResponseEntity<ApiResponse> ResetPassword(@RequestHeader("Authorization") String jwt,@RequestParam String id, @RequestBody ResetPasswordRequest request) throws Exception {

            ForgotPasswordToken verificationCode = forgotPasswordService.findById(id);
            boolean isVerify = verificationCode.getOtp().equals(request.getOtp());
            if(isVerify){
                userService.updatePassword(verificationCode.getUser(),request.getPassword());
                forgotPasswordService.deleteToken(verificationCode);
                ApiResponse response = new ApiResponse();
                response.setMessage("Password Reset successfully");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }

            throw new Exception("Invalid Otp, please check your Email again for Otp");

        }

    }
