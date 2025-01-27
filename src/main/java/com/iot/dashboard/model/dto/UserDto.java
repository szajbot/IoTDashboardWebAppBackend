//package com.iot.DashoboardBackend.model.dto;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import lombok.Getter;
//import lombok.Setter;
//import org.keycloak.representations.idm.UserRepresentation;
//
//@Getter
//@Setter
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class UserDto {
//    private String id;
//    private String username;
//    private String password;
//    private String firstName;
//    private String lastName;
//    private String email;
//
//    public UserDto createFromRepresentation(UserRepresentation userRepresentation) {
//        UserDto userDto = new UserDto();
//        userDto.setId(userRepresentation.getId());
//        userDto.setUsername(userRepresentation.getUsername());
//        userDto.setFirstName(userRepresentation.getFirstName());
//        userDto.setLastName(userRepresentation.getLastName());
//        userDto.setEmail(userRepresentation.getEmail());
//        return userDto;
//    }
//}
