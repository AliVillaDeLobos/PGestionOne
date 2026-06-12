package com.gestion.mappers;

import com.gestion.system.dto.request.create.TaskRequest;
import com.gestion.system.dto.request.update.TaskUpdateRequest;
import com.gestion.system.dto.response.TaskResponse;
import com.gestion.system.mappers.request.ProjectMapper;
import com.gestion.system.mappers.request.TaskMapper;
import com.gestion.system.mappers.update.TaskUpdateMapper;
import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.enums.Colors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskMapperTest {
    private ProjectMapper projectMapper;
    private TaskMapper mapper;
    private TaskUpdateMapper  updateMapper;

    @BeforeEach
    void setUp() {
        projectMapper = new ProjectMapper(new ModelMapper());
        mapper = new TaskMapper(new ModelMapper(),  projectMapper);

        updateMapper = Mappers.getMapper(TaskUpdateMapper.class);
    }

    @Nested
    class ResponseTest{
        @Test
        void shouldMapProjectAndFieldsCorrectly() {
            Projects projects = Projects.builder().id(4).name("ecommerce").build();
            Tasks task = Tasks.builder().id(10).projects(projects).build();

            TaskResponse response = mapper.toResponse(task);

            assertEquals(10, response.getId());
            assertEquals("ecommerce", response.getProject().getName());
        }
    }

    @Nested
    class RequestTest{
        @Test
        void shouldMapProjectEntityCorrectly() {
            Projects projects = Projects.builder().id(4).name("ecommerce").build();
            TaskRequest request = TaskRequest.builder().color(Colors.BLACK).build();

            Tasks result = mapper.requestToEntity(request, projects);

            assertEquals(4, result.getProjects().getId());
            assertEquals(Colors.BLACK, result.getColor());
        }
        @Test
        void shouldHandleProjectNullWithException(){
            TaskRequest request = TaskRequest.builder().color(Colors.BLACK).build();

            assertThrows(IllegalArgumentException.class,
                    () -> mapper.requestToEntity(request, null));
        }
    }

    @Nested
    class UpdateTest{
        @Test
        void shouldNotUpdateId(){
            Projects projects = Projects.builder().id(4).name("ecommerce").build();
            Tasks entity = Tasks.builder().id(7).color(Colors.BLACK).projects(projects).build();
            TaskUpdateRequest update = TaskUpdateRequest.builder().color(Colors.BLUE).build();

            Tasks result = updateMapper.updateEntity(update, entity);

            assertEquals(7, result.getId());
            assertEquals(4, result.getProjects().getId());
            assertEquals(Colors.BLUE, result.getColor());
        }
    }

    @Nested
    class ListsTest{
        @Test
        void shouldHandleNullReturningEmptyList(){
            assertNotNull(mapper.listResponse(null));
            assertNotNull(mapper.listEntity(null, new Projects()));
        }

        @Test
        void shouldThrowExceptionWhenProjectIsNull(){
            assertThrows(IllegalArgumentException.class,
                    () -> mapper.listEntity(List.of(), null));
        }
    }

}
