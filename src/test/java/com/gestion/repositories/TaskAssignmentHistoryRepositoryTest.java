package com.gestion.repositories;

import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.TaskAssignmentHistory;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.Action;
import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.ProjectRoles;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.TaskAssignmentHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskAssignmentHistoryRepositoryTest {
     @Autowired
    private TestEntityManager entityManager;
     @Autowired
    private TaskAssignmentHistoryRepository taskAssignmentHistoryRepository;

    private User user1;
    private User user2;
    private Projects project;
    private Tasks task1;

     @BeforeEach
    void setUp() {
         user1 = User.builder()
                 .name("User1")
                 .password("password1")
                 .email("user1@mail.com")
                 .paternalLastName("Perez")
                 .maternalLastName("hernandez")
                 .systemRole(SystemRole.ADMIN)
                 .build();
         entityManager.persist(user1);

         user2 = User.builder()
                 .name("User2")
                 .password("password2")
                 .email("user2@mail.com")
                 .paternalLastName("Gomez")
                 .maternalLastName("Piña")
                 .systemRole(SystemRole.MANAGER)
                 .build();
         entityManager.persist(user2);

         project = Projects.builder()
                 .name("Project 1")
                 .startDate(LocalDate.now().minusDays(4))
                 .description("Project 1 Test")
                 .build();
         entityManager.persist(project);

         task1 = Tasks.builder()
                 .projects(project)
                 .color(Colors.BLUE)
                 .name("Task 1 test")
                 .startDate(LocalDate.now().minusDays(10))
                 .endDate(LocalDate.now().plusDays(10))
                 .build();
         entityManager.persist(task1);
     }

     @Test
     @DisplayName("findAllByProjectAndAction: should return list for project and action.")
    void findAllByProjectAndAction_ShouldReturnOrderHistory() {
         LocalDateTime now = LocalDateTime.now();

         TaskAssignmentHistory oldHistory = TaskAssignmentHistory.builder()
                 .task(task1)
                 .userAssignedBy(user1)
                 .userAssigned(user2)
                 .action(Action.ASSIGNED)
                 .actionDate(now.minusDays(3))
                 .role(ProjectRoles.RESPONSIBLE)
                 .build();
         entityManager.persist(oldHistory);

         TaskAssignmentHistory newHistory = TaskAssignmentHistory.builder()
                 .task(task1)
                 .userAssignedBy(user1)
                 .userAssigned(user2)
                 .action(Action.ASSIGNED)
                 .actionDate(now)
                 .role(ProjectRoles.RESPONSIBLE)
                 .build();
         entityManager.persist(newHistory);

         TaskAssignmentHistory discardedHistory = TaskAssignmentHistory.builder()
                 .task(task1)
                 .userAssignedBy(user1)
                 .userAssigned(user2)
                 .action(Action.UNASSIGNED)
                 .actionDate(now.minusDays(1))
                 .role(ProjectRoles.RESPONSIBLE)
                 .build();
         entityManager.persist(discardedHistory);

         entityManager.flush();
         entityManager.clear();

         List<TaskAssignmentHistory> result = taskAssignmentHistoryRepository.
                 findAllByProjectAndAction(project.getId(), Action.ASSIGNED);

         assertThat(result).isNotNull().hasSize(2)
                 .extracting(TaskAssignmentHistory::getId).containsExactly(newHistory.getId(), oldHistory.getId());
     }

     @Test
     @DisplayName("findAllByTaskAndAction: should return history entries for specific task and action.")
    void findAllByTaskAndAction_ShouldReturnHistoryInDescOrder() {
         LocalDateTime now = LocalDateTime.now();

         TaskAssignmentHistory oldHistory = TaskAssignmentHistory.builder()
                 .task(task1)
                 .userAssignedBy(user1)
                 .userAssigned(user2)
                 .action(Action.ASSIGNED)
                 .actionDate(now.minusDays(3))
                 .role(ProjectRoles.RESPONSIBLE)
                 .build();
         entityManager.persist(oldHistory);

         TaskAssignmentHistory newHistory = TaskAssignmentHistory.builder()
                 .task(task1)
                 .userAssignedBy(user1)
                 .userAssigned(user2)
                 .action(Action.ASSIGNED)
                 .actionDate(now)
                 .role(ProjectRoles.RESPONSIBLE)
                 .build();
         entityManager.persist(newHistory);

         Tasks discardedTask = Tasks.builder()
                 .projects(project)
                 .color(Colors.BLACK)
                 .name("Task discarded test")
                 .startDate(LocalDate.now().minusDays(10))
                 .endDate(LocalDate.now().plusDays(10))
                 .build();
         entityManager.persist(discardedTask);

         TaskAssignmentHistory discardedHistory = TaskAssignmentHistory.builder()
                 .task(discardedTask)
                 .userAssignedBy(user1)
                 .userAssigned(user2)
                 .action(Action.ASSIGNED)
                 .actionDate(now.minusDays(1))
                 .role(ProjectRoles.RESPONSIBLE)
                 .build();
         entityManager.persist(discardedHistory);

         entityManager.flush();
         entityManager.clear();

         List<TaskAssignmentHistory> result = taskAssignmentHistoryRepository.
                 findAllByTaskAndAction(task1.getId(), Action.ASSIGNED);

         assertThat(result).isNotNull().hasSize(2)
                 .extracting(TaskAssignmentHistory::getId).containsExactly(newHistory.getId(), oldHistory.getId());

     }

     @Test
     @DisplayName("findAllByUserAndAction: should return history entries for a specific user and action.")
    void findAllByUserAndAction_ShouldReturnHistoryInDescOrder() {
         LocalDateTime now = LocalDateTime.now();

         TaskAssignmentHistory oldHistory = TaskAssignmentHistory.builder()
                 .task(task1)
                 .userAssignedBy(user1)
                 .userAssigned(user2)
                 .action(Action.ASSIGNED)
                 .actionDate(now.minusDays(3))
                 .role(ProjectRoles.RESPONSIBLE)
                 .build();
         entityManager.persist(oldHistory);

         TaskAssignmentHistory newHistory = TaskAssignmentHistory.builder()
                 .task(task1)
                 .userAssignedBy(user1)
                 .userAssigned(user2)
                 .action(Action.ASSIGNED)
                 .actionDate(now)
                 .role(ProjectRoles.RESPONSIBLE)
                 .build();
         entityManager.persist(newHistory);

         TaskAssignmentHistory discardedHistory = TaskAssignmentHistory.builder()
                 .task(task1)
                 .userAssignedBy(user2)
                 .userAssigned(user1)
                 .action(Action.ASSIGNED)
                 .actionDate(now.minusDays(1))
                 .role(ProjectRoles.RESPONSIBLE)
                 .build();
         entityManager.persist(discardedHistory);

         entityManager.flush();
         entityManager.clear();

         List<TaskAssignmentHistory> result = taskAssignmentHistoryRepository
                 .findAllByUserAndAction(user2.getId(), Action.ASSIGNED);

         assertThat(result).isNotNull().hasSize(2)
                 .extracting(TaskAssignmentHistory::getId).containsExactly(newHistory.getId(), oldHistory.getId());
     }
}
