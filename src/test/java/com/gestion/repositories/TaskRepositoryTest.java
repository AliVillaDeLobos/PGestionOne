package com.gestion.repositories;

import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.enums.Colors;
import com.gestion.system.repositories.TasksRepository;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //Neceria si no intenta insertar los datos y salta una excepcion de Flyway
public class TaskRepositoryTest {
     @Autowired
    private TasksRepository tasksRepository;
     @Autowired
    private TestEntityManager entityManager;

    private Projects project;

     @BeforeEach
    void setUp() {
         project = new Projects();
         project.setName("Project1");
         project.setDescription("test");
         project.setStartDate(LocalDate.now().minusDays(20));
         project =  entityManager.persist(project);
     }

     @Test
     @DisplayName("findActiveTask: should return only active task with current date.")
    void findActiveTask_ShouldReturnActiveTask() {
         LocalDate today = LocalDate.now();

         Tasks activeTask = new Tasks();
         activeTask.setProjects(project);
         activeTask.setName("activeTask");
         activeTask.setStartDate(today.minusDays(2));
         activeTask.setEndDate(today.plusDays(2));
         activeTask.setColor(Colors.BLACK);
         entityManager.persist(activeTask);


         Tasks pastTask = new Tasks();
         pastTask.setProjects(project);
         pastTask.setName("pastTask");
         pastTask.setStartDate(today.minusDays(10));
         pastTask.setEndDate(today.minusDays(6));
         pastTask.setColor(Colors.BLUE);
         entityManager.persist(pastTask);

         entityManager.flush();
         entityManager.clear();

         List<Tasks> result = tasksRepository.findActiveTask(project.getId(), today);

         assertThat(result).hasSize(1).extracting(Tasks::getName).containsExactly("activeTask");
         assertThat(result.get(0).getStartDate()).isEqualTo(activeTask.getStartDate());
         }

         @Test
         @DisplayName("findBetweenDates: should return list of dates between the search dates.")
        void findBetweenDates_ShouldReturnList_MatchTask() {
            LocalDate stratDate = LocalDate.of(2026, 3, 20);
            LocalDate endDate = LocalDate.of(2026, 3, 30);

             Tasks activeTask = new Tasks();
             activeTask.setProjects(project);
             activeTask.setName("Between Dates");
             activeTask.setStartDate(LocalDate.of(2026, 3, 26));
             activeTask.setEndDate(LocalDate.of(2026, 10, 26));
             activeTask.setColor(Colors.BLACK);
             entityManager.persist(activeTask);


             Tasks pastTask = new Tasks();
             pastTask.setProjects(project);
             pastTask.setName("Not match with dates");
             pastTask.setStartDate(LocalDate.of(2026, 4, 26));
             pastTask.setEndDate(LocalDate.of(2026, 10, 26));
             pastTask.setColor(Colors.BLUE);
             entityManager.persist(pastTask);

             entityManager.flush();
             entityManager.clear();

             List<Tasks> result = tasksRepository.findBetweenDates(project.getId(), stratDate, endDate);

             assertThat(result).hasSize(1).extracting(Tasks::getName).containsExactly("Between Dates");
         }

}
