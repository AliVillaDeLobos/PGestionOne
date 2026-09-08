package com.gestion.mappers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.system.dto.response.AuditResponse;
import com.gestion.system.dto.response.UserResponse;
import com.gestion.system.mappers.request.UserMapper;
import com.gestion.system.mappers.response.AuditMapper;
import com.gestion.system.model.entities.Audit;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.Operation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuditMapperTest {
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private AuditMapper auditMapper;



    @Nested
    class ResponseTests {
        @Test
        void shouldMapperCorrectlyBasicsFields() {
            Audit audit = Audit.builder()
                    .id(1L)
                    .tableName("user")
                    .recordId(10)
                    .operation(Operation.DELETE)
                    .createdDate(LocalDateTime.now())
                    .build();

            AuditResponse dto = auditMapper.toResponse(audit);

            assertEquals(1, dto.getId());
            assertEquals("user", dto.getTableName());
            assertEquals(10, dto.getRecordId());
            assertEquals(Operation.DELETE, dto.getAction());
        }

        @Test
        void shouldPreserveJsonData() throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode oldData = assertDoesNotThrow(() -> objectMapper.readTree("""
                    {
                    "name":"juan", "role":"USER"
                    }
                    """));

            JsonNode newData = assertDoesNotThrow(() -> objectMapper.readTree("""
                    {
                    "name":"Juan", "role":"ADMIN"
                    }
                    """));

            Audit audit = new Audit();
            audit.setOldData(oldData);
            audit.setNewData(newData);

            AuditResponse response = auditMapper.toResponse(audit);

            assertEquals(oldData, response.getOldData());
            assertEquals(newData, response.getNewData());
            assertEquals("juan", response.getOldData().get("name").asText());
            assertEquals("Juan", response.getNewData().get("name").asText());
        }

        @Test
        void shouldCreatedByCorrectly()  {
            User user = new User();
            user.setName("Gael");

            UserResponse userResponse = new UserResponse();
            userResponse.setName("Gael");
            userResponse.setId(20);

            when(userMapper.toResponse(any(User.class)))
                    .thenReturn(userResponse);

            Audit audit = new Audit();
            audit.setUserCreated(user);

            AuditResponse response = auditMapper.toResponse(audit);


            assertNotNull(response.getCreatedBy());
            assertEquals("Gael", response.getCreatedBy().getName());
            assertEquals(20, response.getCreatedBy().getId());
        }
    }


    @Nested
    class ListResponseTests{
        @Test
        void shouldMapListCorrectly(){
            Audit audit = new Audit();
            audit.setId(4L);
            audit.setTableName("user_project");

            List<AuditResponse> list = auditMapper.listResponse(List.of(audit));
            AuditResponse dto = list.get(0);

            assertEquals(1, list.size());
            assertEquals("user_project", dto.getTableName());
        }

        @Test
        void shouldHandlingNull(){
            List<AuditResponse> list = auditMapper.listResponse(null);

            assertNotNull(list);
            assertTrue(list.isEmpty());
        }
    }


}
