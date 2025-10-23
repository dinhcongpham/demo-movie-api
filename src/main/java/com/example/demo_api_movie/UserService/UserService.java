package com.example.demo_api_movie.UserService;


import com.example.demo_api_movie.Exception.BadRequestException;
import com.example.demo_api_movie.Exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHashed(request.getPassword())
                .name(request.getName())
                .avatarUrl(request.getAvatarUrl())
                .build();

        user = userRepository.save(user);
        return toDto(user);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new NotFoundException("User not found")
        );

        return toDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()
            -> new NotFoundException("User not found by email:" + email)
        );

        return toDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(this::toDto).toList();
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()
        -> new NotFoundException("User not found"));

        userRepository.delete(user);
    }

    public UserDto updateUser(UserDto userDto, Long id) {
        if (!Objects.equals(id, userDto.getId())) {
            throw new BadRequestException("Ids are not equal");
        }

        User user = userRepository.findById(userDto.getId()).orElseThrow(()
            -> new NotFoundException("User not found"));

        user.setName(userDto.getName());
        user.setAvatarUrl(userDto.getAvatarUrl());
        userRepository.save(user);

        return toDto(user);
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
