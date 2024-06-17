package com.sparta.icy.controller;

import com.sparta.icy.dto.NewsfeedDto;
import com.sparta.icy.dto.NewsfeedResponseDto;
import com.sparta.icy.service.NewsfeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class NewsfeedControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NewsfeedService newsfeedService;

    @InjectMocks
    private NewsfeedController newsfeedController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(newsfeedController).build();
    }

    @Test
    @WithMockUser
    public void createNewsfeed() throws Exception {
        //given
        NewsfeedDto newsfeedDto = new NewsfeedDto();
        newsfeedDto.setTitle("Test Title");
        newsfeedDto.setContent("Test Content");

        //when
        mockMvc.perform(post("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Title\",\"content\":\"Test Content\"}"))
                .andExpect(status().isOk());

        //then
        verify(newsfeedService).createNewsfeed(any(NewsfeedDto.class));
    }

    @Test
    public void getNewsfeed() throws Exception {
        //given
        Long id = 1L;
        NewsfeedResponseDto newsfeedResponseDto = new NewsfeedResponseDto();
        newsfeedResponseDto.setId(id);
        newsfeedResponseDto.setTitle("Test Title");
        newsfeedResponseDto.setContent("Test Content");

        when(newsfeedService.getNewsfeed(id)).thenReturn(newsfeedResponseDto);

        //when
        mockMvc.perform(get("/boards/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));

        //then
        verify(newsfeedService).getNewsfeed(id);
    }

    @Test
    @WithMockUser
    public void updateNewsfeed() throws Exception {
        //given
        Long feedId = 1L;
        NewsfeedDto newsfeedDto = new NewsfeedDto();
        newsfeedDto.setTitle("Updated Title");
        newsfeedDto.setContent("Updated Content");

        //when
        mockMvc.perform(put("/boards/{feedId}", feedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\",\"content\":\"Updated Content\"}"))
                .andExpect(status().isOk());

        //then
        verify(newsfeedService).updateNewsfeed(eq(feedId), any(NewsfeedDto.class));
    }

    @Test
    @WithMockUser
    public void deleteNewsfeed() throws Exception {
        //given
        Long feedId = 1L;

        //when
        mockMvc.perform(delete("/boards/{feedId}", feedId))
                .andExpect(status().isOk());

        //then
        verify(newsfeedService).deleteNewsfeed(feedId);
    }

    @Test
    public void getAllNewsfeed() throws Exception {
        //given
        when(newsfeedService.getAllNewsfeed()).thenReturn(Collections.emptyList());

        //when
        mockMvc.perform(get("/boards"))
                .andExpect(status().isOk())
                .andExpect(content().string("먼저 작성하여 소식을 알려보세요!")); //한글 인식이 깨지는 현상이 있음

        //then
        verify(newsfeedService).getAllNewsfeed();
    }
}
