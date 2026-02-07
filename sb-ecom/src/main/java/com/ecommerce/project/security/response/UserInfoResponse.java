package com.ecommerce.project.security.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class UserInfoResponse {


    private String username;
    private List<String> roles;
    private Long id;


    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.id=id;
        this.username=username;
        this.roles=roles;
    }


}


