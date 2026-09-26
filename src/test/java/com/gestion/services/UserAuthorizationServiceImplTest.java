package com.gestion.services;

import com.gestion.system.exceptions.InvalidDeletionUserException;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.exceptions.UnauthorizedOperationException;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.UsersRepository;
import com.gestion.system.service.UserAuthorizationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAuthorizationServiceImplTest {

     @Mock
    private UsersRepository usersRepository;

     @InjectMocks
    private UserAuthorizationServiceImpl authorizationService;

     @Nested
     @DisplayName("authorize() Test")
    class AuthorizeTest{

         @Test
         @DisplayName("authorize: shouldn't throw exception when user has higher or equal required role.")
        void authorize_SufficientAuthority_DoesNotThrowException(){
             User requester = new User();
             requester.setSystemRole(SystemRole.ADMIN);

             assertDoesNotThrow(() -> authorizationService.authorize(requester, SystemRole.MANAGER));
             assertDoesNotThrow(() -> authorizationService.authorize(requester, SystemRole.ADMIN));

         }

         @Test
         @DisplayName("authorize: throws Exception when user haven't required rol.")
        void authorize_InsufficientAuthority_ThrowsExceptions(){
             User requester = new User();
             requester.setSystemRole(SystemRole.MEMBER);

             assertThrows(UnauthorizedOperationException.class,
                     () -> authorizationService.authorize(requester, SystemRole.ADMIN));
         }
     }

     @Nested
     @DisplayName("authorizeUser() Test")
    class AuthorizeUser {

         @Test
         @DisplayName("authorizeUser: find by ID, call to authorize() and if exits and has authorization return this user.")
         void authorizeUser_UserExistsAndAuthorized_ReturnUser() {
             User user = new User();
             user.setId(12);
             user.setSystemRole(SystemRole.ADMIN);

             when(usersRepository.findById(12)).thenReturn(Optional.of(user));
             User response = authorizationService.authorizeUser(user.getId(), SystemRole.ADMIN);

             assertNotNull(response);
             assertEquals(12, response.getId());
             verify(usersRepository, times(1)).findById(12);
         }

          @Test
          @DisplayName("authorizeUser: Throw ResourceNotFoundException when user doesn't exist in DB.")
         void authorizeUser_UserNotFound_ThrowsException(){
             Integer id = 20;

             when(usersRepository.findById(id)).thenReturn(Optional.empty());

             assertThrows(ResourceNotFoundException.class,
                     () -> authorizationService.authorizeUser(id, SystemRole.ADMIN));

             verify(usersRepository, times(1)).findById(id);
         }

          @Test
          @DisplayName("authorizeUser: throw UnauthorizedOperationException if user exits but don't have rol requested.")
         void authorizeUser_UserExistsButUnauthorized_ThrowsException(){
             Integer id = 90;
             User user = new User();
             user.setId(id);
             user.setSystemRole(SystemRole.MEMBER);

             when(usersRepository.findById(id)).thenReturn(Optional.of(user));

             assertThrows(UnauthorizedOperationException.class,
                     () -> authorizationService.authorizeUser(id, SystemRole.ADMIN));
             verify(usersRepository, times(1)).findById(id);
         }
    }

     @Nested
     @DisplayName("validateDeleteUserPermission Test")
    class ValidateDeleteUserPermission {

         @Test
         @DisplayName("validateDeleteUserPermission: shouldn't throw exception id users is ROOT/ADMIN and higher rol than target.")
        void validateDeleteUserPermission_HigherPrivilegeActor_Succeeds() {
             User actingUser = new User();
             User targetUser = new User();
             actingUser.setSystemRole(SystemRole.ROOT);
             targetUser.setSystemRole(SystemRole.ADMIN);

             assertDoesNotThrow(() -> authorizationService.validateDeleteUserPermission(targetUser, actingUser));
         }

         @Test
         @DisplayName("validateDeleteUserPermission: throw InvalidDeletionUserException if user try to delete a equal or higher rol.")
        void validateDeleteUserPermission_EqualOrLowerPermission_ThrowsExceptions() {
             User actingUser = new User();
             User targetUser = new User();
             User adminRol = new User();
             actingUser.setSystemRole(SystemRole.ROOT);
             targetUser.setSystemRole(SystemRole.ROOT);
             adminRol.setSystemRole(SystemRole.ADMIN);

             assertThrows(InvalidDeletionUserException.class,
                     () -> authorizationService.validateDeleteUserPermission(targetUser, actingUser));

             assertThrows(InvalidDeletionUserException.class,
                     () -> authorizationService.validateDeleteUserPermission(targetUser, adminRol));
        }

          @Test
          @DisplayName("validateDeleteUserPermission: throw UnauthorizedOperationException if user isn't ADMIN or higher rol.")
        void validateDeleteUserPermission_NonAdminActor_ThrowsUnauthorizedException() {
             User actingUser = new User();
             User targetUser = new User();
             actingUser.setSystemRole(SystemRole.MEMBER);
             targetUser.setSystemRole(SystemRole.MEMBER);

             assertThrows(UnauthorizedOperationException.class,
                     () -> authorizationService.validateDeleteUserPermission(targetUser, actingUser));
         }

    }

}
