package com.gestion.system.mappers.request;

import com.gestion.system.dto.request.create.UserRequest;
import com.gestion.system.dto.response.UserResponse;
import com.gestion.system.model.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    private final ModelMapper mapper;

    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public UserResponse toResponse(User user) {
        if (user == null) {return null;}
        UserResponse dto = mapper.map(user, UserResponse.class);
        // Los apellido se concadenan porque la Response solo manejan un campo "lastName"
        dto.setLastNames(
                user.getPLastName()+ " " + user.getMLastName());
        return dto;
    }

    public User requestToEntity (UserRequest dto){
    //El Mapper no maneja de forma correcta los campos compuestos, por eso se hizo manualmente
        if (dto == null) {return null;}
        User user = new User();
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setPLastName(dto.getFirstLastName());
        user.setMLastName(dto.getSecondLastName());
        user.setEmail(dto.getEmail());
        return user;
    }


    public List<User> listRequest (List<UserRequest> dtos){
        // Se regresa lista  vacia para evitar el NullPointer, en la serealización JSON
        if (dtos == null) return List.of();
        return dtos.stream().map(this::requestToEntity).toList();
    }

    public List<UserResponse> listResponse (List<User> users){
        if (users == null) return List.of();
        return users.stream().map(this::toResponse).toList();
    }

}
