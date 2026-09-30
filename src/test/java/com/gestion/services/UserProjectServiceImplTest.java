package com.gestion.services;

import com.gestion.system.dto.audit.UserProjectAuditModel;
import com.gestion.system.dto.request.create.UserProjectRequest;
import com.gestion.system.dto.response.UserProjectResponse;
import com.gestion.system.exceptions.BusinessRulesExceptions.DuplicateAssignmentException;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.UserProjectMapper;
import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.entities.UserProject;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.UserProjectRepository;
import com.gestion.system.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProjectServiceImplTest {
     @Mock
    private UserProjectRepository userProjectRepository;
     @Mock
    private UserProjectMapper userProjectMapper;
     @Mock
    private ProjectService projectService;
     @Mock
    private UserService userService;
     @Mock
    private AuditService audit;
     @Mock
    private UserAuthorizationService userAuthorization;

     @InjectMocks
    private UserProjectServiceImpl userProjectService;

    private User userAdmin;
    private User userToAssign;
    private Projects projectSample;
    private UserProject userProjectSample;
    private UserProjectResponse responseSample;

     @BeforeEach
    void setUp() {
         userAdmin = new User();
         userAdmin.setId(10);

         userToAssign = new User();
         userToAssign.setId(20);

         projectSample = new Projects();
         projectSample.setId(100);

         userProjectSample = new UserProject();
         userProjectSample.setId(1);

         responseSample = new UserProjectResponse();
         responseSample.setId(1);
     }

     @Nested
     @DisplayName("create() Test")
    class CreateTest {
          @Test
          @DisplayName("create: save adn audit.")
         void create_ShouldSaveAndAudit_WhenAssignmentDoesNotExist() {
              UserProjectRequest request = new UserProjectRequest();
              request.setUserId(20);
              request.setProjectId(100);
              UserProjectAuditModel auditModel = new UserProjectAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.ADMIN)).thenReturn(userAdmin);
              when(userService.findUser(20)).thenReturn(userToAssign);
              when(projectService.findProject(100)).thenReturn(projectSample);
              when(userProjectRepository.existsByUser_IdAndProjects_Id(20, 100)).thenReturn(false);
              when(userProjectMapper.requestToEntity(userToAssign, projectSample)).thenReturn(userProjectSample);
              when(userProjectRepository.save(userProjectSample)).thenReturn(userProjectSample);
              when(userProjectMapper.toAudit(userProjectSample)).thenReturn(auditModel);
              when(userProjectMapper.toResponse(userProjectSample)).thenReturn(responseSample);

              UserProjectResponse result = userProjectService.create(10, request);

              assertThat(result).isNotNull();
              assertThat(result.getId()).isEqualTo(1);

              verify(userProjectRepository, times(1)).save(userProjectSample);
              verify(audit, times(1)).create(
                      eq(AuditableEntity.USER_PROJECT),
                      eq(1),
                      eq(userAdmin),
                      eq(auditModel));
          }

          @Test
          @DisplayName("create: should throw exception when user already have assign to project.")
         void create_ShouldThrowException_WhenAlreadyAssigned() {
              UserProjectRequest request = new UserProjectRequest();
              request.setUserId(20);
              request.setProjectId(100);

              when(userAuthorization.authorizeUser(10, SystemRole.ADMIN)).thenReturn(userAdmin);
              when(userService.findUser(20)).thenReturn(userToAssign);
              when(projectService.findProject(100)).thenReturn(projectSample);
              when(userProjectRepository.existsByUser_IdAndProjects_Id(20, 100)).thenReturn(true);

              assertThatThrownBy(() -> userProjectService.create(10, request))
                      .isInstanceOf(DuplicateAssignmentException.class);

              verify(userProjectRepository, never()).save(any());
              verify(audit, never()).create(any(), any(), any(), any());
          }
     }

     @Nested
     @DisplayName("delete() Test")
    class DeleteTest {
          @Test
          @DisplayName("delete: should audit and delete.")
         void delete_ShouldDeleteAndAudit_WhenExist() {
              UserProjectAuditModel auditModel = new UserProjectAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.ADMIN)).thenReturn(userAdmin);
              when(userProjectRepository.findById(1)).thenReturn(Optional.of(userProjectSample));
              when(userProjectMapper.toAudit(userProjectSample)).thenReturn(auditModel);

              userProjectService.delete(10, 1);

              verify(audit, times(1)).delete(
                      eq(AuditableEntity.USER_PROJECT),
                      eq(1),
                      eq(userAdmin),
                      eq(auditModel));
              verify(userProjectRepository, times(1)).delete(userProjectSample);
          }
     }

     @Nested
     @DisplayName("Read Methods Test")
    class ReadTests {
          @Test
          @DisplayName("getById: shoud authorize ADMIN rol, and return mapped enity.")
         void getById_ShouldReturnResponse_WhenExist() {
              when(userAuthorization.authorizeUser(10, SystemRole.ADMIN)).thenReturn(userAdmin);
              when(userProjectRepository.findById(1)).thenReturn(Optional.of(userProjectSample));
              when(userProjectMapper.toResponse(userProjectSample)).thenReturn(responseSample);

              UserProjectResponse result = userProjectService.getById(10, 1);

              assertThat(result).isNotNull();
              assertThat(result.getId()).isEqualTo(1);
         }

          @Test
          @DisplayName("getAllUserByProjects: found all user with project ID.")
         void getAllUserByProjects_ShouldReturnList() {
              when(userAuthorization.authorizeUser(10, SystemRole.ADMIN)).thenReturn(userAdmin);
              when(userProjectRepository.findAllByProjects_Id(100)).thenReturn(List.of(userProjectSample));
              when(userProjectMapper.listResponse(anyList())).thenReturn(List.of(responseSample));

              List<UserProjectResponse> result = userProjectService.getAllUsersByProjects(10, 100);

              assertThat(result).isNotNull().hasSize(1);
              verify(userProjectRepository, times(1)).findAllByProjects_Id(100);
          }

          @Test
          @DisplayName("findUserProject: should throw excpetion when not found.")
         void findUserProject_ShouldThrowException_WhenNotFound() {
              when(userProjectRepository.findById(1)).thenReturn(Optional.empty());

              assertThatThrownBy(() -> userProjectService.findUserProject(1))
                      .isInstanceOf(ResourceNotFoundException.class);
          }
     }

}

