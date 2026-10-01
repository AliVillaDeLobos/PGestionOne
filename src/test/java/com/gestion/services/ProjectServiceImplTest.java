package com.gestion.services;

import com.gestion.system.dto.audit.ProjectAuditModel;
import com.gestion.system.dto.request.create.ProjectRequest;
import com.gestion.system.dto.request.update.ProjectUpdateRequest;
import com.gestion.system.dto.response.ProjectResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.exceptions.UnauthorizedOperationException;
import com.gestion.system.mappers.request.ProjectMapper;
import com.gestion.system.mappers.update.ProjectUpdateMapper;
import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.ProjectRepository;
import com.gestion.system.service.AuditService;
import com.gestion.system.service.ProjectServiceImpl;
import com.gestion.system.service.UserAuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {
     @Mock
    private ProjectRepository projectRepository;
     @Mock
    private ProjectMapper projectMapper;
     @Mock
    private ProjectUpdateMapper projectUpdateMapper;
     @Mock
    private UserAuthorizationService userAuthorization;
     @Mock
    private AuditService audit;
     @InjectMocks
    private ProjectServiceImpl projectService;

     @Captor
    private ArgumentCaptor<ProjectAuditModel> auditModelCaptor;
     @Captor
    private ArgumentCaptor<Projects> projectsCaptor;

    private Projects projectSample;
    private ProjectResponse responseSample;
    private User userSample;

     @BeforeEach
    void setUp(){
         projectSample = new Projects();
         projectSample.setId(1);

         responseSample = new ProjectResponse();
         responseSample.setId(1);

         userSample = new User();
         userSample.setId(10);
     }

     @Nested
     @DisplayName("create() Test")
    class CreateTest {

          @Test
          @DisplayName("create: should audit and create project with ADMIN rol.")
         void create_ShouldCreateAndAudit_WhenValidData() {
              ProjectRequest request = new ProjectRequest();
              ProjectAuditModel auditModel = new ProjectAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.ADMIN)).thenReturn(userSample);
              when(projectMapper.requestToEntity(request)).thenReturn(projectSample);
              when(projectRepository.save(any(Projects.class))).thenReturn(projectSample);
              when(projectUpdateMapper.toAudit(projectSample)).thenReturn(auditModel);
              when(projectMapper.toResponse(projectSample)).thenReturn(responseSample);

              ProjectResponse result = projectService.create(request, 10);

              assertThat(result).isNotNull();
              assertThat(result.getId()).isEqualTo(1);

              verify(projectRepository, times(1)).save(any(Projects.class));
              verify(audit, times(1)).create(
                      eq(AuditableEntity.PROJECT),
                      eq(1),
                      eq(userSample),
                      auditModelCaptor.capture());
              assertThat(auditModelCaptor.getValue()).isEqualTo(auditModel);
          }
     }

     @Nested
     @DisplayName("update() Test")
    class UpdateTest {
          @Test
          @DisplayName("update: should update and audit whit MANGER rol or higher.")
         void update_ShouldUpdateAndAudit_WhenExist() {
              ProjectUpdateRequest updateRequest = new ProjectUpdateRequest();
              ProjectAuditModel originalAudit = new ProjectAuditModel();
              ProjectAuditModel updatedAudit = new ProjectAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(projectRepository.findById(1)).thenReturn(Optional.of(projectSample));
              when(projectUpdateMapper.toAudit(projectSample)).thenReturn(originalAudit, updatedAudit);
              when(projectRepository.save(any(Projects.class))).thenReturn(projectSample);
              when(projectMapper.toResponse(projectSample)).thenReturn(responseSample);

              ProjectResponse result = projectService.update(1,updateRequest, 10);

              assertThat(result).isNotNull();
              verify(projectRepository, times(1)).save(any(Projects.class));
              verify(audit, times(1)).update(
                      eq(AuditableEntity.PROJECT),
                      eq(1),
                      eq(userSample),
                      eq(originalAudit),
                      eq(updatedAudit));
          }
     }

     @Nested
     @DisplayName("find and read Test")
    class FindAndReadTest {
          @Test
          @DisplayName("getById: should return mapped entity if exist.")
         void getById_ShouldReturnProject_WhenFound() {
              when(projectRepository.findById(1)).thenReturn(Optional.of(projectSample));
              when(projectMapper.toResponse(projectSample)).thenReturn(responseSample);

              ProjectResponse response = projectService.getById(1);

              assertThat(response).isNotNull();
              assertThat(response.getId()).isEqualTo(1);
          }

          @Test
          @DisplayName("searchByName: should return mapped list of response if exist match and IgnoreCase.")
         void searchByName_ShouldReturnList_WhenMatchesExist() {
              when(projectRepository.findByNameContainsIgnoreCase("gestion")).thenReturn(List.of(projectSample));
              when(projectMapper.listResponse(List.of(projectSample))).thenReturn(List.of(responseSample));

              List<ProjectResponse> responseList = projectService.searchByName("gestion");

              assertThat(responseList).isNotNull().hasSize(1);
          }

          @Test
          @DisplayName("getAll: should return empty list when no data.")
         void getAll_ShouldReturnEmptyList_WhenNoData(){
              when(projectRepository.findAll()).thenReturn(Collections.emptyList());
              when(projectMapper.listResponse(Collections.emptyList())).thenReturn(Collections.emptyList());

              List<ProjectResponse> responseList = projectService.getAll();

              assertThat(responseList).isNotNull().isEmpty();
          }

          @Test
          @DisplayName("findProject: should throw exception when not found.")
         void findProject_ShouldThrowException_WhenNotFound() {
              when(projectRepository.findById(1)).thenReturn(Optional.empty());

              assertThatThrownBy(() -> projectService.getById(1))
                      .isInstanceOf(ResourceNotFoundException.class);
          }
     }

     @Nested
     @DisplayName("delete() Test")
    class DeleteTest {
          @Test
          @DisplayName("delete: should audit and delete with ADMIN user rol or higher.")
         void delete_ShouldAuditAndDelete_WhenExist() {
              ProjectAuditModel auditModel = new ProjectAuditModel();

              when((userAuthorization.authorizeUser(10, SystemRole.ADMIN))).thenReturn(userSample);
              when(projectRepository.findById(1)).thenReturn(Optional.of(projectSample));
              when(projectUpdateMapper.toAudit(projectSample)).thenReturn(auditModel);

              projectService.delete(1, 10);

              verify(projectRepository, times(1)).delete(projectsCaptor.capture());
              assertThat(projectsCaptor.getValue()).isEqualTo(projectSample);

              verify(audit, times(1)).delete(
                      eq(AuditableEntity.PROJECT),
                      eq(1),
                      eq(userSample),
                      eq(auditModel));
          }
     }

}
