package com.gestion.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.system.dto.response.AuditResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.exceptions.UnauthorizedOperationException;
import com.gestion.system.mappers.response.AuditMapper;
import com.gestion.system.model.entities.Audit;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.Operation;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.AuditRepository;
import com.gestion.system.service.AuditServiceImpl;
import com.gestion.system.service.UserAuthorizationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditServiceImplTest {

     @Mock
    private AuditRepository auditRepository;
     @Mock
    private ObjectMapper objectMapper;
     @Mock
    private AuditMapper auditMapper;
     @Mock
    private UserAuthorizationService userAuthorization;

     @InjectMocks
    private AuditServiceImpl auditService;


    @Nested
    @DisplayName("Audit Registration Test (Write Operations)")
    class MakeAuditTest {

        @Test
        @DisplayName("delete: oldData should be mapped and save DELETE operation.")
        void deletedSucceeds() {
            AuditableEntity table = AuditableEntity.DAY_SUBTASK;
            Integer idRecord = 3;
            User actor = new User();
            actor.setId(5);
            Object oldData = new Object();

            JsonNode oldJsonMock = Mockito.mock(JsonNode.class);

            when(objectMapper.valueToTree(oldData)).thenReturn(oldJsonMock);

            auditService.delete(table, idRecord, actor, oldData);

            ArgumentCaptor<Audit> auditCaptor = ArgumentCaptor.forClass(Audit.class);
            verify(auditRepository, times(1)).save(auditCaptor.capture());

            Audit savedAudit = auditCaptor.getValue();
            assertEquals(idRecord, savedAudit.getRecordId());
            assertEquals(table.name(), savedAudit.getTableName());
            assertEquals(Operation.DELETE, savedAudit.getOperation());
            assertEquals(oldJsonMock, savedAudit.getOldData());
            assertEquals(5, savedAudit.getUserCreated().getId());
        }

        @Test
        @DisplayName("update: oldData and newData should be mapped to JsonNode and save UPDATE operation.")
        void updatedASucceeds() {
            AuditableEntity table = AuditableEntity.USER;
            Integer idRecord = 10;
            User actor = new User();
            Object oldData = new Object();
            Object newData = new Object();

            JsonNode oldJsonMock = Mockito.mock(JsonNode.class);
            JsonNode newJsonMock = Mockito.mock(JsonNode.class);

            when(objectMapper.valueToTree(oldData)).thenReturn(oldJsonMock);
            when(objectMapper.valueToTree(newData)).thenReturn(newJsonMock);

            auditService.update(table, idRecord, actor, oldData, newData);

            ArgumentCaptor<Audit> auditCaptor = ArgumentCaptor.forClass(Audit.class);
            verify(auditRepository, times(1)).save(auditCaptor.capture());

            Audit saveAudit = auditCaptor.getValue();
            assertEquals(idRecord, saveAudit.getRecordId());
            assertEquals(table.name(), saveAudit.getTableName());
            assertEquals(Operation.UPDATE, saveAudit.getOperation());
            assertEquals(oldJsonMock, saveAudit.getOldData());
            assertEquals(newJsonMock, saveAudit.getNewData());
        }

        @Test
        @DisplayName("create: newData should be mapped to JsonNode and save CREATE operation.")
        void createdSucceeds(){
            AuditableEntity table = AuditableEntity.PROJECT;
            Integer idRecord = 12;
            User actor = new User();
            actor.setId(7);
            Object newData = new Object();

            JsonNode newJsonMock = Mockito.mock(JsonNode.class);

            when(objectMapper.valueToTree(newData)).thenReturn(newJsonMock);

            auditService.create(table, idRecord, actor, newData);

            ArgumentCaptor<Audit> auditCaptor = ArgumentCaptor.forClass(Audit.class);
            verify(auditRepository, times(1)).save(auditCaptor.capture());

            Audit savedAudit = auditCaptor.getValue();
            assertEquals(table.name(), savedAudit.getTableName());
            assertEquals(idRecord, savedAudit.getRecordId());
            assertEquals(Operation.CREATE, savedAudit.getOperation());
            assertEquals(newJsonMock, savedAudit.getNewData());
            assertNull(savedAudit.getOldData());
            assertEquals(7, savedAudit.getUserCreated().getId());
        }

        @Test
        @DisplayName("restored: newData should be mapped to JsonNode and save RESTORED operation.")
        void restoredSucceeds(){
            AuditableEntity table = AuditableEntity.DAY_SUBTASK;
            Integer idRecord = 45;
            User actor = new User();
            actor.setId(3);
            Object newData = new Object();

            JsonNode newJsonMock = Mockito.mock(JsonNode.class);

            when(objectMapper.valueToTree(newData)).thenReturn(newJsonMock);

            auditService.restore(table, idRecord, actor, newData);

            ArgumentCaptor<Audit> auditCaptor = ArgumentCaptor.forClass(Audit.class);
            verify(auditRepository, times(1)).save(auditCaptor.capture());

            Audit savedAudit = auditCaptor.getValue();
            assertEquals(table.name(), savedAudit.getTableName());
            assertEquals(idRecord, savedAudit.getRecordId());
            assertEquals(Operation.RESTORE, savedAudit.getOperation());
            assertEquals(newJsonMock, savedAudit.getNewData());
            assertNull(savedAudit.getOldData());
            assertEquals(3, savedAudit.getUserCreated().getId());
        }
    }

    @Nested
    @DisplayName("Authorization and Consult Audit Test (Read Authorized)")
    class AuthAndConsultAuditTest {

        @Test
        @DisplayName("getAllByUser: should return mapped AuditResponse list when user has ADMIN role.")
        void getAllByUser_Authorization_ReturnAuditList() {
            Integer idUser = 10;
            Integer idActor = 17;

            Audit auditEntity = new Audit();
            List<Audit> auditList = List.of(auditEntity);
            AuditResponse dto = new  AuditResponse();
            List<AuditResponse> dtoResponseList = List.of(dto);

            when(auditRepository.findAllByUserCreated_Id(idActor)).thenReturn(auditList);
            when(auditMapper.listResponse(auditList)).thenReturn(dtoResponseList);

            List<AuditResponse> actualResponse = auditService.getAllByUser(idUser, idActor);

            assertNotNull(actualResponse);
            assertEquals(1, actualResponse.size());
            assertEquals(dtoResponseList, actualResponse);

            verify(userAuthorization, times(1)).authorizeUser(idUser, SystemRole.ADMIN);
        }


        @Test
        @DisplayName("getAllByUser: should throw UnauthorizedOperationException when user lacks ADMIN role")
        void getAllByUser_Unauthorized_ThrowsException() {
            Integer idUser = 10;
            Integer idActor = 5;

            doThrow(new UnauthorizedOperationException("Unauthorized"))
                    .when(userAuthorization).authorizeUser(idUser, SystemRole.ADMIN);

            assertThrows(UnauthorizedOperationException.class,
                    () -> { auditService.getAllByUser(idUser, idActor); });

            verify(auditRepository, never()).findAllByUserCreated_Id(any());
            verify(auditMapper, never()).listResponse(any());
        }

        @Test
        @DisplayName("getAllByUser: should throw ResourceNotFoundException when ID does not exist")
        void getAllByUser_UserNotFound_ThrowsException() {
            Integer idUser = 99;
            Integer idActor = 5;

            doThrow(new ResourceNotFoundException("User not found"))
                    .when(userAuthorization).authorizeUser(idUser, SystemRole.ADMIN);

            assertThrows(ResourceNotFoundException.class,
                    () -> {auditService.getAllByUser(idUser, idActor);});

            verify(auditRepository, never()).findAllByUserCreated_Id(any());
            verify(auditMapper, never()).listResponse(any());
        }

        @Test
        @DisplayName("getAllByTable: should return mapped AuditResponse list for specified table")
        void getAllByTable_Succeeds() {
            Integer idUser = 10;
            AuditableEntity table = AuditableEntity.USER;

            List<Audit> auditList = List.of(new Audit());
            List<AuditResponse> expectedList = List.of(new AuditResponse());

            when(auditRepository.findAllByTableNameIgnoreCase(table.name())).thenReturn(auditList);
            when(auditMapper.listResponse(auditList)).thenReturn(expectedList);

            List<AuditResponse> actualResponse = auditService.getAllByTable(idUser, table);

            assertNotNull(actualResponse);
            assertEquals(expectedList, actualResponse);

            verify(userAuthorization, times(1)).authorizeUser(idUser, SystemRole.ADMIN);
            verify(auditRepository, times(1)).findAllByTableNameIgnoreCase(table.name());
        }

        @Test
        @DisplayName("getAllByRecord: should return mapped AuditResponse list for specified record ID")
        void getAllByRecord_Succeeds() {
            Integer idUser = 10;
            Integer recordId = 42;

            List<Audit> auditList = List.of(new Audit());
            List<AuditResponse> expectedList = List.of(new AuditResponse());

            when(auditRepository.findAllByRecordId(recordId)).thenReturn(auditList);
            when(auditMapper.listResponse(auditList)).thenReturn(expectedList);

            List<AuditResponse> actualResponse = auditService.getAllByRecord(idUser, recordId);

            assertNotNull(actualResponse);
            assertEquals(expectedList, actualResponse);

            verify(userAuthorization, times(1)).authorizeUser(idUser, SystemRole.ADMIN);
            verify(auditRepository, times(1)).findAllByRecordId(recordId);
        }

        @Test
        @DisplayName("getAllOperationByDate: should transform LocalDate to start/end LocalDateTime bounds correctly")
        void getAllOperationByDate_Succeeds() {
            Integer idUser = 10;
            Operation operation = Operation.CREATE;
            LocalDate startDate = LocalDate.of(2026, 3, 1);
            LocalDate endDate = LocalDate.of(2026, 3, 15);

            LocalDateTime expectedStart = startDate.atStartOfDay();
            LocalDateTime expectedEnd = endDate.plusDays(1).atStartOfDay();

            List<Audit> auditList = List.of(new Audit());
            List<AuditResponse> expectedList = List.of(new AuditResponse());

            when(auditRepository.findAllByOperationAndCreatedDateBetween(operation, expectedStart, expectedEnd))
                    .thenReturn(auditList);
            when(auditMapper.listResponse(auditList)).thenReturn(expectedList);

            List<AuditResponse> actualResponse = auditService.getAllOperationByDate(idUser, operation, startDate, endDate);
            assertNotNull(actualResponse);
            assertEquals(expectedList, actualResponse);

            verify(userAuthorization, times(1)).authorizeUser(idUser, SystemRole.ADMIN);
            verify(auditRepository, times(1)).findAllByOperationAndCreatedDateBetween(operation, expectedStart, expectedEnd);
        }

    }


    @Nested
    @DisplayName("Edge Cases & Granular Business Rules")
    class EdgeCasesTest {

        @Test
        @DisplayName("delete: should correctly handle null oldData without throwing NPE during tree conversion")
        void delete_WithNullOldData_Succeeds() {
            AuditableEntity table = AuditableEntity.DAY_SUBTASK;
            Integer idRecord = 5;
            User actor = new User();

            when(objectMapper.valueToTree(null)).thenReturn(null);

            auditService.delete(table, idRecord, actor, null);

            ArgumentCaptor<Audit> captor = ArgumentCaptor.forClass(Audit.class);
            verify(auditRepository, times(1)).save(captor.capture());

            assertNull(captor.getValue().getOldData());
            assertEquals(Operation.DELETE, captor.getValue().getOperation());
        }

        @Test
        @DisplayName("getAllOperationByDate: same start and end date should calculate full 24h boundary")
        void getAllOperationByDate_SameDay_CalculatesCorrectUpperBoundary() {
            Integer idUser = 1;
            Operation operation = Operation.UPDATE;
            LocalDate sameDate = LocalDate.of(2026, 9, 17);

            LocalDateTime expectedStart = sameDate.atStartOfDay();
            LocalDateTime expectedEnd = sameDate.plusDays(1).atStartOfDay();

            when(auditRepository.findAllByOperationAndCreatedDateBetween(operation, expectedStart, expectedEnd))
                    .thenReturn(List.of());
            when(auditMapper.listResponse(anyList())).thenReturn(List.of());

            List<AuditResponse> response = auditService.getAllOperationByDate(idUser, operation, sameDate, sameDate);

            assertNotNull(response);
            assertTrue(response.isEmpty());
            verify(auditRepository, times(1))
                    .findAllByOperationAndCreatedDateBetween(operation, expectedStart, expectedEnd);
        }
    }


    @Nested
    @DisplayName("Break & Boundary Tests - Write Operations")
    class BreakWriteOperationsTest {


        @Test
        @DisplayName("create: should propagate IllegalArgumentException when ObjectMapper fails to convert object")
        void create_ObjectMapperFailure_PropagatesException() {
            AuditableEntity table = AuditableEntity.PROJECT;
            Integer idRecord = 12;
            User actor = new User();
            Object invalidData = new Object();

            when(objectMapper.valueToTree(invalidData))
                    .thenThrow(new IllegalArgumentException("Cannot serialize object to JsonNode"));

            assertThrows(IllegalArgumentException.class, () -> {
                auditService.create(table, idRecord, actor, invalidData);
            });

            verify(auditRepository, never()).save(any());
        }
    }


    @Nested
    @DisplayName("Break & Boundary Tests - Date Boundaries")
    class DateBoundaryTest {

        @Test
        @DisplayName("getAllOperationByDate: when startDate and endDate are identical, upper bound must strictly be start of next day")
        void getAllOperationByDate_SameDay_CalculatesExactBoundary() {
            Integer idUser = 1;
            Operation operation = Operation.CREATE;
            LocalDate singleDate = LocalDate.of(2026, 9, 17);

            LocalDateTime expectedStart = singleDate.atStartOfDay();
            LocalDateTime expectedEnd = singleDate.plusDays(1).atStartOfDay();

            when(auditRepository.findAllByOperationAndCreatedDateBetween(operation, expectedStart, expectedEnd))
                    .thenReturn(List.of());
            when(auditMapper.listResponse(anyList())).thenReturn(List.of());

           List<AuditResponse> result = auditService.getAllOperationByDate(idUser, operation, singleDate, singleDate);

            verify(auditRepository, times(1))
                    .findAllByOperationAndCreatedDateBetween(operation, expectedStart, expectedEnd);
            assertNotNull(result);
        }
    }


    @Nested
    @DisplayName("Break & Boundary Tests - Infrastructure Failures")
    class InfrastructureFailureTest {

        @Test
        @DisplayName("getAllByTable: should propagate DataAccessException if Database connection fails")
        void getAllByTable_DatabaseFailure_PropagatesException() {
            Integer idUser = 10;
            AuditableEntity table = AuditableEntity.USER;

            when(auditRepository.findAllByTableNameIgnoreCase(table.name()))
                    .thenThrow(new RuntimeException("Database connection timeout"));

            assertThrows(RuntimeException.class, () -> {
                auditService.getAllByTable(idUser, table);
            });

            verify(auditMapper, never()).listResponse(any());
        }
    }
}


