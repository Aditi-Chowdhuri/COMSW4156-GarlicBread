package com.garlicbread.includify.entity.organisation;

import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.util.Utils;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "organisation", uniqueConstraints = { @UniqueConstraint(columnNames = {"email"}) })
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    private String description;

    @Column(nullable = false)
    @NotNull(message = "Latitude is required")
    private String latitude;

    @Column(nullable = false)
    @NotNull(message = "Longitude is required")
    private String longitude;

    @Column(nullable = false)
    @NotBlank(message = "Address is required")
    private String address;

    // one organisation can have many resources
    @OneToMany(mappedBy = "organisation", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Resource> resources;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String plainPassword) {
        this.password = Utils.hashPassword(plainPassword);
    }

    public void setPasswordWithoutHash(String password) {
        this.password = password;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
