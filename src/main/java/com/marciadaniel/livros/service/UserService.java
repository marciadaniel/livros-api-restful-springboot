package com.marciadaniel.livros.service;

import com.marciadaniel.livros.dtos.CreateUserDto;
import com.marciadaniel.livros.dtos.UpdateUserDto;
import com.marciadaniel.livros.models.Role;
import com.marciadaniel.livros.models.User;
import com.marciadaniel.livros.repositories.RoleRepository;
import com.marciadaniel.livros.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private  final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final  BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UUID createUser(CreateUserDto dto){

        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));

        return userRepository.save(user).getUserId();
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void updateUserById(String userId, UpdateUserDto updateUserDto) {
        var id = UUID.fromString(userId);

        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()) {
            var user = userEntity.get();

            if (updateUserDto.username() != null) {
                user.setUsername(updateUserDto.username());
            }

            if (updateUserDto.password() != null) {
                user.setPassword(updateUserDto.password());
            }

            userRepository.save(user);
        }

    }

    public void deleteById(String userId) {

        var id = UUID.fromString(userId);

        var userExists = userRepository.existsById(id);

        if (userExists) {
            userRepository.deleteById(id);
        }
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(UUID.fromString(id));
    }
}
