package ru.renatrenat.fintrack.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_to_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Account> accounts = new HashSet<>();

}
