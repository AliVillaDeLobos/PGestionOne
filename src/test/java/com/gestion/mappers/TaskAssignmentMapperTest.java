package com.gestion.mappers;

import com.gestion.system.dto.request.create.TaskAssignmentRequest;
import com.gestion.system.dto.request.update.TaskAssignmentUpdateRequest;
import com.gestion.system.dto.response.TaskAssignmentResponse;
import com.gestion.system.mappers.request.ProjectMapper;
import com.gestion.system.mappers.request.TaskAssignmentMapper;
import com.gestion.system.mappers.request.TaskMapper;
import com.gestion.system.mappers.request.UserMapper;
import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.ProjectRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskAssignmentMapperTest {
    private TaskMapper taskMapper;
    private UserMapper userMapper;
    private TaskAssignmentMapper mapper;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        taskMapper = new TaskMapper(modelMapper, new ProjectMapper(modelMapper));
        userMapper = new UserMapper(modelMapper);
        mapper = new TaskAssignmentMapper(modelMapper, taskMapper, userMapper);
    }

    @Nested
    class ResponseTest{
        @Test
        void shouldMapExternalResponseEntitiesCorrectly() {
            Tasks tasks = Tasks.builder().id(3).name("task 1").build();
            User user =  User.builder().id(5).name("UsrOne").build();

            TaskAssignment entity = new TaskAssignment();
            entity.setId(1);
            entity.setUser(user);
            entity.setTask(tasks);

            TaskAssignmentResponse response = mapper.toResponse(entity);

            assertNotNull(response);
            assertNotNull(response.getTask());
            assertEquals("UsrOne",  response.getUser().getName());
        }
    }

    @Nested
    class RequestTest{
        @Test
        void shouldMapExternalEntitiesCorrectly() {
            Tasks tasks = Tasks.builder().id(3).name("task 1").build();
            User user =  User.builder().id(5).name("UsrOne").build();

            TaskAssignmentRequest request = new TaskAssignmentRequest();
            request.setProjectRoles(ProjectRoles.SUPPORT);

            TaskAssignment result = mapper.requestToEntity(request, tasks, user);

            assertNotNull(result);
            assertEquals("task 1", result.getTask().getName());
            assertEquals(5, result.getUser().getId());
            assertEquals(ProjectRoles.SUPPORT, result.getProjectRoles());
        }

        @Test
        void shouldHandleExternalEntitiesNull(){
            assertThrows(IllegalArgumentException.class,
                    () -> mapper.requestToEntity(new TaskAssignmentRequest(), null, new User()));
            assertThrows(IllegalArgumentException.class,
                    () -> mapper.requestToEntity(new TaskAssignmentRequest(), new Tasks(), null));
        }

        @Test
        void shouldReturnNullWhenRequestIsNull(){
            assertNull(mapper.requestToEntity(null, new Tasks(), new User()));
        }
    }

    @Nested
    class ListResponseTest{
        @Test
        void shouldReturnEmptyListNotNull(){
            List<TaskAssignmentResponse> list = mapper.listResponse(null);
            assertNotNull(list);
        }
    }

    @Nested
    class UpdateTest{
        @Test
        void shouldUpdateOnlyRoleField(){
            Tasks tasks = Tasks.builder().id(3).name("task 1").build();
            User user =  User.builder().id(5).name("UsrOne").build();
            TaskAssignment entity = TaskAssignment.builder()
                    .id(20).projectRoles(ProjectRoles.REVIEWER).task(tasks).user(user).build();

            TaskAssignmentUpdateRequest updateRequest = new TaskAssignmentUpdateRequest();
            updateRequest.setProjectRoles(ProjectRoles.SUPPORT);

            TaskAssignment result = mapper.updateEntity(updateRequest, entity);

            assertEquals(ProjectRoles.SUPPORT, result.getProjectRoles());
            assertEquals(20, result.getId());
            assertEquals("UsrOne", result.getUser().getName());
            assertEquals(3, result.getTask().getId());
        }
    }

}
