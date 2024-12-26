package com.DrakeN.trading.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DrakeN.trading.Configuration.JwtProvider;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Repository.UserRepository;
import com.DrakeN.trading.Response.AuthResponse;
import com.DrakeN.trading.Service.CustomUserDetailsService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetails;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody User user) throws Exception{
   

        User existUser = userRepository.findByEmail(user.getEmail());
        if(existUser != null){
            throw new Exception("This email already registerd under another account");
            
        }
        User saveUser = new User();
        saveUser.setFullName(user.getFullName());
        saveUser.setEmail(user.getEmail());
        saveUser.setPassword(user.getPassword());

        User newUser = userRepository.save(saveUser);

         Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
          SecurityContextHolder.getContext().setAuthentication(authentication);

          String jwt = JwtProvider.generateToken(authentication);
        
          AuthResponse response = new AuthResponse();
          response.setJwt(jwt);
          response.setStatus(true);
          response.setMessage("Register successfully");

        return new ResponseEntity<>(response,HttpStatus.CREATED);

    }

    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception{
   

       

         Authentication authentication = authenticate(user.getEmail(),user.getPassword());

         SecurityContextHolder.getContext().setAuthentication(authentication);
         
                   String jwt = JwtProvider.generateToken(authentication);
                 
                   AuthResponse response = new AuthResponse();
                   response.setJwt(jwt);
                   response.setStatus(true);
                   response.setMessage("Login successfully");
         
                 return new ResponseEntity<>(response,HttpStatus.CREATED);
         
             }
         
         
             private Authentication authenticate(String email, String password) {
               UserDetails userDetails = customUserDetails.loadUserByUsername(email);
               if(userDetails == null){
                throw new BadCredentialsException("Invalid username");
               }
               if(!password.equals(userDetails.getPassword())){
                throw new BadCredentialsException("Invalid password");
               }
               return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
             }

}
