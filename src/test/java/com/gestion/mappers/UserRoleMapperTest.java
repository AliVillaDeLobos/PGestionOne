package com.gestion.mappers;

import com.gestion.system.mappers.request.UserMapper;
import com.gestion.system.mappers.response.RolesMapper;
import com.gestion.system.model.entities.Roles;
import com.gestion.system.model.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

public class UserRoleMapperTest {
    private UserMapper userMapper;
    private RolesMapper roleMapper;
    private UserRoleMapper mapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper(new ModelMapper());
        roleMapper = new RolesMapper();
        mapper =new UserRoleMapper(userMapper, roleMapper);
    }

    @Nested
    class ResponseTest{
        @Test
        void shouldMapEntitiesToResponseCorrectly() {
            User user = User.builder().id(7).name("Pedro").build();
            Roles role = Roles.builder().id(10).roleName("Project Manager").build();
            UserRole entity = UserRole.builder().id(1).role(role).user(user).build();

            UserRoleResponse response = mapper.toResponse(entity);

            assertEquals(1, response.getId());
            assertEquals("Pedro", response.getUser().getName());
            assertEquals("Project Manager", response.getRole().getRole());
        }
    }

    @Nested
    class RequestTest{
        @Test
        void shouldMapRequestToEntityCorrectly() {
            User user = User.builder().id(7).name("Pedro").build();
            Roles role = Roles.builder().id(10).roleName("Project Manager").build();

            UserRole result = mapper.requestToEntity(user, role);

            assertNotNull(result);
            assertEquals(7, result.getUser().getId());
            assertEquals(10, result.getRole().getId());
        }

        @Test
        void shouldThrowExceptionWhenEntitiesAreNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> mapper.requestToEntity(null, new Roles()));
            assertThrows(IllegalArgumentException.class,
                    () -> mapper.requestToEntity(new User(), null));
        }
    }

    @Nested
    class ListResponseTest{
        @Test
        void shouldReturnEmptyListWhenNull(){
            assertTrue(mapper.listResponse(null).isEmpty());
        }
    }


}
