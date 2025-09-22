package com.neel.projects.airBnbApp.security;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.neel.projects.airBnbApp.dto.LoginDto;
import com.neel.projects.airBnbApp.dto.SignUpRequestDto;
import com.neel.projects.airBnbApp.dto.UserDto;
import com.neel.projects.airBnbApp.entity.User;
import com.neel.projects.airBnbApp.entity.enums.Role;
import com.neel.projects.airBnbApp.exception.ResourceNotFoundException;
import com.neel.projects.airBnbApp.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    public UserDto signUp(SignUpRequestDto signUpRequestDto){
        User user = userRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);
        if(user!=null){
            throw new RuntimeException("User with email already exists");
        }
        
        User newUser = modelMapper.map(signUpRequestDto,User.class);
        newUser.setRoles(Set.of(Role.GUEST));
        newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        newUser= userRepository.save(newUser);
        return modelMapper.map(newUser,UserDto.class);
    } 

    public String[] login(LoginDto loginDto){
        log.info("Login called");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        User user  = (User) authentication.getPrincipal();
        String[] arr = new String[2];
        arr[0] = jwtService.generateAccessToken(user);
        arr[1] = jwtService.generateRefreshToken(user);
        log.info("after generating tokens");
        return arr;
    }
    public String refreshToken(String refreshToken){
        Long id = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+ id));
        return jwtService.generateAccessToken(user); 
    }
}
