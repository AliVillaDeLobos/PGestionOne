package com.gestion.system.controller;

import com.gestion.system.dto.request.create.UserCreateRequest;
import com.gestion.system.dto.request.create.UserRequest;
import com.gestion.system.dto.request.update.ChangePasswordRequest;
import com.gestion.system.dto.request.update.UserUpdateRequest;
import com.gestion.system.dto.response.UserResponse;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    //                            Id = 1 del User es temporal hasta agregar SecurityContext
                            private static final Integer TEMP_USER_ID = 1;
    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUser(@PathVariable Integer id) {
        UserResponse response = userService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        UserResponse response = userService.getByEmail(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROOT')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        UserResponse response = userService.create(userCreateRequest, TEMP_USER_ID);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> update(@PathVariable Integer id,
                                               @Valid @RequestBody UserUpdateRequest updateRequest) {

        UserResponse response = userService.update(TEMP_USER_ID, id, updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROOT')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id, TEMP_USER_ID);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/password")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.updatePassword(TEMP_USER_ID, changePasswordRequest);
        return ResponseEntity.noContent().build();
    }


}
