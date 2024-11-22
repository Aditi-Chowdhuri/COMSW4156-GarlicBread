package com.garlicbread.includify.entity.resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

class ResourceTypeTest {

  @Mock
  private Resource mockResource;

  private ResourceType resourceType;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    resourceType = new ResourceType();
    resourceType.setTitle("Test Resource");
    resourceType.setDescription("Test Description");
  }

  @Test
  void testPreRemove_WithAssociatedResources_ShouldThrowException() {
    List<Resource> resources = new ArrayList<>();
    resources.add(mockResource);
    resourceType.setResources(resources);
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      resourceType.preRemove();
    });
    assertEquals("Cannot delete resource type with existing resources.", exception.getMessage());
  }

  @Test
  void testPreRemove_WithoutAssociatedResources_ShouldNotThrowException() {
    resourceType.setResources(new ArrayList<>());
    assertDoesNotThrow(() -> resourceType.preRemove());
  }

  @Test
  void testGetResourceIds_WithResources_ShouldReturnResourceIds() {
    List<Resource> resources = new ArrayList<>();
    when(mockResource.getId()).thenReturn("resource1");
    resources.add(mockResource);
    resourceType.setResources(resources);
    List<String> resourceIds = resourceType.getResourceIds();
    assertNotNull(resourceIds);
    assertEquals(1, resourceIds.size());
    assertEquals("resource1", resourceIds.get(0));
  }

  @Test
  void testGetResourceIds_WithoutResources_ShouldReturnEmptyList() {
    resourceType.setResources(new ArrayList<>());
    List<String> resourceIds = resourceType.getResourceIds();
    assertNotNull(resourceIds);
    assertTrue(resourceIds.isEmpty());
  }

  @Test
  void testSetAndGetTitle() {
    resourceType.setTitle("New Title");
    assertEquals("New Title", resourceType.getTitle());
  }

  @Test
  void testSetAndGetDescription() {
    resourceType.setDescription("New Description");
    assertEquals("New Description", resourceType.getDescription());
  }
}
