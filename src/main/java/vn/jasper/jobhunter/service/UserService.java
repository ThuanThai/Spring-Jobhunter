package vn.jasper.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.jasper.jobhunter.domain.User;
import vn.jasper.jobhunter.domain.dto.Meta;
import vn.jasper.jobhunter.domain.dto.ResCreateUserDTO;
import vn.jasper.jobhunter.domain.dto.ResultPaginationDTO;
import vn.jasper.jobhunter.repository.UserRepository;

import java.util.Optional;

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

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> userPage = userRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();

        Meta meta = new Meta();
        meta.setCurrent(pageable.getPageNumber());
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(userPage.getTotalPages());
        meta.setTotal(userPage.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(userPage.getContent());

        return rs;
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

    public Optional<User> handleFetchUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public Boolean isEmailExisted(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }
}
