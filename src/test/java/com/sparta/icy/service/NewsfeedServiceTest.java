package com.sparta.icy.service;

import com.sparta.icy.dto.NewsfeedDto;
import com.sparta.icy.dto.NewsfeedResponseDto;
import com.sparta.icy.entity.Newsfeed;
import com.sparta.icy.entity.User;
import com.sparta.icy.repository.NewsfeedRepository;
import com.sparta.icy.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsfeedServiceTest {

    @Mock
    private NewsfeedRepository newsfeedRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private NewsfeedService newsfeedService;

    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setNickname("testnickname");
        user.setEmail("testuser@example.com");

        userDetails = new UserDetailsImpl(user);

        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.isAuthenticated()).thenReturn(true);
        lenient().when(authentication.getPrincipal()).thenReturn(userDetails);    }

    @Test
    void createNewsfeed_Success() {
        // Given
        NewsfeedDto newsfeedDto = new NewsfeedDto();
        newsfeedDto.setTitle("Test Title");
        newsfeedDto.setRecruitmentCount(5);
        newsfeedDto.setContent("Test Content");

        // When
        newsfeedService.createNewsfeed(newsfeedDto);

        // Then
        verify(newsfeedRepository, times(1)).save(any(Newsfeed.class));
    }

    @Test
    void createNewsfeed_EmptyTitle_ThrowsException() {
        // Given
        NewsfeedDto newsfeedDto = new NewsfeedDto();
        newsfeedDto.setTitle("");
        newsfeedDto.setRecruitmentCount(5);
        newsfeedDto.setContent("Test Content");

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> newsfeedService.createNewsfeed(newsfeedDto));
    }

    @Test
    void getNewsfeed_Success() {
        // Given
        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setId(1L);
        newsfeed.setTitle("Test Title");
        newsfeed.setRecruitmentCount(5);
        newsfeed.setContent("Test Content");
        newsfeed.setCreated_at(LocalDateTime.now());
        newsfeed.setUpdated_at(LocalDateTime.now());
        newsfeed.setUser(user);
        newsfeedRepository.save(newsfeed);

        when(newsfeedRepository.findById(1L)).thenReturn(Optional.of(newsfeed));

        // When
        NewsfeedResponseDto responseDto = newsfeedService.getNewsfeed(1L);

        // Then
        assertNotNull(responseDto);
        assertEquals("Test Title", responseDto.getTitle());
    }


    @Test
    void getNewsfeed_NotFound_ThrowsException() {
        // Given
        when(newsfeedRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> newsfeedService.getNewsfeed(1L));
    }

    @Test
    void updateNewsfeed_Success() {
        // Given
        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setId(1L);
        newsfeed.setTitle("Old Title");
        newsfeed.setRecruitmentCount(5);
        newsfeed.setContent("Old Content");
        newsfeed.setCreated_at(LocalDateTime.now());
        newsfeed.setUpdated_at(LocalDateTime.now());
        newsfeed.setUser(user);

        NewsfeedDto newsfeedDto = new NewsfeedDto();
        newsfeedDto.setTitle("New Title");
        newsfeedDto.setRecruitmentCount(10);
        newsfeedDto.setContent("New Content");

        when(newsfeedRepository.findById(1L)).thenReturn(Optional.of(newsfeed));

        // When
        newsfeedService.updateNewsfeed(1L, newsfeedDto);

        // Then
        assertEquals("New Title", newsfeed.getTitle());
        assertEquals(10, newsfeed.getRecruitmentCount());
        assertEquals("New Content", newsfeed.getContent());
        verify(newsfeedRepository, times(1)).save(newsfeed);
    }

    @Test
    void updateNewsfeed_NotFound_ThrowsException() {
        // Given
        NewsfeedDto newsfeedDto = new NewsfeedDto();
        newsfeedDto.setTitle("New Title");
        newsfeedDto.setRecruitmentCount(10);
        newsfeedDto.setContent("New Content");

        when(newsfeedRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> newsfeedService.updateNewsfeed(1L, newsfeedDto));
    }

    @Test
    void deleteNewsfeed_Success() {
        // Given
        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setId(1L);
        newsfeed.setTitle("Test Title");
        newsfeed.setRecruitmentCount(5);
        newsfeed.setContent("Test Content");
        newsfeed.setCreated_at(LocalDateTime.now());
        newsfeed.setUpdated_at(LocalDateTime.now());
        newsfeed.setUser(user);

        when(newsfeedRepository.findById(1L)).thenReturn(Optional.of(newsfeed));

        // When
        newsfeedService.deleteNewsfeed(1L);

        // Then
        verify(newsfeedRepository, times(1)).delete(newsfeed);
    }

    @Test
    void deleteNewsfeed_NotFound_ThrowsException() {
        // Given
        when(newsfeedRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> newsfeedService.deleteNewsfeed(1L));
    }

    @Test
    void getAllNewsfeed_Success() {
        // Given
        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setId(1L);
        newsfeed.setTitle("Test Title");
        newsfeed.setRecruitmentCount(5);
        newsfeed.setContent("Test Content");
        newsfeed.setCreated_at(LocalDateTime.now());
        newsfeed.setUpdated_at(LocalDateTime.now());
        newsfeed.setUser(user);

        when(newsfeedRepository.findAll()).thenReturn(Collections.singletonList(newsfeed));

        // When
        List<NewsfeedResponseDto> responseDtos = newsfeedService.getAllNewsfeed();

        // Then
        assertNotNull(responseDtos);
        assertFalse(responseDtos.isEmpty());
        assertEquals("Test Title", responseDtos.get(0).getTitle());
    }
}
