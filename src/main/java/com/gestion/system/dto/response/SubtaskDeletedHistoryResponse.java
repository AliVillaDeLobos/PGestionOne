package com.gestion.system.dto.response;

import com.gestion.system.model.entities.Subtask;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubtaskDeletedHistoryResponse {

    private Integer id;
    private Subtask subtask;
    private String message;
    private LocalDate deletedDate;
}
