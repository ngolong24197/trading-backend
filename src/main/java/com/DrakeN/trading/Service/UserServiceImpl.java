package com.DrakeN.trading.Service;

import com.DrakeN.trading.Configuration.JwtProvider;
import com.DrakeN.trading.Domain.VerificationType;
import com.DrakeN.trading.Enitty.TwoFactorAuth;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {




    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new Exception("User not found");

        }
        return user;
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new Exception("User not found");

        }
        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new Exception("User not found with id: " + userId);
        }
        return user.get();
    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType type, String sendTo, User user) {

        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnable(true);
        twoFactorAuth.setSendTo(type);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }


    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);

        return userRepository.save(user);
    }
}
