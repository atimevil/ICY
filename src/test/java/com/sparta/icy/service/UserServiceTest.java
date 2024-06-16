package com.sparta.icy.service;

import com.sparta.icy.aspect.LoggingAspect;
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
    private LoggingAspect loggingAspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        AspectJProxyFactory fac = new AspectJProxyFactory(userService);
        fac.addAspect(loggingAspect);
        userService = fac.getProxy();
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
        verify(userRepository, times(1)).save(any(User.class));

        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(savedUser));
        assertThat(savedUser.getUsername()).isEqualTo(requestDto.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(requestDto.getPassword());
        assertThat(savedUser.getNickname()).isEqualTo(requestDto.getNickname());
        assertThat(savedUser.getEmail()).isEqualTo(requestDto.getEmail());
        assertThat(savedUser.getIntro()).isEqualTo(requestDto.getIntro());
        assertThat(savedUser.getStatus()).isEqualTo("정상");
    }

    @Test
    void getUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void signout() {
    }

    @Test
    void logout() {
    }
}