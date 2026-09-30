package com.gestion.services;

import com.gestion.system.dto.audit.UserAuditModel;
import com.gestion.system.dto.request.create.UserRequest;
import com.gestion.system.dto.request.update.ChangePasswordRequest;
import com.gestion.system.dto.request.update.UserUpdateRequest;
import com.gestion.system.dto.response.UserResponse;
import com.gestion.system.exceptions.BusinessRulesExceptions.DuplicateResourceException;
import com.gestion.system.exceptions.PasswordInvalidateException;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.UserMapper;
import com.gestion.system.mappers.update.UserUpdateMapper;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.UsersRepository;
import com.gestion.system.service.AuditService;
import com.gestion.system.service.UserAuthorizationService;
import com.gestion.system.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
     @Mock
    private UsersRepository usersRepository;
     @Mock
    private UserMapper userMapper;
     @Mock
    private UserUpdateMapper userUpdateMapper;
     @Mock
    private PasswordEncoder passwordEncoder;
     @Mock
    private UserAuthorizationService userAuthorization;
     @Mock
    private AuditService auditService;

     @InjectMocks
    private UserServiceImpl userService;

    private User userSample;
    private User userAdmin;
    private UserResponse responseSample;

     @BeforeEach
    void setUp() {
         userSample = new User();
         userSample.setId(10);
         userSample.setEmail("test@mail.com");
         userSample.setPassword("pwd123");

         userAdmin = new User();
         userAdmin.setId(1);

         responseSample = new UserResponse();
         responseSample.setId(20);
     }

     @Nested
     @DisplayName("updatePassword() Test")
    class UpdatePasswordTest {
          @Test
          @DisplayName("updatePassword: save and update new password when data is valid.")
         void updatePassword_ShouldUpdatePassword_WhenValid() {
              ChangePasswordRequest request = new ChangePasswordRequest();
              request.setCurrentPassword("pwd123");
              request.setNewPassword("newpwd123");

              when(usersRepository.findById(10)).thenReturn(Optional.of(userSample));
              when(passwordEncoder.matches("pwd123", "pwd123")).thenReturn(true);
              when(passwordEncoder.encode("newpwd123")).thenReturn("EncodedNewPassword");

              userService.updatePassword(10, request);

              assertThat(userSample.getPassword()).isEqualTo("EncodedNewPassword");
              verify(usersRepository, times(1)).save(userSample);
          }

          @Test
          @DisplayName("updatePassword: should throw when current or new password is blank.")
         void updatePassword_ShouldThrowException_WHenFieldAreBlank() {
              ChangePasswordRequest request = new ChangePasswordRequest();
              request.setCurrentPassword(" ");
              request.setNewPassword("newpwd123");

              when(usersRepository.findById(10)).thenReturn(Optional.of(userSample));

              assertThatThrownBy(() -> userService.updatePassword(10, request))
                      .isInstanceOf(PasswordInvalidateException.class);

              verify(usersRepository, never()).save(any());
          }

          @Test
          @DisplayName("updatePassword: should throw exception when current password isn't match.")
         void updatePassword_ShouldThrowException_WhenCurrentPasswordIsWrong() {
              ChangePasswordRequest request = new ChangePasswordRequest();
              request.setCurrentPassword("pwd123");
              request.setNewPassword("newpwd123");

              when(usersRepository.findById(10)).thenReturn(Optional.of(userSample));
              when(passwordEncoder.matches("pwd123", "pwd123")).thenReturn(false);

              assertThatThrownBy(() -> userService.updatePassword(10, request))
                      .isInstanceOf(PasswordInvalidateException.class);

              verify(usersRepository, never()).save(any());
          }

          @Test
          @DisplayName("updatePassword: should throw exception when new and old password are equals.")
         void updatePassword_ShouldThrowException_WhenNewPasswordEqualsOldPassword() {
              ChangePasswordRequest request = new ChangePasswordRequest();
              request.setCurrentPassword("pwd123");
              request.setNewPassword("pwd123");

              when(usersRepository.findById(10)).thenReturn(Optional.of(userSample));
              when(passwordEncoder.matches("pwd123", "pwd123")).thenReturn(true);

              assertThatThrownBy(() -> userService.updatePassword(10, request))
                      .isInstanceOf(PasswordInvalidateException.class);

              verify(usersRepository, never()).save(any());
          }
     }

     @Nested
     @DisplayName("Update(), create() & delete() Tests")
    class CrudTest {
          @Test
          @DisplayName("create: should authorize ROOT rol, save and audit entity.")
         void create_ShouldCreateAndAudit() {
              UserRequest request = new UserRequest();
              UserAuditModel auditModel = new UserAuditModel();

              when(userAuthorization.authorizeUser(1, SystemRole.ROOT)).thenReturn(userAdmin);
              when(userMapper.requestToEntity(request)).thenReturn(userSample);
              when(usersRepository.save(userSample)).thenReturn(userSample);
              when(userMapper.toResponse(userSample)).thenReturn(responseSample);
              when(userUpdateMapper.toAudit(userSample)).thenReturn(auditModel);

              UserResponse result = userService.create(request, 1, SystemRole.MANAGER);

              assertThat(result).isNotNull();
              assertThat(userSample.getSystemRole()).isEqualTo(SystemRole.MANAGER);

              verify(usersRepository, times(1)).save(userSample);
              verify(auditService, times(1)).create(
                      eq(AuditableEntity.USER),
                      eq(10),
                      eq(userAdmin),
                      eq(auditModel));
          }

          @Test
          @DisplayName("create: should throw exception when email already exist.")
         void create_ShouldThrowException_WhenEmailAlreadyExist() {
              UserRequest request = new UserRequest();
              request.setEmail("test@email.com");

              when(userAuthorization.authorizeUser(1, SystemRole.ROOT)).thenReturn(userAdmin);
              when(usersRepository.existsByEmail("test@email.com")).thenReturn(true);

              assertThatThrownBy(() -> userService.create(request, 1, SystemRole.MANAGER)).
                      isInstanceOf(DuplicateResourceException.class);

              verify(usersRepository, never()).save(any());
              verify(auditService, never()).create(any(), any(), any(), any());
          }

          @Test
          @DisplayName("update: should update and audit entity.")
         void update_ShouldUpdate_WhenValidData() {
              UserUpdateRequest request = new UserUpdateRequest();

              when(usersRepository.findById(10)).thenReturn(Optional.of(userSample));
              when(userUpdateMapper.updateEntity(request, userSample)).thenReturn(userSample);
              when(userMapper.toResponse(userSample)).thenReturn(responseSample);

              UserResponse result = userService.update(10, request);

              assertThat(result).isNotNull();
              verify(usersRepository, times(1)).save(userSample);
          }

          @Test
          @DisplayName("delete: should valid permissions, audit and delete.")
         void delete_ShouldValidatePermissionsAuditAndDelete() {
              UserAuditModel auditModel = new UserAuditModel();

              when(usersRepository.findById(10)).thenReturn(Optional.of(userSample));
              when(usersRepository.findById(1)).thenReturn(Optional.of(userAdmin));
              when(userUpdateMapper.toAudit(userSample)).thenReturn(auditModel);

              userService.delete(10,1);

              verify(userAuthorization, times(1)).validateDeleteUserPermission(userSample, userAdmin);
              verify(auditService, times(1)).delete(
                      eq(AuditableEntity.USER),
                      eq(10),
                      eq(userAdmin),
                      eq(auditModel));
              verify(usersRepository, times(1)).delete(userSample);
          }
    }

     @Nested
     @DisplayName("Read Methods Tests")
    class ReadTests {
          @Test
          @DisplayName("getByEmail: should retun mapped user if exist.")
         void getByEmail_ShouldReturnResponse_WhenUserExist() {
              when(usersRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(userSample));
              when(userMapper.toResponse(userSample)).thenReturn(responseSample);

              UserResponse result = userService.getByEmail("test@mail.com");

              assertThat(result).isNotNull();
              verify(usersRepository, times(1)).findByEmail("test@mail.com");
          }

          @Test
          @DisplayName("getByEmail: should throw exception when email not found.")
         void getByEmail_ShouldThrowException_WHenNotFound() {
              when(usersRepository.findByEmail("notfound@mail")).thenReturn(Optional.empty());

              assertThatThrownBy(() -> userService.getByEmail("notfound@mail")).
                      isInstanceOf(ResourceNotFoundException.class);
          }
     }

}
