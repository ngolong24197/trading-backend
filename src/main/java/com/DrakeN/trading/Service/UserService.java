package com.DrakeN.trading.Service;

import com.DrakeN.trading.Domain.VerificationType;
import com.DrakeN.trading.Enitty.User;

import java.util.List;

public interface UserService {

    User findUserByEmail(String email) throws Exception;
    User findUserProfileByJwt(String jwt) throws Exception;
    User findUserById(Long userId) throws Exception;
    User enableTwoFactorAuthentication(VerificationType type, String sendTo, User user);

    User updatePassword(User user, String newPassword);

    List<User> findAllUsers();


}
