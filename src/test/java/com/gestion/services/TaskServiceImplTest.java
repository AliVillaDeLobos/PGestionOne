package com.gestion.services;

import com.gestion.system.dto.audit.TaskAuditModel;
import com.gestion.system.dto.request.create.TaskRequest;
import com.gestion.system.dto.request.update.TaskUpdateRequest;
import com.gestion.system.dto.response.TaskResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.TaskMapper;
import com.gestion.system.mappers.update.TaskUpdateMapper;
import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.Status;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.TasksRepository;
import com.gestion.system.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.config.Task;
import org.springframework.test.context.NestedTestConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
     @Mock
    private TasksRepository tasksRepository;
     @Mock
    private TaskMapper taskMapper;
     @Mock
    private TaskUpdateMapper taskUpdateMapper;
     @Mock
    private ProjectService projectService;
     @Mock
    private AuditService audit;
     @Mock
    private UserAuthorizationService userAuthorization;

     @InjectMocks
    private TaskServiceImpl  taskService;

    private User userManger;
    private User userMember;
    private Tasks taskSample;
    private Projects projectSample;
    private TaskResponse responseSample;

     @BeforeEach
    void setUp() {
         userManger = new User();
         userManger.setId(10);

         userMember = new User();
         userMember.setId(20);

         projectSample = new Projects();
         projectSample.setId(100);

         taskSample = new Tasks();
         taskSample.setId(1);

         responseSample = new TaskResponse();
         responseSample.setId(1);
     }

     @Nested
     @DisplayName("create() Test")
    class CreateTest {
          @Test
          @DisplayName("create: find project, save and audit when user have MANGER rol.")
         void create_ShouldCreateAndAudit_WhenValidData() {
              TaskRequest request =  new TaskRequest();
              TaskAuditModel auditModel = new TaskAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userManger);
              when(projectService.findProject(100)).thenReturn(projectSample);
              when(taskMapper.requestToEntity(request, projectSample)).thenReturn(taskSample);
              when(taskUpdateMapper.toAudit(taskSample)).thenReturn(auditModel);
              when(taskMapper.toResponse(taskSample)).thenReturn(responseSample);

              TaskResponse result = taskService.create(10, request, 100);

              assertThat(result).isNotNull();
              verify(tasksRepository, times(1)).save(taskSample);
              verify(audit, times(1)).create(
                      eq(AuditableEntity.TASKS),
                      eq(1),
                      eq(userManger),
                      eq(auditModel));
          }
     }

     @Nested
     @DisplayName("update() Test")
    class UpdateTest {
          @Test
          @DisplayName("update: should update, audit and authorize MEMBER rol when Task exist.")
         void update_ShouldUpdateAndAudit_WhenValidData() {
              TaskUpdateRequest request = new TaskUpdateRequest();
              TaskAuditModel auditModel = new TaskAuditModel();

              when(userAuthorization.authorizeUser(20, SystemRole.MEMBER)).thenReturn(userMember);
              when(tasksRepository.findByIdAndProjects_Id(1, 100)). thenReturn(Optional.of(taskSample));
              when(taskUpdateMapper.toAudit(taskSample)).thenReturn(auditModel);
              when(taskMapper.toResponse(taskSample)).thenReturn(responseSample);

              TaskResponse result = taskService.update(20, request, 100, 1);

              assertThat(result).isNotNull();
              verify(taskUpdateMapper, times(1)).updateEntity(request, taskSample);
              verify(tasksRepository, times(1)).save(taskSample);
              verify(audit, times(1)).update(
                      eq(AuditableEntity.TASKS),
                      eq(1),
                      eq(userMember),
                      eq(auditModel),
                      eq(auditModel));
          }

          @Test
          @DisplayName("update: should throw ResourceNotFoundException when task is not related with project.")
         void update_ShouldThrowException_WhenTaskNotInProject() {
              TaskUpdateRequest request = new TaskUpdateRequest();

              when(userAuthorization.authorizeUser(20, SystemRole.MEMBER)).thenReturn(userMember);
              when(tasksRepository.findByIdAndProjects_Id(1, 100)).thenReturn(Optional.empty());

              assertThatThrownBy(() -> taskService.update(20, request, 100, 1))
                      .isInstanceOf(ResourceNotFoundException.class);

              verify(tasksRepository, never()).save(any());
          }
     }

     @Nested
     @DisplayName("delete() Test")
    class DeleteTest {
          @Test
          @DisplayName("delete: audit and delete when user has MANAGER rol or higher.")
         void delete_ShouldDelete_WhenValidData() {
              TaskAuditModel auditModel = new TaskAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userManger);
              when(projectService.findProject(100)).thenReturn(projectSample);
              when(tasksRepository.findByIdAndProjects_Id(1, 100)).thenReturn(Optional.of(taskSample));
              when(taskUpdateMapper.toAudit(taskSample)).thenReturn(auditModel);

              taskService.delete(10, 100, 1);

              verify(audit, times(1)).delete(
                      eq(AuditableEntity.TASKS),
                      eq(1),
                      eq(userManger),
                      eq(auditModel));
              verify(tasksRepository, times(1)).delete(taskSample);
          }
     }

     @Nested
     @DisplayName("Read Methods Tests")
    class ReadMethodsTest {
          @Test
          @DisplayName("getByColor: filter by color and project.")
         void getByColor_ShouldReturnList() {
              when(tasksRepository.findAllByProjects_IdAndColor(100, Colors.BLACK)).thenReturn(List.of(taskSample));
              when(taskMapper.listResponse(anyList())).thenReturn(List.of(responseSample));

              List<TaskResponse> result = taskService.getByColor(100, Colors.BLACK);

              assertThat(result).isNotNull().hasSize(1);
              verify(tasksRepository, times(1)).findAllByProjects_IdAndColor(100, Colors.BLACK);
          }

          @Test
          @DisplayName("getByStatus: filter by status and project")
         void getByStatus_ShouldReturnList() {
              when(tasksRepository.findAllByProjects_IdAndStatus(100, Status.PENDING)).thenReturn(List.of(taskSample));
              when(taskMapper.listResponse(anyList())).thenReturn(List.of(responseSample));

              List<TaskResponse> result = taskService.getByStatus(100, Status.PENDING);

              assertThat(result).isNotNull().hasSize(1);
              verify(tasksRepository, times(1)).findAllByProjects_IdAndStatus(100, Status.PENDING);
          }

          @Test
          @DisplayName("activeList: should return active task by current date.")
         void activeList_ShouldPassCurrentDateToRepository() {
              when(tasksRepository.findActiveTask(eq(100), any(LocalDate.class))).thenReturn(List.of(taskSample));
              when(taskMapper.listResponse(anyList())).thenReturn(List.of(responseSample));

              List<TaskResponse> result = taskService.activeList(100);

              assertThat(result).isNotNull().hasSize(1);
              verify(tasksRepository, times(1)).findActiveTask(eq(100), any(LocalDate.class));
          }

          @Test
          @DisplayName("betweenDates: should return tasks between a range of dates.")
         void betweenDates_ShouldReturnList() {
              LocalDate startDate = LocalDate.now().minusDays(5);
              LocalDate end = LocalDate.now();

              when(tasksRepository.findBetweenDates(100, startDate, end)).thenReturn(List.of(taskSample));
              when(taskMapper.listResponse(anyList())).thenReturn(List.of(responseSample));

              List<TaskResponse> result = taskService.betweenDates(100, startDate, end);

              assertThat(result).isNotNull().hasSize(1);
              verify(tasksRepository, times(1)).findBetweenDates(100, startDate, end);
          }

          @Test
          @DisplayName("findTask: should throw ResourceNotFoundEXception when task doesn't exist.")
         void findTask_ShouldThrowException_WhenNotFound() {
              when(tasksRepository.findById(1)).thenReturn(Optional.empty());

              assertThatThrownBy(() -> taskService.findTask(1))
                      .isInstanceOf(ResourceNotFoundException.class);
          }
     }

}
