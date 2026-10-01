package com.gestion.system.model.entities;

import com.gestion.system.model.enums.Operation;
import com.gestion.system.validations.ValidAudit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ValidAudit
@Table(name = "audit")
public class Audit {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_audit")
    private Long id;

     @Column(name = "table_name", nullable = false)
    private String tableName;

     @Column(name = "record_id", nullable = false)
    private Integer recordId;

     @Enumerated(EnumType.STRING)
     @Column (name= "operation", nullable = false)
    private Operation operation;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn( name = "id_user_created", nullable = false)
    private User userCreated;

     @JdbcTypeCode(SqlTypes.JSON)
     @Column(columnDefinition = "LONGTEXT")
    private JsonNode oldData;

     @JdbcTypeCode(SqlTypes.JSON)
     @Column(columnDefinition = "LONGTEXT")
    private JsonNode newData;

     @CreationTimestamp //El campo será llenado cuando entre en la DB en automatico
     @Column(updatable = false)
    private LocalDateTime createdDate;

}
