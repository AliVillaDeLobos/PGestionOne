package com.gestion.mappers;


import com.gestion.system.dto.request.create.DaysHoursRequest;
import com.gestion.system.mappers.request.DaySubtaskMapper;
import com.gestion.system.mappers.request.DaysHoursMapper;
import com.gestion.system.model.entities.DaySubtasks;
import com.gestion.system.model.entities.DaysHours;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DaysHoursTest {
    @Mock
    private DaySubtaskMapper daySubtaskMapper;
    private DaysHoursMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DaysHoursMapper(new ModelMapper(),  daySubtaskMapper);
    }

    @Nested
    class ResponseTest{
        @Test
        void shouldReturnNull(){
            assertNull(mapper.toResponse(null));
        }
    }

    @Nested
    class RequestTest{
        @Test
        void shouldMapRequesToEntity(){
            DaysHoursRequest daysHoursRequest = new DaysHoursRequest();
            DaySubtasks subtasks = new DaySubtasks();

            DaysHours entity = mapper.requestToEntity(daysHoursRequest, subtasks);

            assertNotNull(entity);
            assertEquals(subtasks, entity.getDaySubtask());
        }
    }

    @Nested
    class ListsTests{
        @Test
        void shouldReturnEmptyListWhitNull(){
            assertTrue(mapper.listResponse(null).isEmpty());
            assertTrue(mapper.listResponse(List.of()).isEmpty());
        }

        @Test
        void shouldMapListToEntityCorrectly(){
            DaysHoursRequest daysHoursRequest = new DaysHoursRequest();
            DaySubtasks subtasks = new DaySubtasks();

            List<DaysHours> resultList = mapper.listEntity(List.of(daysHoursRequest), subtasks);

            assertEquals(1, resultList.size());
            assertEquals(subtasks, resultList.get(0).getDaySubtask());
        }
    }


}
