package com.gestion.services;

import com.gestion.system.dto.audit.DaySubtaskAuditModel;
import com.gestion.system.dto.request.create.DaySubtaskRequest;
import com.gestion.system.dto.request.update.DaySubtaskUpdateRequest;
import com.gestion.system.dto.response.DaySubtaskResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.DaySubtaskMapper;
import com.gestion.system.mappers.update.DaySubtaskUpdateMapper;
import com.gestion.system.model.entities.Day;
import com.gestion.system.model.entities.DaySubtasks;
import com.gestion.system.model.entities.Subtask;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.DaysSubtasksRepository;
import com.gestion.system.service.*;
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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.COLLECTION;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DaySubtaskServiceImplTest {
     @Mock
    private DaysSubtasksRepository daysSubtasksRepository;
     @Mock
    private DaySubtaskMapper daySubtaskMapper;
     @Mock
    private DaySubtaskUpdateMapper daySubtaskUpdateMapper;
     @Mock
    private DayService dayService;
     @Mock
    private SubtaskService subtaskService;
     @Mock
    private AuditService auditService;
     @Mock
    private UserAuthorizationService userAuthorization;

     @InjectMocks
    DaySubtaskServiceImpl daySubtaskService;

     @Captor
    private ArgumentCaptor<DaySubtaskAuditModel> auditModelCaptor;
     @Captor
    private ArgumentCaptor<DaySubtasks> daySubtasksCaptor;

    private DaySubtasks daySubtasksSample;
    private DaySubtaskResponse responseSample;
    private User userSample;
    private Day daySample;
    private Subtask subtaskSample;

     @BeforeEach
    void setUp(){
         daySubtasksSample = new DaySubtasks();
         daySubtasksSample.setId(1);

         responseSample = new DaySubtaskResponse();
         responseSample.setId(1);

         userSample = new User();
         userSample.setId(10);

         daySample = new Day();
         daySample.setId(20);

         subtaskSample = new Subtask();
         subtaskSample.setId(30);
     }

     @Nested
     @DisplayName("create() Test")
    class CreateTest{

          @Test
          @DisplayName("Should create, audit entity and return response.")
         void create_ShouldCreateAndAudit_WhenValidData() {
              DaySubtaskRequest request = new DaySubtaskRequest();
              request.setIdDay(20);
              request.setIdSubtask(30);

              DaySubtaskAuditModel auditModel = new DaySubtaskAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(dayService.findDay(20)).thenReturn(daySample);
              when(subtaskService.findSubtask(30)).thenReturn(subtaskSample);
              when(daySubtaskMapper.requestToEntity(request, daySample, subtaskSample)).thenReturn(daySubtasksSample);
              when(daysSubtasksRepository.save(daySubtasksSample)).thenReturn(daySubtasksSample);
              when(daySubtaskUpdateMapper.toAudit(daySubtasksSample)).thenReturn(auditModel);
              when(daySubtaskMapper.toResponse(daySubtasksSample)).thenReturn(responseSample);

              DaySubtaskResponse result = daySubtaskService.create(request, 10);

              assertThat(result).isNotNull();
              assertThat(result.getId()).isEqualTo(1);

              verify(daysSubtasksRepository, times(1)).save(daySubtasksSample);
              verify(auditService, times(1)).create(
                      eq(AuditableEntity.DAY_SUBTASK),
                      eq(1),
                      eq(userSample),
                      auditModelCaptor.capture());

              assertThat(auditModelCaptor.getValue()).isEqualTo(auditModel);
          }

          @Test
          @DisplayName("Should throw expection if Day ID doesn't exist.")
         void create_ShouldThrowException_WhenDayNotFound() {
              DaySubtaskRequest request = new DaySubtaskRequest();
              request.setIdDay(20);

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(dayService.findDay(20)).thenThrow(new ResourceNotFoundException(""));

              assertThatThrownBy(() -> daySubtaskService.create(request, 10))
                      .isInstanceOf(ResourceNotFoundException.class);

              verify(daysSubtasksRepository, never()).save(any());
              verifyNoInteractions(auditService);
          }
     }

     @Nested
     @DisplayName("update() Test")
    class UpdateTest{
          @Test
          @DisplayName("Should update and audit entity correctly if entity exist.")
         void update_ShouldUpdateAndAudit_WhenValidData() {
              DaySubtaskUpdateRequest updateRequest = new DaySubtaskUpdateRequest();
              updateRequest.setIdDay(20);

              DaySubtaskAuditModel originAudit = new DaySubtaskAuditModel();
              DaySubtaskAuditModel updateAudit = new DaySubtaskAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(daysSubtasksRepository.findById(1)).thenReturn(Optional.of(daySubtasksSample));
              when(dayService.findDay(20)).thenReturn(daySample);

              when(daySubtaskUpdateMapper.toAudit(daySubtasksSample)).thenReturn(originAudit, updateAudit);
              when(daySubtaskUpdateMapper.updateEntity(updateRequest, daySample, daySubtasksSample)).thenReturn(daySubtasksSample);
              when(daySubtaskMapper.toResponse(daySubtasksSample)).thenReturn(responseSample);

              DaySubtaskResponse result = daySubtaskService.update(1, updateRequest, 10);

              assertThat(result).isNotNull();
              assertThat(result.getId()).isEqualTo(1);

              verify(auditService, times(1)).update(
                      eq(AuditableEntity.DAY_SUBTASK),
                      eq(1),
                      eq(userSample),
                      eq(originAudit),
                      eq(updateAudit));
          }
     }

     @Nested
     @DisplayName("getById() Test")
    class getByIdTest{
         @Test
         @DisplayName("Should return mapped response if exist.")
        void getById_ShouldReturnResponse_WheExist() {
             when(daysSubtasksRepository.findById(1)).thenReturn(Optional.of(daySubtasksSample));
             when(daySubtaskMapper.toResponse(daySubtasksSample)).thenReturn(responseSample);

             DaySubtaskResponse result = daySubtaskService.getById(1);

             assertThat(result).isNotNull();
             assertThat(result.getId()).isEqualTo(1);
         }

         @Test
         @DisplayName("Should throw exception when not found ID.")
        void findDaySubtask_ShouldThrowException_WhenNotFound() {
             when(daysSubtasksRepository.findById(1)).thenReturn(Optional.empty());

             assertThatThrownBy(() -> daySubtaskService.getById(1))
                     .isInstanceOf(ResourceNotFoundException.class);
         }
     }

     @Nested
     @DisplayName("Lists Test")
    class ListTest {
          @Test
          @DisplayName("getByIdSubtask: should return a mapped list if exists data.")
         void getByIdDay_ShouldReturnMappedList_WhenExistData() {
              when(daysSubtasksRepository.findAllByDay_Id(20)).thenReturn(List.of(daySubtasksSample));
              when(daySubtaskMapper.listResponse(List.of(daySubtasksSample))).thenReturn(List.of(responseSample));

              List<DaySubtaskResponse> result = daySubtaskService.getByIdDay(20);

              assertThat(result).isNotNull().hasSize(1);
          }

          @Test
          @DisplayName("getByIdSubtask: should return a empty list if doesn't exists data.")
         void getByIdSubtask_ShouldReturnEmptyList_WhenNoData() {
             when(daysSubtasksRepository.findAllBySubtask_Id(30)).thenReturn(Collections.emptyList());
             when(daySubtaskMapper.listResponse(Collections.emptyList())).thenReturn(Collections.emptyList());

             List<DaySubtaskResponse> result = daySubtaskService.getByIdSubtask(30);

             assertThat(result).isNotNull().isEmpty();
         }
     }

     @Nested
     @DisplayName("delete() Test")
    class deleteTest{
          @Test
          @DisplayName("delete: should audit and delete entity correctly.")
         void delete_ShouldDelete_WhenExistData() {
              DaySubtaskAuditModel originAudit = new DaySubtaskAuditModel();

              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(daysSubtasksRepository.findById(1)).thenReturn(Optional.of(daySubtasksSample));
              when(daySubtaskUpdateMapper.toAudit(daySubtasksSample)).thenReturn(originAudit);

              daySubtaskService.delete(1, 10);

              verify(daysSubtasksRepository, times(1)).delete(daySubtasksCaptor.capture());
              assertThat(daySubtasksCaptor.getValue()).isEqualTo(daySubtasksSample);

              verify(auditService, times(1)).delete(
                      eq(AuditableEntity.DAY_SUBTASK),
                      eq(1),
                      eq(userSample),
                      eq(originAudit));
          }
     }

}

