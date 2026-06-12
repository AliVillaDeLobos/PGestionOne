package com.gestion.mappers;

import com.gestion.system.dto.response.TaskAssignmentHistoryResponse;
import com.gestion.system.mappers.request.ProjectMapper;
import com.gestion.system.mappers.request.TaskMapper;
import com.gestion.system.mappers.request.UserMapper;
import com.gestion.system.mappers.response.TaskAssignmentHistoryMapper;
import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.TaskAssignmentHistory;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskAssignmentHistoryMapperTest {
    private UserMapper userMapper;
    private TaskMapper taskMapper;
    private TaskAssignmentHistoryMapper mapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper(new ModelMapper());
        taskMapper = new TaskMapper(new ModelMapper(), new ProjectMapper(new ModelMapper()));
        mapper = new TaskAssignmentHistoryMapper(userMapper, taskMapper);
    }

    @Nested
    class ResponseTest{
        @Test
        void shouldMapExternalEntitiesToResponse(){
            User userOne = User.builder().name("Pedro").build();
            User userTwo = User.builder().name("Martin").build();
            Projects projects = Projects.builder().id(50).build();
            Tasks task = Tasks.builder().id(3).projects(projects).build();

            TaskAssignmentHistory entity = TaskAssignmentHistory.builder()
                    .id(200).userAssigned(userOne).userAssignedBy(userTwo).task(task).build();

            TaskAssignmentHistoryResponse response = mapper.toResponse(entity);

            assertEquals(200, response.getId());
            assertEquals("Pedro", response.getUserAssigned().getName());
            assertEquals("Martin", response.getUserAssignedBy().getName());
            assertEquals(3, response.getTask().getId());
            assertEquals(50, response.getTask().getProject().getId());
        }
    }

    @Nested
    class ListResponseTest{
        @Test
        void shouldReturnEmptyListWhenIsNull(){
            assertTrue(mapper.listResponse(null).isEmpty());
        }
    }


}
