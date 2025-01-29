package com.example.restful.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactReponse {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

}
