package com.garlicbread.includify.entity.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.garlicbread.includify.util.Utils;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false)
    @Min(value = 1)
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

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

    public String getPassword() {
        return password;
    }

    public List<UserCategory> getCategories() {
        return categories;
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

    public void setPassword(String plainPassword) {
        this.password = Utils.hashPassword(plainPassword);
    }

    public void setPasswordWithoutHash(String password) {
        this.password = password;
    }

    public void setCategories(List<UserCategory> categories) {
        this.categories = categories;
    }

}