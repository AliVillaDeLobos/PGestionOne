package com.gestion.mappers;

import com.gestion.system.dto.response.UserProjectResponse;
import com.gestion.system.mappers.request.ProjectMapper;
import com.gestion.system.mappers.request.UserMapper;
import com.gestion.system.mappers.request.UserProjectMapper;
import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.entities.UserProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

public class UserProjectMapperTest {
    private ProjectMapper projectMapper;
    private UserMapper userMapper;
    private UserProjectMapper mapper;

    @BeforeEach
    void setUp(){
        projectMapper = new ProjectMapper(new ModelMapper());
        userMapper = new UserMapper(new ModelMapper());
        mapper = new UserProjectMapper(userMapper, projectMapper);
    }

    @Nested
    class ResponseTest{
        @Test
        void shouldMapEntitiesToResponseCorrectly(){
            User user = User.builder().id(8).name("Aldo").build();
            Projects projects = Projects.builder().id(2).name("Volaris Application").build();
            UserProject entity = UserProject.builder().id(1).user(user).projects(projects).build();

            UserProjectResponse response = mapper.toResponse(entity);

            assertEquals(1, response.getId());
            assertEquals("Aldo", response.getUser().getName());
            assertEquals("Volaris Application", response.getProject().getName());
        }
    }

    @Nested
    class RequestTest{
        @Test
        void  shouldMapEntitiesToRequestCorrectly(){
            User user = User.builder().id(8).name("Aldo").build();
            Projects projects = Projects.builder().id(2).name("Volaris Application").build();

            UserProject result = mapper.requestToEntity(user, projects);

            assertNotNull(result);
            assertEquals(8, result.getUser().getId());
            assertEquals(2, result.getProjects().getId());
        }

        @Test
        void shouldThrowExceptionWhenEntityIsNull(){
            assertThrows(IllegalArgumentException.class,
                    () -> mapper.requestToEntity(null, new Projects()));
        }
    }

    @Nested
    class ListTest{
        @Test
        void shouldReturnEmptyListWhenNull(){
            assertTrue(mapper.listResponse(null).isEmpty());
        }
    }

}
