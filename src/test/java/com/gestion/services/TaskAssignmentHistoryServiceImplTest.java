package com.gestion.services;

import com.gestion.system.dto.response.TaskAssignmentHistoryResponse;
import com.gestion.system.mappers.response.TaskAssignmentHistoryMapper;
import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.entities.TaskAssignmentHistory;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.Action;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.TaskAssignmentHistoryRepository;
import com.gestion.system.service.ProjectService;
import com.gestion.system.service.TaskAssignmentHistoryServiceImpl;
import com.gestion.system.service.TaskService;
import com.gestion.system.service.UserAuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskAssignmentHistoryServiceImplTest {
     @Mock
    private TaskAssignmentHistoryRepository historyRepository;
     @Mock
    private TaskAssignmentHistoryMapper historyMapper;
     @Mock
    private TaskService taskService;
     @Mock
    private ProjectService projectService;
     @Mock
    private UserAuthorizationService userAuthorization;

     @InjectMocks
    private TaskAssignmentHistoryServiceImpl historyService;

     private User userAdmin;
     private User userSample;
     private TaskAssignment taskAssignmentSample;
     private TaskAssignmentHistory historySample;
     private TaskAssignmentHistoryResponse responseSample;

     @BeforeEach
    void setUp() {
         userAdmin = new User();
         userAdmin.setId(20);

         userSample = new User();
         userSample.setId(10);

         taskAssignmentSample = new TaskAssignment();
         taskAssignmentSample.setId(100);

         historySample = new TaskAssignmentHistory();
         historySample.setId(1);

         responseSample = new  TaskAssignmentHistoryResponse();
         responseSample.setId(1);
     }

     @Nested
     @DisplayName("assign() & unassign() Tests")
    class AssignAndUnassignTests {
         @Test
         @DisplayName("assign: should mapped with Action.ASSIGN and call saveAndFlush().")
         void assign_ShouldSaveHistoryWhitAssignAction() {
             when(historyMapper.taskAssignmentToHistory(taskAssignmentSample, userAdmin, Action.ASSIGNED))
                     .thenReturn(historySample);

             historyService.assign(taskAssignmentSample, userAdmin);

             verify(historyMapper, times(1)).taskAssignmentToHistory(taskAssignmentSample, userAdmin, Action.ASSIGNED);
             verify(historyRepository, times(1)).saveAndFlush(historySample);
         }

         @Test
         @DisplayName("unassig: should mapped with Action.UNASSIGN and call saveAndFlush().")
         void unassign_ShouldSaveHistoryWithUnassignAction() {
             when(historyMapper.taskAssignmentToHistory(taskAssignmentSample, userAdmin, Action.UNASSIGNED)).thenReturn(historySample);

             historyService.unassign(taskAssignmentSample, userAdmin);

             verify(historyMapper, times(1)).taskAssignmentToHistory(taskAssignmentSample, userAdmin, Action.UNASSIGNED);
             verify(historyRepository, times(1)).saveAndFlush(historySample);
         }
     }

     @Nested
     @DisplayName("Read History Methods Test")
    class ReadHistoryTeste {
          @Test
          @DisplayName("getAllByTaskAndAction: authorize ADMIN rol, return a history when data valid.")
         void getAllByTaskAnsAction_ShouldReturnList_WhenValid() {
              when(userAuthorization.authorizeUser(20, SystemRole.ADMIN)).thenReturn(userAdmin);
              when(historyRepository.findAllByTaskAndAction(30, Action.ASSIGNED)).thenReturn(List.of(historySample));
              when(historyMapper.listResponse(any())).thenReturn(List.of(responseSample));

              List<TaskAssignmentHistoryResponse> responseList = historyService.getAllByTaskAndAction(20, 30, Action.ASSIGNED);

              assertThat(responseList).isNotNull().hasSize(1);
              verify(taskService, times(1)).findTask(30);
              verify(historyRepository, times(1)).findAllByTaskAndAction(30, Action.ASSIGNED);
          }

          @Test
          @DisplayName("getAllByUserAndAction: should return mapped list when valid.")
         void getAllByUserAndAction_ShouldReturnList_WhenValid() {
              when(userAuthorization.authorizeUser(20, SystemRole.ADMIN)).thenReturn(userAdmin);
              when(userAuthorization.authorizeUser(10, SystemRole.MEMBER)).thenReturn(userSample);
              when(historyRepository.findAllByUserAndAction(10, Action.UNASSIGNED)).thenReturn(List.of(historySample));
              when(historyMapper.listResponse(any())).thenReturn(List.of(responseSample));

              List<TaskAssignmentHistoryResponse> responseList = historyService.getAllByUserAndAction(20, 10, Action.UNASSIGNED);

              assertThat(responseList).isNotNull().hasSize(1);
              verify(userAuthorization, times(1)).authorizeUser(20, SystemRole.ADMIN);
              verify(userAuthorization, times(1)).authorizeUser(10, SystemRole.MEMBER);
              verify(historyRepository, times(1)).findAllByUserAndAction(10, Action.UNASSIGNED);
          }

          @Test
          @DisplayName("getAllByProjectAndAction: authorize ADMIN rol, and return mapped list when valid data.")
         void getAllByProjectAndAction_ShouldReturnList_WhenValid() {
              when(userAuthorization.authorizeUser(20, SystemRole.ADMIN)).thenReturn(userAdmin);
              when(historyRepository.findAllByProjectAndAction(50, Action.ASSIGNED)).thenReturn(List.of(historySample));
              when(historyMapper.listResponse(any())).thenReturn(List.of(responseSample));

              List<TaskAssignmentHistoryResponse> responseList = historyService.getAllByProjectAndAction(20, 50, Action.ASSIGNED);

              assertThat(responseList).isNotNull().hasSize(1);
              verify(projectService, times(1)).findProject(50);
              verify(historyRepository, times(1)).findAllByProjectAndAction(50, Action.ASSIGNED);
          }
     }

}
