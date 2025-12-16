package com.ecommerce.project.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @NotBlank
    @Size(max=20)
    @Column(name="username")
    private String userName;

    @NotBlank
    @Size(max=120)
    @Column(name="password")
    private String password;

    @NotBlank
    @Size(max=50)
    @Email
    @Column(name="email")
    private String email;

    public User(String userName,String password,String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    @ManyToMany(cascade={CascadeType.MERGE, CascadeType.PERSIST},fetch=FetchType.EAGER)
    @JoinTable(
        name="user_roles",
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="role_id")
    )
    @Getter
    @Setter
    private Set<Role> roles=new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy="user", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval=true
    )
    private Set<Product> products=new HashSet<>();

    @Getter
    @Setter
    @ManyToMany(cascade={CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
        name="user_addresses",
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="address_id")
    )
    private Set<Address> addresses=new HashSet<>();

}
