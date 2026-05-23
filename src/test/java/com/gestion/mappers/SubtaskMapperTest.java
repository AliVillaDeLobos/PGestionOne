package com.gestion.mappers;

import com.gestion.system.dto.request.create.SubtaskRequest;
import com.gestion.system.dto.request.update.SubtaskUpdateRequest;
import com.gestion.system.dto.response.SubtaskResponse;
import com.gestion.system.mappers.request.ProjectMapper;
import com.gestion.system.mappers.request.SubtaskMapper;
import com.gestion.system.mappers.request.TaskMapper;
import com.gestion.system.mappers.update.SubtaskUpdateMapper;
import com.gestion.system.model.entities.Subtask;
import com.gestion.system.model.entities.Tasks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SubtaskMapperTest {

    private ProjectMapper projectMapper;
    private TaskMapper taskMapper;
    private SubtaskMapper mapper;
    private SubtaskUpdateMapper updateMapper;

    @BeforeEach
    void setUp(){
        projectMapper = new ProjectMapper(new ModelMapper());
        taskMapper = new TaskMapper(new ModelMapper(), projectMapper);
        mapper = new SubtaskMapper(new ModelMapper(), taskMapper);

        updateMapper = Mappers.getMapper(SubtaskUpdateMapper.class);
    }

    @Nested
    class ResponseTest {
        @Test
        void shouldMapBasicsFields() {
            Subtask subtask = Subtask.builder().id(10).name("Debugging UserService").build();

            SubtaskResponse response = mapper.toResponse(subtask);

            assertNotNull(response);
            assertEquals(10, response.getId());
            assertEquals("Debugging UserService", response.getName());
        }

        @Test
        void shouldMapExternalEntityCorrectly() {
            Tasks task = Tasks.builder().id(3).name("Debugging all Services").build();
            Subtask subtask = new Subtask();
            subtask.setTask(task);

            SubtaskResponse response = mapper.toResponse(subtask);

            assertNotNull(response.getTask());
            assertEquals(3, response.getTask().getId());
            assertEquals("Debugging all Services", response.getTask().getName());
        }
    }

        @Nested
        class ListsTests{
            @Test
            void shouldReturnEmptyListWithOutputNullAndEmpty(){
                List<SubtaskResponse> response = mapper.listResponse(null);

                assertNotNull(response);
                assertTrue(response.isEmpty());
            }
            @Test
            void shouldReturnEmptyListRequest(){
                List<Subtask> response = mapper.listRequest(null, new Tasks());

                assertNotNull(response);
                assertTrue(response.isEmpty());
            }
            @Test
            void shouldHandleTaskNull(){
                assertThrows(IllegalArgumentException.class,
                        () -> mapper.listRequest(List.of(new SubtaskRequest()), null));

            }
        }

        @Nested
        class RequestTest {
            @Test
            void shouldMapInjectionExternalEntityCorrectly(){
                Tasks task = Tasks.builder().id(5).name("Debugging all Controllers").build();
                SubtaskRequest request = SubtaskRequest.builder().name("Debugging UserController").build();

                Subtask result = mapper.requestToEntity(request, task);

                assertNotNull(result.getTask());
                assertEquals(5, result.getTask().getId());
            }

            @Test
            void shouldHandleTaskNull(){
                assertThrows(IllegalArgumentException.class,
                        () -> mapper.requestToEntity(new SubtaskRequest(), null));
            }
        }

    @Nested
    class UpdateTest{
        @Test
        void shouldNotUpdateId(){
            Subtask subtask = Subtask.builder().id(12).completed(false).build();

            SubtaskUpdateRequest update = SubtaskUpdateRequest.builder().completed(true).build();

            Subtask result = updateMapper.updateEntity(update, subtask);

            assertNotNull(result);
            assertEquals(12, result.getId());
            assertEquals(true, result.getCompleted());
        }
    }

}
