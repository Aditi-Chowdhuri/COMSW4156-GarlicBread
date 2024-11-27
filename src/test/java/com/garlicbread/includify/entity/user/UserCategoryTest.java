package com.garlicbread.includify.entity.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;

import com.garlicbread.includify.entity.resource.Resource;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link UserCategory} class.
 * This class tests the functionality of the UserCategory entity,
 * including its methods for managing users and resources, and its
 * behavior when deleting categories with or without users or resources.
 */
public class UserCategoryTest {

  private UserCategory userCategory;
  private User mockUser1;
  private User mockUser2;
  private Resource mockResource1;
  private Resource mockResource2;

  /**
   * Set up mock data before each test.
   * Initializes the UserCategory object and mocks necessary dependencies
   * like User and Resource objects.
   */
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
      assertEquals("Cannot delete a user category with existing users or "
          + "with existing resources targeting the same.", e.getMessage());
    }
  }

  @Test
  public void testPreRemoveWithNullUsersAndResources() {
    userCategory.setUsers(null);
    userCategory.setResources(null);

    try {
      userCategory.preRemove();
    } catch (IllegalStateException e) {
      fail("Unexpected IllegalStateException thrown when deleting category with null users and resources.");
    }
  }

  @Test
  public void testPreRemoveWithNonEmptyUsersAndNullResources() {
    // Users non-empty, resources null
    userCategory.setUsers(Arrays.asList(mockUser1));
    userCategory.setResources(null);

    try {
      userCategory.preRemove();
      fail("Expected IllegalStateException when deleting user category with existing users");
    } catch (IllegalStateException e) {
      assertEquals("Cannot delete a user category with existing users or with "
          + "existing resources targeting the same.", e.getMessage());
    }
  }

  @Test
  public void testPreRemoveWithNullUsersAndNonEmptyResources() {
    userCategory.setUsers(null);
    userCategory.setResources(Arrays.asList(mockResource1));

    try {
      userCategory.preRemove();
      fail("Expected IllegalStateException when deleting user category with existing resources");
    } catch (IllegalStateException e) {
      assertEquals("Cannot delete a user category with existing users or with "
          + "existing resources targeting the same.", e.getMessage());
    }
  }

  @Test
  public void testPreRemoveWithoutUsers() {
    userCategory.setUsers(Arrays.asList());
    userCategory.setResources(Arrays.asList());
    try {
      userCategory.preRemove();
    } catch (IllegalStateException e) {
      fail("Unexpected IllegalStateException thrown when deleting category without users.");
    }
  }
  @Test
  public void testGetResourceIdsWhenResourcesIsNull() {
    userCategory.setResources(null);
    List<String> resourceIds = userCategory.getResourceIds();
    assertNotNull(resourceIds, "Resource IDs list should not be null");
    assertTrue(resourceIds.isEmpty(), "Resource IDs list should be empty when resources are null");
  }

  @Test
  public void testGetUserIdsWhenUsersIsNull() {
    userCategory.setUsers(null);
    List<String> userIds = userCategory.getUserIds();
    assertNotNull(userIds, "User IDs list should not be null");
    assertTrue(userIds.isEmpty(), "User IDs list should be empty when users are null");
  }
}
