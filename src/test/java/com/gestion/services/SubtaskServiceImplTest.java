package com.gestion.services;

import com.gestion.system.dto.audit.SubtaskAuditModel;
import com.gestion.system.dto.request.create.SubtaskRequest;
import com.gestion.system.dto.request.update.SubtaskUpdateRequest;
import com.gestion.system.dto.response.SubtaskResponse;
import com.gestion.system.exceptions.InvalidResourceStateException;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.SubtaskMapper;
import com.gestion.system.mappers.update.SubtaskUpdateMapper;
import com.gestion.system.model.entities.Subtask;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.SubtaskRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubtaskServiceImplTest {
     @Mock
    private SubtaskRepository subtaskRepository;
     @Mock
    private TaskService taskService;
     @Mock
    private SubtaskMapper subtaskMapper;
     @Mock
    private SubtaskUpdateMapper subtaskUpdateMapper;
     @Mock
    private SubtaskDeletedHistoryService subtaskDeletedHistory;
     @Mock
    private AuditService audit;
     @Mock
    private UserAuthorizationService userAuthorization;

     @InjectMocks
    SubtaskServiceImpl subtaskService;

     private Subtask subtaskSample;
     private SubtaskResponse responseSample;
     private User userSample;
     private Tasks taskSample;

     @BeforeEach
    void setUp() {
         subtaskSample = new Subtask();
         subtaskSample.setId(1);
         subtaskSample.setIsDeleted(false);
         subtaskSample.setCompleted(false);

         responseSample = new SubtaskResponse();
         responseSample.setId(1);

         taskSample = new Tasks();
         taskSample.setId(100);

         userSample = new User();
         userSample.setId(10);
     }

     @Nested
     @DisplayName("create() & update() Tests")
    class CreateAndUpdateTest {
          @Test
          @DisplayName("cerate: should audit and save when user has MANAGER rol or higher.")
         void create_ShouldCreateAndAudit_WhenValidData() {
              SubtaskRequest request = new SubtaskRequest();
              SubtaskAuditModel auditModel = new SubtaskAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(taskService.findTask(100)).thenReturn(taskSample);
              when(subtaskMapper.requestToEntity(request, taskSample)).thenReturn(subtaskSample);
              when(subtaskUpdateMapper.toAudit(subtaskSample)).thenReturn(auditModel);
              when(subtaskMapper.toResponse(subtaskSample)).thenReturn(responseSample);

              SubtaskResponse result = subtaskService.create(10, request, 100);

              assertThat(result).isNotNull();
              verify(subtaskRepository, times(1)).save(subtaskSample);
              verify(audit, times(1)).create(
                      eq(AuditableEntity.SUBTASK),
                      eq(1),
                      eq(userSample),
                      eq(auditModel));
          }

          @Test
          @DisplayName("update: should update, save and audit changes when user is MEMBER or higher.")
         void update_ShouldUpdateAndAudit_WhenDataExist() {
              SubtaskUpdateRequest request = new SubtaskUpdateRequest();
              SubtaskAuditModel auditModel = new SubtaskAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.MEMBER)).thenReturn(userSample);
              when(subtaskRepository.findByIdAndTask_Id(1, 100)).thenReturn(Optional.of(subtaskSample));
              when(subtaskUpdateMapper.toAudit(subtaskSample)).thenReturn(auditModel);
              when(subtaskMapper.toResponse(subtaskSample)).thenReturn(responseSample);

              SubtaskResponse result = subtaskService.update(10, 1, request, 100);

              assertThat(result).isNotNull();
              verify(subtaskUpdateMapper, times(1)).updateEntity(request, subtaskSample);
              verify(subtaskRepository, times(1)).save(subtaskSample);
              verify(audit, times(1)).update(
                      eq(AuditableEntity.SUBTASK),
                      eq(1),
                      eq(userSample),
                      eq(auditModel),
                      eq(auditModel));
          }

     }

     @Nested
     @DisplayName("delete() Test")
    class DeleteTest {
          @Test
          @DisplayName("delete: should mark isDeleted=true, save in  history and audit.")
         void delete_ShouldMarkAsDeletedAndRegisterHistory_WhenNotDeleted() {
              String message = "Necessary deletion";
              SubtaskAuditModel auditModel = new SubtaskAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(subtaskRepository.findByIdAndTask_Id(1, 100)).thenReturn(Optional.of(subtaskSample));
              when(subtaskUpdateMapper.toAudit(subtaskSample)).thenReturn(auditModel);
              when(subtaskMapper.toResponse(subtaskSample)).thenReturn(responseSample);

              SubtaskResponse result = subtaskService.delete(10, 1, message, 100);

              assertThat(result).isNotNull();
              assertThat(subtaskSample.getIsDeleted()).isTrue();

              verify(subtaskRepository, times(1)).save(subtaskSample);
              verify(subtaskDeletedHistory, times(1)).registerDeletion(subtaskSample, message);
              verify(audit, times(1)).delete(
                      eq(AuditableEntity.SUBTASK),
                      eq(1),
                      eq(userSample),
                      eq(auditModel));
          }

          @Test
          @DisplayName("delete: throw InvalidResourceStateException when subtask already deleted.")
         void delete_ShouldThrowException_WhenSubtaskAlreadyDeleted() {
              subtaskSample.setIsDeleted(true);

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(subtaskRepository.findByIdAndTask_Id(1, 100)).thenReturn(Optional.of(subtaskSample));

              assertThatThrownBy(() -> subtaskService.delete(10, 1, "message", 100))
                      .isInstanceOf(InvalidResourceStateException.class);

              verify(subtaskRepository, times(1)).findByIdAndTask_Id(1, 100);
              verify(subtaskDeletedHistory, never()).registerDeletion(any(), any());
              verify(audit, never()).delete(any(), any(), any(), any());
          }
     }

     @Nested
     @DisplayName("restore() Test")
    class RestoreTest {
          @Test
          @DisplayName("restore: should mark isDeleted=false, restore in history and audit.")
         void restore_ShouldRestoreSubtask_WhenIsDeleted() {
              subtaskSample.setIsDeleted(true);
              SubtaskAuditModel auditModel = new SubtaskAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(subtaskRepository.findById(1)).thenReturn(Optional.of(subtaskSample));
              when(subtaskUpdateMapper.toAudit(subtaskSample)).thenReturn(auditModel);
              when(subtaskMapper.toResponse(subtaskSample)).thenReturn(responseSample);

              SubtaskResponse result = subtaskService.restore(10,1);

              assertThat(result).isNotNull();
              assertThat(subtaskSample.getIsDeleted()).isFalse();

              verify(subtaskRepository, times(1)).findById(1);
              verify(subtaskDeletedHistory, times(1)).restore(1);
              verify(audit, times(1)).restore(
                      eq(AuditableEntity.SUBTASK),
                      eq(1),
                      eq(userSample),
                      eq(auditModel));
          }

          @Test
          @DisplayName("restore: throw InvalidResourceStateException when subtask wasn't delete.")
         void restore_ShouldThrowException_WhenNoDeleted() {
              subtaskSample.setIsDeleted(false);

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(subtaskRepository.findById(1)).thenReturn(Optional.of(subtaskSample));

              assertThatThrownBy(() -> subtaskService.restore(10,1))
                      .isInstanceOf(InvalidResourceStateException.class);

              verify(subtaskRepository, times(1)).findById(1);
              verify(subtaskDeletedHistory, never()).restore(any());
              verify(audit, never()).restore(any(), any(), any(), any());
          }
     }

     @Nested
     @DisplayName("Read Methods Test")
    class ReadTests {
          @Test
          @DisplayName("getAllByTaskId: should filter by isDeleted=false")
         void getAllByTaskId_ShouldReturnList() {
            when(subtaskRepository.findAllByTask_IdAndIsDeletedFalse(100)).thenReturn(List.of(subtaskSample));
            when(subtaskMapper.listResponse(List.of(subtaskSample))).thenReturn(List.of(responseSample));

            List<SubtaskResponse> responseList = subtaskService.getAllByTaskId(100);

            assertThat(responseList).isNotNull().hasSize(1);
            verify(subtaskRepository, times(1)).findAllByTask_IdAndIsDeletedFalse(100);
          }

          @Test
          @DisplayName("getAllIsDeleted: should return a list of all subtask by isDeleted=true.")
         void getAllIsDeleted_ShouldReturnList() {
              when(subtaskRepository.findAllByTask_IdAndIsDeleted(100, true)).thenReturn(List.of(subtaskSample));
              when(subtaskMapper.listResponse(List.of(subtaskSample))).thenReturn(List.of(responseSample));

              List<SubtaskResponse> responseList = subtaskService.getAllIsDeleted(100);

              assertThat(responseList).isNotNull().hasSize(1);
              verify(subtaskRepository, times(1)).findAllByTask_IdAndIsDeleted(100, true);
          }

          @Test
          @DisplayName("findSubtask: should throw ResourceNotFoundException is data not exits.")
         void findSubtask_ShouldThrowException_WhenNotFound() {
              when(subtaskRepository.findById(1)).thenReturn(Optional.empty());

              assertThatThrownBy(() -> subtaskService.findSubtask(1))
                      .isInstanceOf(ResourceNotFoundException.class);
          }
     }

}
