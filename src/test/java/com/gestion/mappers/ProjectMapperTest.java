package com.gestion.mappers;


import com.gestion.system.dto.request.update.ProjectUpdateRequest;
import com.gestion.system.dto.response.ProjectResponse;
import com.gestion.system.mappers.request.ProjectMapper;
import com.gestion.system.mappers.update.ProjectUpdateMapper;
import com.gestion.system.model.entities.Project;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectMapperTest {
    private ModelMapper modelMapper;
    private ProjectMapper mapper;
    private ProjectUpdateMapper updateMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        mapper = new ProjectMapper(modelMapper);
        updateMapper = Mappers.getMapper(ProjectUpdateMapper.class);
    }

    @Nested
    class ResponseTest {
        @Test
        void shouldMapToResponseCorrectly() {

            Project project = Project.builder().id(10).name("Plants ecommerce").build();
            ProjectResponse response = mapper.toResponse(project);

            assertNotNull(response);
            assertEquals("Plants ecommerce", response.getName());
            assertEquals(project.getId(), response.getId());
        }
    }

    @Nested
    class ListsTests{
        @Test
        void shouldReturnEmptyListWhenListIsNull() {
            assertNotNull(mapper.listResponse(null));
            assertTrue(mapper.listResponse(null).isEmpty());
        }
    }

    @Nested
    class UpdateMapper{
        @Test
        void shouldNotUpdateId(){
            Project entity = new Project();
                entity.setId(25);
                entity.setName("Old");

                ProjectUpdateRequest dto = new ProjectUpdateRequest();
                dto.setName("New");

                updateMapper.updateEntity(dto, entity);

                assertEquals(25, entity.getId());
                assertEquals("New", entity.getName());
            }
    }

}
