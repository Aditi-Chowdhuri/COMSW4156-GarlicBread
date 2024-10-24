package com.garlicbread.includify.entity.resource.types;

import com.garlicbread.includify.entity.resource.Resource;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Represents a tool resource in the system, associated with a specific resource.
 * Each ResourceTool entity can be mapped to only one Resource.
 */
@Entity
@Table(name = "resource_tool")
public class ResourceTool {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  // one resource_tool can be mapped to only one resource
  @OneToOne(cascade = CascadeType.REMOVE)
  @MapsId
  @JoinColumn(name = "id")
  private Resource resource;

  @Column(nullable = false)
  private int count;
}

