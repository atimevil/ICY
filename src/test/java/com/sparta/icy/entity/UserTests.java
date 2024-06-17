package com.sparta.icy.entity;

import com.sparta.icy.dto.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {

    @Test
    public void testUserUpdate() {
        //given
        UserStatus activeStatus = UserStatus.IN_ACTION; // Assuming UserStatus is an enum
        User user = new User("john_doe", "John", "password", "john.doe@example.com", "Intro text", activeStatus);

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setNickname("Johnny");
        updateRequest.setIntro("Updated intro");
        updateRequest.setNewPassword("newPassword");

        //when
        user.update(updateRequest);

        //then
        assertEquals("Johnny", user.getNickname());
        assertEquals("Updated intro", user.getIntro());
        assertEquals("newPassword", user.getPassword());
    }
}
