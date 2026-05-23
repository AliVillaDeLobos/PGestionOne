package com.gestion.mappers;

import com.gestion.system.dto.response.DayResponse;
import com.gestion.system.dto.response.WeekResponse;
import com.gestion.system.mappers.response.DayMapper;
import com.gestion.system.mappers.response.WeekMapper;
import com.gestion.system.model.entities.Day;
import com.gestion.system.model.entities.Week;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DayMapperTest {
    @Mock
    private WeekMapper weekMapper;
    private DayMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DayMapper(new ModelMapper(), weekMapper);
    }

    @Nested
    class ResponseTest{
        @Test
        void ShouldMapCorrectlyBasicsFields(){
            Day day = new Day();
            day.setId(5);
            day.setDayName("MONDAY");

            when(weekMapper.toResponse(any())).thenReturn(null);

            DayResponse response = mapper.toResponse(day);

            assertNotNull(response);
            assertEquals(5, response.getId());
            assertEquals("MONDAY", response.getDayName());
        }

        @Test
        void shouldWeekMapperInjectCorrectly(){
            Week week = new Week();

            WeekResponse weekResponse = new WeekResponse();
            weekResponse.setId(36);

            when(weekMapper.toResponse(any())).thenReturn(weekResponse);

            Day day = new Day();
            day.setWeek(week);

            DayResponse response = mapper.toResponse(day);

            assertNotNull(response.getWeek());
            assertEquals(36, response.getWeek().getId());
        }
    }

    @Nested
    class ListTest{
        @Test
        void ShouldHandlingNullWithEmptyList(){
            List<DayResponse> list = mapper.listResponse(null);

            assertNotNull(list);
            assertTrue(list.isEmpty());
        }
    }
}
