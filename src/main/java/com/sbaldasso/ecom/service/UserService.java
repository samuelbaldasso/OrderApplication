package com.sbaldasso.ecom.service;

import com.sbaldasso.ecom.dto.AddressDTO;
import com.sbaldasso.ecom.dto.UserRequest;
import com.sbaldasso.ecom.dto.UserResponse;
import com.sbaldasso.ecom.model.User;
import com.sbaldasso.ecom.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Get all users
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).toList();
    }

    //Get user by id
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id).map(this::toDTO);
    }

    //Create user
    public UserResponse createUser(User user) {
        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    //Update user
    public UserResponse updateUser(Long id, UserRequest user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setEmail(user.getEmail());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPhone(user.getPhone());
            userRepository.save(existingUser);
            return toDTO(existingUser);
        } else {
            return null;
        }
    }

    //Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserResponse toDTO(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getUserRole());
        userResponse.setUsername(user.getUsername());

        if(user.getAddress() != null){
            userResponse.setAddress(new AddressDTO(
                    user.getAddress().getStreet(),
                    user.getAddress().getCity(),
                    user.getAddress().getState(),
                    user.getAddress().getZipcode(),
                    user.getAddress().getCountry()
            ));
        }
        return userResponse;
    }

    private UserRequest toDTO(User userRequest, UserRequest user){
        userRequest.setEmail(user.getEmail());
        userRequest.setFirstName(user.getFirstName());
        userRequest.setLastName(user.getLastName());
        userRequest.setPhone(user.getPhone());
        return user;
    }
}
