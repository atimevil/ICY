package com.sparta.icy.service;

import com.sparta.icy.dto.SignoutRequestDto;
import com.sparta.icy.dto.SignupRequestDto;
import com.sparta.icy.dto.UserProfileResponse;
import com.sparta.icy.dto.UserUpdateRequest;
import com.sparta.icy.entity.User;
import com.sparta.icy.entity.UserStatus;
import com.sparta.icy.error.AlreadySignedOutUserCannotBeSignoutAgainException;
import com.sparta.icy.error.DuplicateUsernameException;
import com.sparta.icy.repository.UserRepository;
import com.sparta.icy.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

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
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser1234");
        user.setNickname("testnickname");
        user.setEmail("testuser@example.com");
        user.setIntro("testintro");
        user.setStatus(String.valueOf(UserStatus.IN_ACTION));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        UserProfileResponse response = userService.getUser(1L);

        // then
        assertNotNull(response);
        assertEquals("testuser1234", response.getUsername());
        assertEquals("testnickname", response.getNickname());
        assertEquals("testintro", response.getIntro());
        assertEquals("testuser@example.com", response.getEmail());
    }

    @Test
    void updateUser() {
        //given
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("testuser1234");
        currentUser.setPassword("Password123!");
        currentUser.setNickname("testnickname");
        currentUser.setEmail("testuser@example.com");
        currentUser.setIntro("testintro");
        currentUser.setStatus(String.valueOf(UserStatus.IN_ACTION));
        UserDetailsImpl userDetails = new UserDetailsImpl(currentUser);

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setCurrentPassword("Password123!");
        updateRequest.setNewPassword("NewPassword123!");
        updateRequest.setNickname("newnickname");
        updateRequest.setIntro("newintro");

        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
        when(passwordEncoder.matches(updateRequest.getCurrentPassword(), currentUser.getPassword())).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(passwordEncoder.encode(updateRequest.getNewPassword())).thenReturn("newEncodedPassword");
        userRepository.save(currentUser);

        //when
        currentUser = userService.updateUser(1L, updateRequest);

        //then
        assertThat(currentUser.getPassword()).isEqualTo("newEncodedPassword");
        assertThat(currentUser.getNickname()).isEqualTo("newnickname");
        assertThat(currentUser.getIntro()).isEqualTo("newintro");

        verify(userRepository).save(currentUser);
    }

    @Test
    void signup_duplicateUsername() {
        //given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername("testuser1234");
        signupRequestDto.setPassword("Password123!");
        signupRequestDto.setNickname("testnickname");
        signupRequestDto.setIntro("testintro");
        signupRequestDto.setEmail("testuser@example.com");

        when(userRepository.findByUsername(signupRequestDto.getUsername())).thenReturn(Optional.of(new User()));

        //when
        //then
        assertThrows(DuplicateUsernameException.class, () -> userService.signup(signupRequestDto));
    }

    @Test
    void signout_alreadySignedOut() {
        //given
        User user = new User();
        user.setUsername("testuser1234");
        user.setPassword("Password123!");
        user.setNickname("testnickname");
        user.setEmail("testuser@example.com");
        user.setStatus(UserStatus.SECESSION.getStatus());

        SignoutRequestDto requestDto = new SignoutRequestDto();
        requestDto.setPassword("Password123!");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);

        //when
        //then
        assertThrows(AlreadySignedOutUserCannotBeSignoutAgainException.class, () -> userService.signout(user.getUsername(), requestDto));
    }

}