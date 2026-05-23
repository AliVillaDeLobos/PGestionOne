package com.gestion.mappers;

import com.gestion.system.dto.response.UserProjectRoleResponse;
import com.gestion.system.mappers.request.ProjectMapper;
import com.gestion.system.mappers.request.UserMapper;
import com.gestion.system.mappers.request.UserProjectMapper;
import com.gestion.system.mappers.request.UserProjectRoleMapper;
import com.gestion.system.mappers.response.RolesMapper;
import com.gestion.system.model.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

public class UserProjectRoleMapperTest {
    private ProjectMapper projectMapper;
    private UserMapper userMapper;
    private UserProjectMapper userProjectMapper;
    private RolesMapper rolesMapper;
    private UserProjectRoleMapper mapper;

    @BeforeEach
    void setUp() {
        projectMapper = new ProjectMapper(new ModelMapper());
        userMapper = new UserMapper(new ModelMapper());
        userProjectMapper = new UserProjectMapper(userMapper, projectMapper);
        rolesMapper = new RolesMapper();
        mapper = new UserProjectRoleMapper(userProjectMapper, rolesMapper);
    }

    @Nested
    class ResponseTests {
        @Test
        void shouldMapFieldsAndEntitiesToResponse() {
            User user = User.builder().name("Juan").build();
            Project project = Project.builder().name("Android App").build();
            UserProject userProject = UserProject.builder().id(1).user(user).project(project).build();
            Roles role = Roles.builder().roleName("Project Manager").build();
            UserProjectRole  entity = UserProjectRole.builder()
                    .id(10).userProject(userProject).role(role).build();

            UserProjectRoleResponse response =  mapper.toResponse(entity);

            assertEquals(10, response.getId());
            assertEquals("Juan", response.getUserProject().getUser().getName());
            assertEquals("Android App", response.getUserProject().getProject().getName());
            assertEquals("Project Manager", response.getRole().getRole());
        }
    }

    @Nested
    class RequestTests {
        @Test
        void shouldMapFieldsAndEntitiesToRequest() {
            User user = User.builder().name("Juan").build();
            Project project = Project.builder().name("Android App").build();
            UserProject userProject = UserProject.builder().id(1).user(user).project(project).build();
            Roles role = Roles.builder().roleName("Project Manager").build();

            UserProjectRole result = mapper.requestToEntity(userProject, role);

            assertEquals(1, result.getUserProject().getId());
            assertEquals("Juan", result.getUserProject().getUser().getName());
            assertEquals("Android App", result.getUserProject().getProject().getName());
            assertEquals("Project Manager", result.getRole().getRoleName());
        }

        @Test
        void shouldTrowExceptionWhenEntitiesAreNull(){
            assertThrows(IllegalArgumentException.class,
                    () -> mapper.requestToEntity(null, new Roles()));
            assertThrows(IllegalArgumentException.class,
                    () -> mapper.requestToEntity(new UserProject(), null));
        }
    }

    @Nested
    class ListResponseTest{
        @Test
        void shouldReturnEmptyListWhenIsNull(){
            assertNotNull(mapper.listResponse(null));
        }
    }

    @Nested
    class UpdateTest{
        @Test
        void shouldUpdateOnlyRole(){
            User user = User.builder().id(3).build();
            Project project = Project.builder().id(90).build();
            UserProject userProject = UserProject.builder().id(15).user(user).project(project).build();
            Roles role = Roles.builder().roleName("Project Manager").build();

            UserProjectRole entity = UserProjectRole.builder()
                    .id(20).userProject(userProject).role(role).build();

            Roles updateRole = Roles.builder().roleName("Developer").build();

            UserProjectRole result =  mapper.updateEntity(entity,  updateRole);

            assertEquals(20, result.getId());
            assertEquals("Developer", result.getRole().getRoleName());
            assertEquals(3, result.getUserProject().getUser().getId());
            assertEquals(90, result.getUserProject().getProject().getId());
            assertEquals(15, result.getUserProject().getId());
        }

    }


}
