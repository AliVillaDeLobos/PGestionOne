package com.gestion.services;

import com.gestion.system.dto.audit.DayHoursAuditModel;
import com.gestion.system.dto.request.create.DaysHoursRequest;
import com.gestion.system.dto.request.update.DaysHoursUpdateRequest;
import com.gestion.system.dto.response.DaysHoursResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.DaysHoursMapper;
import com.gestion.system.mappers.update.DaysHoursUpdateMapper;
import com.gestion.system.model.entities.DaySubtasks;
import com.gestion.system.model.entities.DaysHours;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.DaysHoursRepository;
import com.gestion.system.service.AuditService;
import com.gestion.system.service.DaySubtaskService;
import com.gestion.system.service.DaysHoursServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DaysHoursServiceImplTest {
     @Mock
    private DaysHoursRepository daysHoursRepository;
     @Mock
    private DaysHoursMapper daysHoursMapper;
     @Mock
    private DaySubtaskService daySubtaskService;
     @Mock
    private AuditService auditService;
     @Mock
    private DaysHoursUpdateMapper daysHoursUpdateMapper;
     @Mock
    private UserAuthorizationService userAuthorization;

     @InjectMocks
    private DaysHoursServiceImpl daysHoursService;

     @Captor
    private ArgumentCaptor<DayHoursAuditModel> auditModelCaptor;
     @Captor
    private ArgumentCaptor<DaysHours> daysHoursCaptor;

     private DaysHours entitySample;
     private DaysHoursResponse  responseSample;
     private User userSample;
     private DaySubtasks daySubtaskSample;

     @BeforeEach
    void setUp(){
         entitySample = new DaysHours();
         entitySample.setId(1);

         responseSample = new DaysHoursResponse();
         responseSample.setId(1);

         userSample = new User();
         userSample.setId(10);

         daySubtaskSample = new DaySubtasks();
         daySubtaskSample.setId(100);
     }

     @Nested
     @DisplayName("crate() Test")
    class CreateTest{

          @Test
          @DisplayName("Should created, audit and return response.")
         void create_ShouldCreateEntityAndAudit_WhenValidData(){
              DaysHoursRequest request = new DaysHoursRequest();
              DayHoursAuditModel mockAuditModel = new DayHoursAuditModel();

              when(daySubtaskService.findDaySubtask(100)).thenReturn(daySubtaskSample);
              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(daysHoursMapper.requestToEntity(request, daySubtaskSample)).thenReturn(entitySample);
              when(daysHoursRepository.save(entitySample)).thenReturn(entitySample);
              when(daysHoursMapper.toResponse(entitySample)).thenReturn(responseSample);
              when(daysHoursUpdateMapper.toAudit(entitySample)).thenReturn(mockAuditModel);

              DaysHoursResponse result = daysHoursService.create(request, 100, 10);

              assertThat(result).isNotNull();
              assertThat(result.getId()).isEqualTo(1);

              verify(daysHoursRepository, timeout(1)).save(entitySample);
              verify(auditService, times(1)).create(
                      eq(AuditableEntity.DAYS_HOURS),
                      eq(1),
                      eq(userSample),
                      auditModelCaptor.capture());

              DayHoursAuditModel captureAudit = auditModelCaptor.getValue();
              assertThat(captureAudit).isEqualTo(mockAuditModel);
          }
     }

     @Nested
     @DisplayName("update() Test")
    class UpdateTest{

         @Test
         @DisplayName("Should update entity, audit status before and after update.")
         void update_ShouldUpdateAndAudit_WhenEntityExist(){
             DaysHoursUpdateRequest updateRequest = new DaysHoursUpdateRequest();
             DayHoursAuditModel originAudit = new DayHoursAuditModel();
             DayHoursAuditModel updateAudit = new DayHoursAuditModel();

             when(daysHoursRepository.findById(1)).thenReturn(Optional.of(entitySample));
             when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
             when(daysHoursUpdateMapper.toAudit(entitySample)).thenReturn(originAudit, updateAudit);
             when((daysHoursUpdateMapper.updateEntity(updateRequest, entitySample))).thenReturn(entitySample);
             when(daysHoursRepository.save(entitySample)).thenReturn(entitySample);
             when(daysHoursMapper.toResponse(entitySample)).thenReturn(responseSample);

             DaysHoursResponse result = daysHoursService.update(1, updateRequest, 10);

             assertThat(result).isNotNull();
             assertThat(result.getId()).isEqualTo(1);

             verify(daysHoursRepository, times(1)).save(entitySample);
             verify(auditService, times(1)).update(
                     eq(AuditableEntity.DAYS_HOURS),
                     eq(1),
                     eq(userSample),
                     eq(originAudit),
                     eq(updateAudit));
         }

          @Test
          @DisplayName("Should throws ResourceNotFoundException if entity doesn't exist.")
         void update_ShouldThrowException_WhenEntityNotFound(){
             DaysHoursUpdateRequest updateRequest = new DaysHoursUpdateRequest();

             when(daysHoursRepository.findById(1)).thenReturn(Optional.empty());

             assertThatThrownBy(() -> daysHoursService.update(1, updateRequest, 10))
                     .isInstanceOf(ResourceNotFoundException.class);

             verifyNoInteractions(auditService);
             verify(daysHoursRepository, never()).save(any());
          }
     }

     @Nested
     @DisplayName("getById() Test")
    class GetByIdTest{

          @Test
          @DisplayName("Should return mapped response when ID exist.")
         void getById_ShouldReturnResponse_WHenExist(){
              when(daysHoursRepository.findById(1)).thenReturn(Optional.of(entitySample));
              when(daysHoursMapper.toResponse(entitySample)).thenReturn(responseSample);

              DaysHoursResponse result = daysHoursService.getById(1);

              assertThat(result).isNotNull();
              assertThat(result.getId()).isEqualTo(1);
              verify(daysHoursRepository, times(1)).findById(1);
          }

          @Test
          @DisplayName("Should throws ResourceNotFoundException if entity doesn't exist.")
         void getById_ShouldThrowException_WhenNotFound(){
              when(daysHoursRepository.findById(1)).thenReturn(Optional.empty());

              assertThatThrownBy(() -> daysHoursService.getById(1))
                      .isInstanceOf(ResourceNotFoundException.class);
          }
     }

     @Nested
     @DisplayName("getBySubtaskId() Test")
    class GetSubtaskId {

         @Test
         @DisplayName("Should return mapped list when data exist.")
         void fetBySubtaskId_ShouldReturnMappedList_WhenDataExist() {
             List<DaysHours> entityList = List.of(entitySample);
             List<DaysHoursResponse> responseList = List.of(responseSample);

             when(daysHoursRepository.findAllByDaySubtask_Id(100)).thenReturn(entityList);
             when(daysHoursMapper.listResponse(entityList)).thenReturn(responseList);

             List<DaysHoursResponse> result = daysHoursService.getBySubtaskId(100);

             assertThat(result).isNotNull().hasSize(1);
             assertThat(result.get(0).getId()).isEqualTo(1);
         }

          @Test
          @DisplayName("Should return an empty list and don't throw exception when doesn't exist any data.")
         void getBySubtaskId_ShouldReturnEmptyLis_WhenNoDataExist() {
            when(daysHoursRepository.findAllByDaySubtask_Id(100)).thenReturn(Collections.emptyList());
            when(daysHoursMapper.listResponse(Collections.emptyList())).thenReturn(Collections.emptyList());

            List<DaysHoursResponse> result = daysHoursService.getBySubtaskId(100);

            assertThat(result).isNotNull().isEmpty();
            verify(daysHoursRepository, times(1)).findAllByDaySubtask_Id(100);
         }
     }

     @Nested
     @DisplayName("delete() Test")
    class DeleteTests {

          @Test
          @DisplayName("Should audit and delete entity if exist.")
         void delete_ShouldAuditAndDelete_WhenEntityExist() {
              DayHoursAuditModel auditModel = new DayHoursAuditModel();

              when(daysHoursRepository.findById(1)).thenReturn(Optional.of(entitySample));
              when(userAuthorization.authorizeUser(10, SystemRole.MANAGER)).thenReturn(userSample);
              when(daysHoursUpdateMapper.toAudit(entitySample)).thenReturn(auditModel);

              daysHoursService.delete(1, 10);

              verify(daysHoursRepository, times(1)).delete(daysHoursCaptor.capture());
              assertThat(daysHoursCaptor.getValue()).isEqualTo(entitySample);
              verify(auditService, times(1)).delete(
                      eq(AuditableEntity.DAYS_HOURS),
                      eq(1),
                      eq(userSample),
                      eq(auditModel));
          }
          @Test
          @DisplayName("Should throw expcetion if entity doesn't exist.")
         void delete_ShouldThrowException_WhenNotFound() {
              when(daysHoursRepository.findById(1)).thenReturn(Optional.empty());

              assertThatThrownBy(() -> daysHoursService.delete(1, 10))
                      .isInstanceOf(ResourceNotFoundException.class);
              verify(daysHoursRepository, never()).delete(any());
              verifyNoInteractions(auditService);
          }
     }

     @Nested
     @DisplayName("findDayHour()")
    class FindDayHourTest{

          @Test
          @DisplayName("Should return entity if exist in DB.")
         void findDayHour_ShouldReturnEntity_WhenExist() {
              when(daysHoursRepository.findById(1)).thenReturn(Optional.of(entitySample));

              DaysHours result = daysHoursService.findDayHours(1);

              assertThat(result).isNotNull();
              assertThat(result.getId()).isEqualTo(1);
          }

          @Test
          @DisplayName("Should throw ResourceNotFoundException if not found in DB.")
         void findDayHour_ShouldThrowException_WhenNotFound() {
              when(daysHoursRepository.findById(1)).thenReturn(Optional.empty());

              assertThatThrownBy(() -> daysHoursService.findDayHours(1))
                      .isInstanceOf(ResourceNotFoundException.class);
          }
     }

}
