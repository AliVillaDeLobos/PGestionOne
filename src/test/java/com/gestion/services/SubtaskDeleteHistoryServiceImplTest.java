package com.gestion.services;

import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.model.entities.Subtask;
import com.gestion.system.model.entities.SubtaskDeletedHistory;
import com.gestion.system.repositories.SubtaskDeletedHistoryRepository;
import com.gestion.system.service.SubtaskDeletedHistoryServiceImpl;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubtaskDeleteHistoryServiceImplTest {
     @Mock
    private  SubtaskDeletedHistoryRepository deletedHistoryRepository;

     @InjectMocks
    SubtaskDeletedHistoryServiceImpl deletedHistoryService;

     @Captor
    private ArgumentCaptor<SubtaskDeletedHistory> historyCaptor;

    private Subtask subtaskSample;
    private SubtaskDeletedHistory historySample;

     @BeforeEach
    void setUp() {
          subtaskSample = new Subtask();
          subtaskSample.setId(10);

          historySample = new SubtaskDeletedHistory();
          historySample.builder()
                  .id(1)
                  .subtask(subtaskSample)
                  .message("Deleted subtask")
                  .deletedDate(LocalDate.now())
                  .build();
      }

     @Nested
     @DisplayName("registerDeletion() Test")
    class RegisterDeletionTest {
          @Test
          @DisplayName("registerDelation: should build entity and save.")
         void registerDeletion_ShouldSaveHistory_WhenValidData() {
              String message = "Subtarea completada";

              deletedHistoryService.registerDeletion(subtaskSample, message);

              verify(deletedHistoryRepository, times(1)).save(historyCaptor.capture());

              SubtaskDeletedHistory captured = historyCaptor.getValue();
              assertThat(captured).isNotNull();
              assertThat(captured.getSubtask()).isEqualTo(subtaskSample);
              assertThat(captured.getMessage()).isEqualTo(message);
              assertThat(captured.getDeletedDate()).isEqualTo(LocalDate.now());
          }
     }

     @Nested
     @DisplayName("restore() Test")
    class RestoreTest {
          @Test
          @DisplayName("restore: should delete history when exist data.")
         void restore_ShouldDeleteHistory_WhenValidData() {
              when(deletedHistoryRepository.findBySubtask_Id(10)).thenReturn(Optional.of(historySample));

              deletedHistoryService.restore(10);

              verify(deletedHistoryRepository, times(1)).findBySubtask_Id(10);
              verify(deletedHistoryRepository, times(1)).delete(historySample);
          }

          @Test
          @DisplayName("restore: throws excpetion when no data with ID")
         void restore_ShouldThrowException_WhenNoFound() {
              when(deletedHistoryRepository.findBySubtask_Id(10)).thenReturn(Optional.empty());

              assertThatThrownBy(() -> deletedHistoryService.restore(10))
                      .isInstanceOf(ResourceNotFoundException.class);
              verify(deletedHistoryRepository, never()).delete(any());
          }
     }

}
