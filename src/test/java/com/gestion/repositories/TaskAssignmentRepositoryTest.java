package com.gestion.repositories;

import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.ProjectRoles;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.TaskAssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskAssignmentRepositoryTest {
     @Autowired
    private TestEntityManager entityManager;
     @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;

     private User user1;
     private User user2;
     private Projects project1;
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

        project1 = Projects.builder()
                .name("Project 1")
                .startDate(LocalDate.now().minusDays(4))
                .description("Project 1 Test")
                .build();
        entityManager.persist(project1);

        task1 = Tasks.builder()
                .projects(project1)
                .color(Colors.BLUE)
                .name("Task 1 test")
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(10))
                .build();
        entityManager.persist(task1);
    }

     @Test
     @DisplayName("findAllByProject: should return all assignments for given project ID.")
    void findAllByProject_ShouldReturnAllAssignmentsForProject(){
         TaskAssignment taskAssignment1 = TaskAssignment.builder()
                 .task(task1)
                 .user(user2)
                 .assignedDate(LocalDate.now())
                 .projectRoles(ProjectRoles.RESPONSIBLE)
                 .build();
         entityManager.persist(taskAssignment1);

         entityManager.flush();
         entityManager.clear();

         List<TaskAssignment> result = taskAssignmentRepository.findAllByProject(project1.getId());

         assertThat(result).isNotNull().hasSize(1)
                 .extracting(ta -> ta.getUser().getId()).containsExactly(user2.getId());
     }

     @Test
     @DisplayName("finAllByUser: should return assignments specific to a user ID.")
    void finAllByUser_ShouldReturnAssignmentsForUser(){
         TaskAssignment taskAssignmentUser1 = TaskAssignment.builder()
                 .task(task1)
                 .user(user1)
                 .projectRoles(ProjectRoles.RESPONSIBLE)
                 .assignedDate(LocalDate.now().minusDays(1))
                 .build();
         entityManager.persist(taskAssignmentUser1);

         entityManager.flush();
         entityManager.clear();

         List<TaskAssignment> result = taskAssignmentRepository.findAllByUser(user1.getId());

         assertThat(result).isNotNull().hasSize(1)
                 .extracting(ta -> ta.getTask().getId()).containsExactly(task1.getId());
     }

     @Test
     @DisplayName("findAllByTask: should return assignment specific to a task ID.")
    void findAllByTask_ShouldReturnAssignmentsForTask(){
         TaskAssignment taskAssignment = TaskAssignment.builder()
                 .task(task1)
                 .user(user2)
                 .projectRoles(ProjectRoles.RESPONSIBLE)
                 .assignedDate(LocalDate.now().minusDays(1))
                 .build();
         entityManager.persist(taskAssignment);

         entityManager.flush();
         entityManager.clear();

         List<TaskAssignment> result = taskAssignmentRepository.findAllByTask(task1.getId());

         assertThat(result).isNotNull().hasSize(1)
                 .extracting(ta -> ta.getUser().getId()).containsExactly(user2.getId());
     }

}
