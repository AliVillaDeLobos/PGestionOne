package com.gestion.mappers;

import com.gestion.system.dto.request.create.DaySubtaskRequest;
import com.gestion.system.dto.request.update.DaySubtaskUpdateRequest;
import com.gestion.system.dto.response.DayResponse;
import com.gestion.system.dto.response.DaySubtaskResponse;
import com.gestion.system.dto.response.SubtaskResponse;
import com.gestion.system.mappers.request.DaySubtaskMapper;
import com.gestion.system.mappers.request.SubtaskMapper;
import com.gestion.system.mappers.response.DayMapper;
import com.gestion.system.mappers.update.DaySubtaskUpdateMapper;
import com.gestion.system.model.entities.Day;
import com.gestion.system.model.entities.DaySubtasks;
import com.gestion.system.model.entities.Subtask;
import com.gestion.system.model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DaySubtaskMapperTest {
    @Mock
    private DayMapper dayMapper;
    @Mock
    private SubtaskMapper subtaskMapper;
    private DaySubtaskMapper mapper;
    private DaySubtaskUpdateMapper updateMapper;

    @BeforeEach
    void setup() {
        mapper = new DaySubtaskMapper(dayMapper, subtaskMapper);
        updateMapper = Mappers.getMapper(DaySubtaskUpdateMapper.class);
    }

    @Nested
    class ResponseTest{
        @Test
        void shouldInjectExternalMappersToResponse(){
            Day day = new Day();
            Subtask subtask = new Subtask();

            DayResponse dayResponse = new DayResponse();
            SubtaskResponse subtaskResponse = new SubtaskResponse();
            dayResponse.setId(4);
            subtaskResponse.setId(6);
            when(dayMapper.toResponse(day)).thenReturn(dayResponse);
            when(subtaskMapper.toResponse(subtask)).thenReturn(subtaskResponse);

            DaySubtasks entity = DaySubtasks.builder()
                    .subtask(subtask)
                    .day(day)
                    .status(Status.CANCELLED)
                    .build();

            DaySubtaskResponse response = mapper.toResponse(entity);

            assertEquals(4, response.getDay().getId());
            assertEquals(6, response.getSubtask().getId());
            assertEquals(Status.CANCELLED, response.getStatus());
        }
    }

    @Nested
    class RequestTest{
        @Test
        void shouldReturnNullWhenRequestIsNull(){
            assertNull(mapper.requestToEntity(null, new Day(), new Subtask()));
        }

        @Test
        void shouldThrowExceptionWhenExternalEntitiesAreNull(){
            DaySubtaskRequest request = new DaySubtaskRequest();

            assertThrows(IllegalArgumentException.class,
                    () -> mapper.requestToEntity(request, new Day(), null));
            assertThrows(IllegalArgumentException.class,
                    () -> mapper.requestToEntity(request, null, new Subtask()));
        }
        @Test
        void shouldMapRequestToEntity(){
            Day day = Day.builder().id(4).build();
            Subtask subtask = Subtask.builder().id(7).build();
            DaySubtaskRequest request = new DaySubtaskRequest();

            DaySubtasks entity = mapper.requestToEntity(request, day, subtask);

            assertNotNull(entity);
            assertEquals(4, entity.getDay().getId());
            assertEquals(7, entity.getSubtask().getId());
        }
    }

    @Nested
    class ResponseListTest{
        @Test
        void  shouldReturnEmptyListWhenResponseListIsNull(){
            DaySubtasks response = new DaySubtasks();
            List<DaySubtaskResponse> list = mapper.listResponse(List.of(response));

            assertTrue(mapper.listResponse(null).isEmpty());
            assertEquals(1, list.size());
        }
    }

    @Nested
    class UpdateTest{
        @Test
        void shoulNotUpdateId(){
            DaySubtaskUpdateRequest request = new DaySubtaskUpdateRequest();
            request.setStatus(Status.COMPLETED);

            DaySubtasks entity = new DaySubtasks();
            entity.setId(15);
            entity.setStatus(Status.PENDING);

            updateMapper.updateEntity(request, new Day(), entity);

            assertEquals(Status.COMPLETED, entity.getStatus());
            assertEquals(15, entity.getId());

        }
    }


}
