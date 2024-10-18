package com.garlicbread.includify.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.garlicbread.includify.entity.resource.Resource;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "id", "email" }) })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Age is required")
    private int age;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_categories", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<UserCategory> categories;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String hashedPassword;

    // one user can have many resources
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Resource> resources;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashedPassword(String plainPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.hashedPassword = passwordEncoder.encode(plainPassword);
    }

}