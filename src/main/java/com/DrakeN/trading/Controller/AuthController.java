package com.DrakeN.trading.Controller;

import com.DrakeN.trading.Enitty.TwoFactorOTP;
import com.DrakeN.trading.Enitty.WatchList;
import com.DrakeN.trading.Service.EmailService;
import com.DrakeN.trading.Service.TwoFactorOtpService;
import com.DrakeN.trading.Service.WatchListService;
import com.DrakeN.trading.Ulti.otpUltis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.DrakeN.trading.Configuration.JwtProvider;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Repository.UserRepository;
import com.DrakeN.trading.Response.Response.AuthResponse;
import com.DrakeN.trading.Service.CustomUserDetailsService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository, CustomUserDetailsService customUserDetails, TwoFactorOtpService twoFactorOtpService, EmailService emailService) {
        this.userRepository = userRepository;
        this.customUserDetails = customUserDetails;
        this.twoFactorOtpService = twoFactorOtpService;
        this.emailService = emailService;
    }

    @Autowired
    private final CustomUserDetailsService customUserDetails;

    @Autowired
    private final TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private WatchListService watchListService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody User user) throws Exception {


        User existUser = userRepository.findByEmail(user.getEmail());
        if (existUser != null) {
            throw new Exception("This email already registerd under another account");

        }
        User saveUser = new User();
        saveUser.setFullName(user.getFullName());
        saveUser.setEmail(user.getEmail());
        saveUser.setPassword(user.getPassword());

       User newUser =  userRepository.save(saveUser);
       WatchList watchList = watchListService.createWatchList(newUser);


        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setStatus(true);
        response.setMessage("Register successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {


        Authentication authentication = authenticate(user.getEmail(), user.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        User checkuser = userRepository.findByEmail(user.getEmail());


        if (checkuser.getTwoFactorAuth().isEnable()) {
            AuthResponse response = new AuthResponse();
            response.setMessage("Two-factor authentication required");
            response.setTwoFactorAuthEnabled(true);
            String otp = otpUltis.generateOtp();

            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(checkuser.getId());
            if (oldTwoFactorOTP != null) {
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }
            TwoFactorOTP newTwoFactorOTP = twoFactorOtpService.createTwoFactorOtp(checkuser, otp, jwt);

            emailService.sendVerficationOtpEmail(user.getEmail(), otp);

            response.setSession(newTwoFactorOTP.getId());
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setStatus(true);
        response.setMessage("Login successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        if (!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }


    @PostMapping("/verify-otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOtp(@PathVariable String otp, @RequestParam String verificationId) throws Exception {

        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(verificationId);

        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP,otp)){
            AuthResponse response = new AuthResponse();
            response.setMessage("Two-factor authentication verified");
            response.setTwoFactorAuthEnabled(true);
            response.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        throw new Exception("Invalid Otp, please check your email again for Otp");

    }

}
