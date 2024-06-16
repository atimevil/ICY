package com.sparta.icy.service;

import com.sparta.icy.aspect.LoggingAspect;
import com.sparta.icy.dto.SignoutRequestDto;
import com.sparta.icy.dto.SignupRequestDto;
import com.sparta.icy.entity.User;
import com.sparta.icy.entity.UserStatus;
import com.sparta.icy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private LogService logService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void signup() {
        //given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("testuser1234");
        requestDto.setPassword("Password123!");
        requestDto.setNickname("testnickname");
        requestDto.setIntro("testintro");
        requestDto.setEmail("testuser@example.com");

        User savedUser = new User(requestDto.getUsername(), requestDto.getNickname(), "Password123!", requestDto.getEmail(), requestDto.getIntro(), UserStatus.IN_ACTION);
        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        //when
        userService.signup(requestDto);

        //then
        verify(userRepository).save(any(User.class));

        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(savedUser));
        assertThat(savedUser.getUsername()).isEqualTo(requestDto.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(requestDto.getPassword());
        assertThat(savedUser.getNickname()).isEqualTo(requestDto.getNickname());
        assertThat(savedUser.getEmail()).isEqualTo(requestDto.getEmail());
        assertThat(savedUser.getIntro()).isEqualTo(requestDto.getIntro());
        assertThat(savedUser.getStatus()).isEqualTo("정상");
    }

    @Test
    void signout() {
        //given
        User user = new User();
        user.setUsername("testuser1234");
        user.setPassword("Password123!");
        user.setNickname("testnickname");
        user.setEmail("testuser@example.com");
        user.setStatus(String.valueOf(UserStatus.IN_ACTION));

        SignoutRequestDto requestDto = new SignoutRequestDto();
        requestDto.setPassword("Password123!");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);

        //when
        boolean result = userService.signout(user.getUsername(), requestDto);

        //then
        assertThat(result).isTrue();
        verify(userRepository).save(any(User.class));
        verify(logService).addLog(user.getUsername(), "탈퇴");
    }

    @Test
    void getUser() {
    }

    @Test
    void updateUser() {
    }


    @Test
    void logout() {
    }
}