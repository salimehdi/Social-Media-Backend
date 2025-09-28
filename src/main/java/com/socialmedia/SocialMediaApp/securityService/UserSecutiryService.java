package com.socialmedia.SocialMediaApp.securityService;

import com.socialmedia.SocialMediaApp.entities.User;
import com.socialmedia.SocialMediaApp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "userSecurityService")
public class UserSecutiryService {

    private UserRepository userRepository;

    public UserSecutiryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        org.springframework.security.core.userdetails.User authenticatedUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        return userRepository.findByUsername(authenticatedUser.getUsername());
    }

    public boolean canDeleteUser(String userId) {
        Optional<User> user = getUser();
        if (user.isEmpty()) {
            return false;
        }
        return user.get().getId().equals(userId);
    }

    public boolean canUpdateUser(String userId) {
        Optional<User> user = getUser();
        if (user.isEmpty()) {
            return false;
        }
        return user.get().getId().equals(userId);
    }

    public boolean restrictUserItself(String userId){
        Optional<User> user = getUser();
        if (user.isEmpty()) {
            return false;
        }
        return !(user.get().getId().equals(userId));
    }

    public boolean canViewProfile(String userId){
        Optional<User> user = getUser();
        if (user.isEmpty()) {
            return false;
        }
        User profileUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Auth User Id = "+ user.get().getId()+ " and userId = "+ userId);
        if(user.get().getId().equals(profileUser.getId())) return true;
        else if(profileUser.getBlockedUsers().contains(user.get())) return false;
        else if(!profileUser.isPrivate()) return true;
        else return profileUser.getFollowers().contains(user.get());
    }

}
