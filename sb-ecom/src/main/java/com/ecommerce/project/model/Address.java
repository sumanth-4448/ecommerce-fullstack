package com.ecommerce.project.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name="addresses")
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min=5, message="Street should have at least 5 characters")
    private String street;

    @NotBlank
    @Size(min=5, message="Building name should have at least 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min=3, message="City should have at least 3 characters")
    private String city;

    @NotBlank
    @Size(min=3, message="State should have at least 3 characters")
    private String state;

    @NotBlank
    @Size(min=3, message="Country should have at least 3 characters")
    private String country;

    @NotBlank
    @Size(min=4, message="Pin code should have at least 4 characters")
    private String pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users=new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String country,
            String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }

}
