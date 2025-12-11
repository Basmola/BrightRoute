package com.brightroute.brightroute.service;
import com.brightroute.brightroute.repository.UserRepository;
import com.brightroute.brightroute.model.User;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private  UserRepository UserRepo;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User register(User user){
    user.setCreatedAt(LocalDateTime.now());  
    if(user.getAccountStatus()==null){
        user.setAccountStatus("Active");
    }
    if(user.getRole()==null){
        user.setRole("Student");
    }
    return  UserRepo.save(user);
    }

    User login(String email, String password){
    User user=UserRepo.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
    if(user.getPasswordHash().equals(password)){
        return user;    
    }
    else{
        throw new RuntimeException("Invalid credentials");
    }
    }

    public User updateProfile(Long id, User updatedUser){
        User user=viewUser(id);
        if(updatedUser.getFirstName()!=null){
            user.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null){
        user.setLastName(updatedUser.getLastName());
        } 
        if (updatedUser.getPhoneNumber() != null){
            user.setPhoneNumber(updatedUser.getPhoneNumber());
        }

        if (updatedUser.getAccountStatus() != null){
            user.setAccountStatus(updatedUser.getAccountStatus());
        }
        return UserRepo.save(user);
    }

    public boolean verifyIdentity(Long userId, String firstName, String lastName, String password){
        Optional<User> optionalUser = UserRepo.findById(userId);
        if(optionalUser.isEmpty()) return false;
        User user = optionalUser.get();
        return user.getFirstName().equals(firstName) &&
                user.getLastName().equals(lastName) &&
                passwordEncoder.matches(password, user.getPasswordHash());
    }
    public User viewUser(Long id){
        return UserRepo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
    }
      public void logout(Long userId) {
        // If you store session tokens or JWT blacklists, invalidate them here.
        // With stateless JWT you typically don't store server-side sessions; this is a placeholder.
        Optional<User> u = UserRepo.findById(userId);
        if (u.isEmpty()) throw new RuntimeException("User not found.");
        // Example: set an "last_logout_at" if you had a column. For now nothing persisted.
        // user.setLastLogoutAt(LocalDateTime.now()); userRepository.save(user);
    }
    }

