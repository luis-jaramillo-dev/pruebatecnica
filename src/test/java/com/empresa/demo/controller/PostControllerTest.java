package com.empresa.demo.controller;

import com.empresa.demo.dto.PostDto;
import com.empresa.demo.network.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostControllerTest {

    private PostController postController;
    private PostService mockPostService;

    @BeforeEach
    void setUp() {
        mockPostService = mock(PostService.class);
        postController = new PostController(mockPostService);
    }

    @Test
    void testGetPosts_Success() {
        // Given
        List<PostDto> mockPosts = Arrays.asList(
            new PostDto(1L, 1L, "Test Title 1", "Test Body 1"),
            new PostDto(1L, 2L, "Test Title 2", "Test Body 2")
        );
        when(mockPostService.getPosts()).thenReturn(mockPosts);

        // When
        ResponseEntity<List<PostDto>> response = postController.getPosts();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Test Title 1", response.getBody().get(0).getTitle());
        assertEquals("Test Body 2", response.getBody().get(1).getBody());
        
        verify(mockPostService, times(1)).getPosts();
    }

    @Test
    void testGetPosts_EmptyList() {
        // Given
        when(mockPostService.getPosts()).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<PostDto>> response = postController.getPosts();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        
        verify(mockPostService, times(1)).getPosts();
    }

    @Test
    void testGetPosts_ServiceException() {
        // Given
        when(mockPostService.getPosts()).thenThrow(new RuntimeException("Service error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            postController.getPosts();
        });
        
        verify(mockPostService, times(1)).getPosts();
    }
} 