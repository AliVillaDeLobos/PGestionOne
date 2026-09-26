package com.gestion.services;

import com.gestion.system.dto.response.DayResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.response.DayMapper;
import com.gestion.system.model.entities.Day;
import com.gestion.system.repositories.DaysRepository;
import com.gestion.system.service.DayServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DayServiceImplTest {

     @Mock
    private DaysRepository daysRepository;
     @Mock
    private DayMapper dayMapper;

     @InjectMocks
    private DayServiceImpl dayService;

     @Nested
     @DisplayName("getDaysByWeek() test")
    class GetDaysByWeekTest{

          @Test
          @DisplayName("getDaysByWeek: should return a mapped list when week have days to show.")
         void getDaysByWeek_DaysExist_ReturnDayResponseList(){
              Integer weekId = 3;
              List<Day> days = List.of(new Day(), new Day());
              List<DayResponse> expectedList = List.of(new DayResponse(), new DayResponse());

              when(daysRepository.findByWeek_Id(weekId)).thenReturn(days);
              when(dayMapper.listResponse(days)).thenReturn(expectedList);

              List<DayResponse> responseList = dayService.getDaysByWeek(weekId);

              assertNotNull(responseList);
              assertEquals(2, responseList.size());
              assertEquals(expectedList, responseList);
              verify(daysRepository, times(1)).findByWeek_Id(weekId);
        }

         @Test
         @DisplayName("getDaysByWeek: should return empty list when week don't have values.")
        void getDaysByWeek_NoDays_ReturnEmptyLIst(){
            Integer weekId = 20;

            when(daysRepository.findByWeek_Id(weekId)).thenReturn(List.of());
            when(dayMapper.listResponse(List.of())).thenReturn(List.of());

            List<DayResponse> responseList = dayService.getDaysByWeek(weekId);

            assertNotNull(responseList);
            assertTrue(responseList.isEmpty());
            verify(daysRepository, times(1)).findByWeek_Id(weekId);
         }

     }

     @Nested
     @DisplayName("findDay() and getDayResponseById() Test")
    class GetDayByIdTest{

          @Test
          @DisplayName("getDayResponseById: should return response when ID exit.")
         void getDayById_DayExist_ReturnResponse(){
              Day day = new Day();
              DayResponse dayResponse = new DayResponse();
              day.setId(5);

              when(daysRepository.findById(5)).thenReturn(Optional.of(day));
              when(dayMapper.toResponse(day)).thenReturn(dayResponse);

              DayResponse response = dayService.getDayResponseById(5);

              assertNotNull(response);
              assertEquals(dayResponse, response);
              verify(daysRepository, times(1)).findById(5);
          }

          @Test
          @DisplayName("findDay: should return a Day entity when ID exist.")
         void findDay_DayExist_ReturnDayEntity(){
              Day day = new Day();

              when(daysRepository.findById(30)).thenReturn(Optional.of(day));

              Day actualDay = dayService.findDay(30);

              assertNotNull(actualDay);
              assertEquals(day, actualDay);
              verify(daysRepository, times(1)).findById(30);
          }

          @Test
          @DisplayName("findDay: should throw ResoruceNotFoundException when ID isn't exit.")
         void findDay_DayNotExist_ThrowsException(){
              Integer id = 80;

              when(daysRepository.findById(id)).thenReturn(Optional.empty());

              assertThrows(ResourceNotFoundException.class,
                      () -> dayService.findDay(id));
              verify(daysRepository, times(1)).findById(id);
              verify(dayMapper, never()).toResponse(any());
          }
     }

     @Nested
     @DisplayName("getDayByNameAndWeek_Id() Test")
    class GetDayByNameAndWeekIdTest {

         @Test
         @DisplayName("getDayByNameAndWeek_Id: should return response when exist a day name and week id combination.")
         void getDayByNameAndWeekId_Succeeds() {
             String dayName = "Lunes";
             Integer weekId = 3;
             Day day = new Day();
             DayResponse dayResponse = new DayResponse();

             when(daysRepository.findByDayNameAndWeek_Id(dayName, weekId)).thenReturn(Optional.of(day));
             when(dayMapper.toResponse(day)).thenReturn(dayResponse);

             DayResponse actualResponse = dayService.getDayByNameAndWeek_Id(dayName, weekId);

             assertNotNull(actualResponse);
             assertEquals(dayResponse, actualResponse);
             verify(daysRepository, times(1)).findByDayNameAndWeek_Id(dayName, weekId);
             verify(dayMapper, times(1)).toResponse(day);
         }

          @Test
          @DisplayName("getDayByNameAndWeek_Id: throws ResourceNotFoundException when name and week id combination doesn't exist.")
         void getDayByNameAndWeekId_NotFound_ThrowsException() {
            String dayName = "Lunes";
            Integer weekId = 100;

              when(daysRepository.findByDayNameAndWeek_Id(dayName, weekId)).thenReturn(Optional.empty());

              assertThrows(ResourceNotFoundException.class,
                      () -> dayService.getDayByNameAndWeek_Id(dayName, weekId));
              verify(daysRepository, times(1)).findByDayNameAndWeek_Id(dayName, weekId);
              verify(dayMapper, never()).toResponse(any());
         }
     }

}

