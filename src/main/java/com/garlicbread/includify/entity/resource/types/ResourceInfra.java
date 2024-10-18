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

@Entity
@Table(name = "resource_infra")
public class ResourceInfra {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  // one resource_infra can be mapped to only one resource
  @OneToOne(cascade = CascadeType.REMOVE)
  @MapsId
  @JoinColumn(name = "id")
  private Resource resource;

  @Column(nullable = false)
  private String latitude;

  @Column(nullable = false)
  private String longitude;

  @Column(nullable = false)
  private String address;
}

