package com.garlicbread.includify.entity.resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.garlicbread.includify.entity.appointment.Appointment;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.user.UserCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class ResourceTest {

  private Resource resource;

  @BeforeEach
  void setUp() {
    resource = new Resource();
  }

  @Test
  void testGetAppointments_WhenAppointmentsAreNull_ShouldReturnEmptyList() {
    resource.setAppointments(null);
    List<String> appointmentIds = resource.getAppointments();
    assertNotNull(appointmentIds);
    assertTrue(appointmentIds.isEmpty());
  }

  @Test
  void testGetAppointments_WhenAppointmentsExist_ShouldReturnIds() {
    Appointment appointment1 = mock(Appointment.class);
    when(appointment1.getId()).thenReturn("id1");
    Appointment appointment2 = mock(Appointment.class);
    when(appointment2.getId()).thenReturn("id2");

    resource.setAppointments(Arrays.asList(appointment1, appointment2));
    List<String> appointmentIds = resource.getAppointments();

    assertNotNull(appointmentIds);
    assertEquals(2, appointmentIds.size());
    assertEquals(Arrays.asList("id1", "id2"), appointmentIds);
  }

  @Test
  void testGetOrganisation_WhenOrganisationIsSet_ShouldReturnId() {
    Organisation organisation = mock(Organisation.class);
    when(organisation.getId()).thenReturn("org123");

    resource.setOrganisation(organisation);
    assertEquals("org123", resource.getOrganisation());
  }

  @Test
  void testGetResourceType_WhenResourceTypesAreSet_ShouldReturnList() {
    ResourceType type1 = mock(ResourceType.class);
    ResourceType type2 = mock(ResourceType.class);

    resource.setResourceType(Arrays.asList(type1, type2));
    List<ResourceType> resourceTypes = resource.getResourceType();

    assertNotNull(resourceTypes);
    assertEquals(2, resourceTypes.size());
    assertTrue(resourceTypes.contains(type1));
    assertTrue(resourceTypes.contains(type2));
  }

  @Test
  void testGetTargetUserCategory_WhenCategoriesAreSet_ShouldReturnList() {
    UserCategory category1 = mock(UserCategory.class);
    UserCategory category2 = mock(UserCategory.class);

    resource.setTargetUserCategory(Arrays.asList(category1, category2));
    List<UserCategory> targetUserCategories = resource.getTargetUserCategory();

    assertNotNull(targetUserCategories);
    assertEquals(2, targetUserCategories.size());
    assertTrue(targetUserCategories.contains(category1));
    assertTrue(targetUserCategories.contains(category2));
  }

  @Test
  void testGetAndSetTitle_ShouldWorkCorrectly() {
    resource.setTitle("Resource Title");
    assertEquals("Resource Title", resource.getTitle());
  }

  @Test
  void testGetAndSetDescription_ShouldWorkCorrectly() {
    resource.setDescription("A description of the resource.");
    assertEquals("A description of the resource.", resource.getDescription());
  }

  @Test
  void testGetAndSetUsageInstructions_ShouldWorkCorrectly() {
    resource.setUsageInstructions("Some usage instructions.");
    assertEquals("Some usage instructions.", resource.getUsageInstructions());
  }

  @Test
  void testSetAndGetId_ShouldWorkCorrectly() {
    resource.setId("resource-id");
    assertEquals("resource-id", resource.getId());
  }
}

