package com.gestion.mappers;


import com.gestion.system.dto.request.create.UserRequest;
import com.gestion.system.dto.request.update.UserUpdateRequest;
import com.gestion.system.dto.response.UserResponse;
import com.gestion.system.mappers.request.UserMapper;
import com.gestion.system.mappers.update.UserUpdateMapper;
import com.gestion.system.mappers.update.UserUpdateMapperImpl;
import com.gestion.system.model.entities.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    private final UserUpdateMapper updateMapper = new UserUpdateMapperImpl();
    private final UserMapper mapper = new UserMapper(new ModelMapper());


    @Nested
    class ResponseTest {

        @Test
         void correctlyBindLastNames(){
            User entity = new User();
            entity.setId(2);
            entity.setPaternalLastName("Mejia");
            entity.setMaternalLastName("Hernandez");

            UserResponse dto = mapper.toResponse(entity);

            assertEquals("Mejia Hernandez", dto.getLastNames());
        }
    }

    @Nested
    class RequestTest{
        @Test
        void requestToEntityConvertLastNames(){
            UserRequest request = new UserRequest();
            request.setFirstLastName("Hernandez");
            request.setSecondLastName("Villalobos");

            User entity = mapper.requestToEntity(request);

            assertEquals("Hernandez", entity.getPaternalLastName());
            assertEquals("Villalobos", entity.getMaternalLastName());
        }
    }

    @Nested
     class UpdateTests {

        @Test
        void shouldMapLastNamesCorrectly() {

            User entity = new User();
            entity.setId(1);

            UserUpdateRequest dto = new UserUpdateRequest();
            dto.setFirstLastName("Lopez");
            dto.setSecondLastName("Villalobos");

            User result = updateMapper.updateEntity(dto, entity);

            assertEquals("Lopez", result.getPaternalLastName());
            assertEquals("Villalobos", result.getMaternalLastName());
        }

        @Test
        void shouldNotModifyId() {
            User entity = new User();
            entity.setId(10);

            UserUpdateRequest dto = new UserUpdateRequest();

            User result = updateMapper.updateEntity(dto, entity);

            assertEquals(10, result.getId());
        }

        @Test
        void shoulbMoodifySameInstance() {
            User entity = new User();

            User result = updateMapper.updateEntity(new UserUpdateRequest(), entity);

            assertSame(entity, result);
        }
    }

    @Nested
    class ListResponseTest{
        @Test
        void listResponseHandleCorrectly(){
            User user = User.builder()
                    .name("Martin")
                    .paternalLastName("Martinez")
                    .maternalLastName("Mejia")
                    .email("martinezmartin@mail.com")
                    .password("1234").build();

            List<UserResponse> resultList = mapper.listResponse(List.of(user));

            assertEquals(1, resultList.size());

            UserResponse dto = resultList.get(0);

            assertEquals("Martin", dto.getName());
            assertEquals("martinezmartin@mail.com", dto.getEmail());
            assertEquals("Martinez Mejia", dto.getLastNames());
        }

        @Test
        void shouldReturnEmptyListWithNull(){
            List<UserResponse> resultList = mapper.listResponse(null);

            assertNotNull(resultList);
            assertTrue(resultList.isEmpty());
        }
    }

    @Nested
    class ListRequestTest{
        @Test
        void listRequestHandleCorrectly(){
            UserRequest dto = UserRequest.builder()
                    .name("Martin")
                    .firstLastName("Martinez")
                    .secondLastName("Mejia")
                    .email("martinezmartin@mail.com").build();

            List<User> resultList  = mapper.listRequest(List.of(dto));

            assertEquals(1, resultList.size());

            User entity = resultList.get(0);

            assertEquals("Martin", entity.getName());
            assertEquals("martinezmartin@mail.com", entity.getEmail());
            assertEquals("Martinez", entity.getPaternalLastName());
        }

        @Test
        void shouldReturnEmptyListWithNull(){
            List<User> resultList = mapper.listRequest(null);

            assertNotNull(resultList);
            assertTrue(resultList.isEmpty());
        }
    }


}
