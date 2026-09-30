package com.gestion.services;

import com.gestion.system.dto.request.create.TaskAssignmentRequest;
import com.gestion.system.dto.response.TaskAssignmentResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.TaskAssignmentMapper;
import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.TaskAssignmentRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskAssignmentServiceImplTest {
     @Mock
    private TaskAssignmentRepository taskAssignmentRepository;
     @Mock
    private TaskAssignmentMapper taskAssignmentMapper;
     @Mock
    private TaskAssignmentHistoryService historyService;
     @Mock
    private ProjectService projectService;
     @Mock
    private TaskService taskService;
     @Mock
    private UserService userService;
     @Mock
    private UserAuthorizationService userAuthorization;

     @InjectMocks
    private TaskAssignmentServiceImpl taskAssignmentService;

    private User userManager;
    private User userMember;
    private Tasks tasksSample;
    private TaskAssignment taskAssignmentSample;
    private TaskAssignmentResponse responseSample;

     @BeforeEach
    void setUp() {
         userManager = new User();
         userManager.setId(10);

         userMember = new User();
         userMember.setId(20);

         tasksSample = new Tasks();
         tasksSample.setId(100);

         taskAssignmentSample = new TaskAssignment();
         taskAssignmentSample.setId(1);

         responseSample = new TaskAssignmentResponse();
         responseSample.setId(1);
     }

     @Nested
     @DisplayName("assign() Test")
    class AssignTest {
          @Test
          @DisplayName("assign: should assign a task to user, audit, save and register inthistory.")
         void assign_ShouldAssignTaskAndRegisterHistory_WhenValidData() {
              TaskAssignmentRequest request = new TaskAssignmentRequest();
              request.setUserId(20);
              request.setTaskId(100);

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userManager);
              when(userService.findUser(20)).thenReturn(userMember);
              when(taskService.findTask(100)).thenReturn(tasksSample);
              when(taskAssignmentMapper.requestToEntity(request, tasksSample, userMember)).thenReturn(taskAssignmentSample);
              when(taskAssignmentRepository.save(taskAssignmentSample)).thenReturn(taskAssignmentSample);
              when(taskAssignmentMapper.toResponse(taskAssignmentSample)).thenReturn(responseSample);

              TaskAssignmentResponse result = taskAssignmentService.assign(10, request);

              assertThat(result).isNotNull();
              assertThat(result.getId()).isEqualTo(1);

              verify(taskAssignmentRepository, times(1)).save(taskAssignmentSample);
              verify(historyService, times(1)).assign(taskAssignmentSample, userManager);
          }
     }

     @Nested
     @DisplayName("unassign() Test")
    class UnassignTest {
          @Test
          @DisplayName("unassing: should throw ResourceNotFoundEXception is data not exist.")
         void unassign_ShouldThrowException_WhenNotFound() {
              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userManager);
              when(taskAssignmentRepository.findById(1)).thenReturn(Optional.empty());

              assertThatThrownBy(() -> taskAssignmentService.unassign(10, 1))
                      .isInstanceOf(ResourceNotFoundException.class);

              verify(historyService, never()).unassign(any(), any());
              verify(taskAssignmentRepository, never()).delete(any());
          }
     }

     @Nested
     @DisplayName("Read Methods Tests")
    class ReadTests {
          @Test
          @DisplayName("getAllByProject: valid authorization, verify project ID if exist.")
         void getAllByProject_ShouldReturnList_WhenValid() {
              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userManager);
              when(taskAssignmentRepository.findAllByProject(30)).thenReturn(List.of(taskAssignmentSample));
              when(taskAssignmentMapper.listResponse(List.of(taskAssignmentSample))).thenReturn(List.of(responseSample));

              List<TaskAssignmentResponse> responseList = taskAssignmentService.getAllByProject(10, 30);

              assertThat(responseList).isNotNull().hasSize(1);
              verify(projectService, times(1)).findProject(30);
              verify(taskAssignmentRepository, times(1)).findAllByProject(30);
          }

          @Test
          @DisplayName("getAllByUser: valid authorization and verify if user ID exist.")
         void getAllByUser_ShouldReturnList_WhenValidData() {
             when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userManager);
             when(userAuthorization.authorizeUser(20, SystemRole.MEMBER)).thenReturn(userMember);
             when(taskAssignmentRepository.findAllByUser(20)).thenReturn(List.of(taskAssignmentSample));
             when(taskAssignmentMapper.listResponse(List.of(taskAssignmentSample))).thenReturn(List.of(responseSample));

             List<TaskAssignmentResponse> responseList = taskAssignmentService.getAllByUser(10,20);

             assertThat(responseList).isNotNull().hasSize(1);
             verify(userAuthorization, times(1)).authorizeUser(20, SystemRole.MEMBER);
             verify(userAuthorization, times(1)).authorizeUser(10, SystemRole.MANAGER);
             verify(taskAssignmentRepository, times(1)).findAllByUser(20);
         }

          @Test
          @DisplayName("getAllByTask: valid authorization and verify if task ID exist.")
         void getAllByTask_ShouldReturnList_WhenValidData() {
              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userManager);
              when(taskAssignmentRepository.findAllByTask(100)).thenReturn(List.of(taskAssignmentSample));
              when(taskAssignmentMapper.listResponse(List.of(taskAssignmentSample))).thenReturn(List.of(responseSample));

              List<TaskAssignmentResponse> responseList = taskAssignmentService.getAllByTask(10, 100);

              assertThat(responseList).isNotNull().hasSize(1);
              verify(taskService, times(1)).findTask(100);
              verify(taskAssignmentRepository, times(1)).findAllByTask(100);
          }
     }

}
