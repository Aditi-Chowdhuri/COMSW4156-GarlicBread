package com.garlicbread.includify.entity.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.garlicbread.includify.entity.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class UserCategoryTest {

    private UserCategory userCategory;
    private User mockUser1;
    private User mockUser2;
    private Resource mockResource1;
    private Resource mockResource2;

    @BeforeEach
    public void setUp() {
        userCategory = new UserCategory();
        mockUser1 = mock(User.class);
        mockUser2 = mock(User.class);
        mockResource1 = mock(Resource.class);
        mockResource2 = mock(Resource.class);

        when(mockUser1.getId()).thenReturn("user1");
        when(mockUser2.getId()).thenReturn("user2");

        when(mockResource1.getId()).thenReturn("resource1");
        when(mockResource2.getId()).thenReturn("resource2");

        userCategory.setUsers(Arrays.asList(mockUser1, mockUser2));
        userCategory.setResources(Arrays.asList(mockResource1, mockResource2));

        userCategory.setTitle("Test Category");
        userCategory.setDescription("Description for test category");
    }

    @Test
    public void testGetUserIds() {
        List<String> userIds = userCategory.getUserIds();
        assertNotNull(userIds);
        assertEquals(2, userIds.size());
        assertTrue(userIds.contains("user1"));
        assertTrue(userIds.contains("user2"));
    }

    @Test
    public void testGetResourceIds() {
        List<String> resourceIds = userCategory.getResourceIds();
        assertNotNull(resourceIds);
        assertEquals(2, resourceIds.size());
        assertTrue(resourceIds.contains("resource1"));
        assertTrue(resourceIds.contains("resource2"));
    }

    @Test
    public void testPreRemoveWithUsers() {
        try {
            userCategory.preRemove();
            fail("Expected IllegalStateException when deleting user category with existing users");
        } catch (IllegalStateException e) {
            assertEquals("Cannot delete user category with existing users.", e.getMessage());
        }
    }

    @Test
    public void testPreRemoveWithoutUsers() {
        userCategory.setUsers(Arrays.asList());
        try {
            userCategory.preRemove();
        } catch (IllegalStateException e) {
            fail("Unexpected IllegalStateException thrown when deleting category without users.");
        }
    }
}
