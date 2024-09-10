package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User handleCreateUser(User newUser) {
        String hashPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashPassword);
        return userRepository.save(newUser);
    }

    public void handleDeleteUser(long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> handleFetchUserById(long id) {
        return userRepository.findById(id);
    }

    public List<User> fetchAllUser() {
        return userRepository.findAll();
    }

    public User handleUpdateUser(User user) {
        Optional<User> updatedUserOptional = userRepository.findById(user.getId());
        if (updatedUserOptional.isPresent()) {
            User updatedUser = updatedUserOptional.get();
            // update
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());

            return userRepository.save(updatedUser);
        }
        return null;
    }

    public Optional<User> handleFetchUserbyUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
}
