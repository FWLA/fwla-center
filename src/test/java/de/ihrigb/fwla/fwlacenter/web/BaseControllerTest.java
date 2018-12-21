package de.ihrigb.fwla.fwlacenter.web;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.ihrigb.fwla.fwlacenter.web.model.DataResponse;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BaseControllerTest {

	private BaseController<Entity, String, EntityDTO> testee;

	private JpaRepository<Entity, String> repository;

	@Before
	public void setUp() {
		repository = mock(JpaRepository.class);

		testee = new BaseController<Entity, String, EntityDTO>(repository) {

			@Override
			protected Function<? super Entity, ? extends EntityDTO> getToDTOFunction() {
				return entity -> new EntityDTO(entity);
			}

			@Override
			protected String getId(Entity t) {
				return t.getId();
			}

			@Override
			protected Function<? super EntityDTO, ? extends Entity> getFromDTOFunction() {
				return dto -> dto.toPersistenceModel();
			}
		};
	}

	@Test
	public void testDoGetAll() throws Exception {

		Entity entity = new Entity();
		entity.setId("id");
		entity.setName("name");

		expect(repository.findAll()).andReturn(Collections.singletonList(entity));

		replay(repository);
		ResponseEntity<DataResponse<List<EntityDTO>>> response = testee.doGetAll();
		assertEquals(HttpStatus.OK, response.getStatusCode());

		DataResponse<List<EntityDTO>> dataResponse = response.getBody();
		assertEquals(1, dataResponse.getTotal().longValue());

		List<EntityDTO> entities = dataResponse.getData();
		assertEquals(1, entities.size());

		EntityDTO dto = entities.stream().findFirst().get();
		assertEquals("id", dto.getId());
		assertEquals("name", dto.getName());

		verify(repository);
	}

	@Data
	public static class Entity {
		private String id;
		private String name;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class EntityDTO {
		private String id;
		private String name;

		public EntityDTO(Entity entity) {
			this.id = entity.getId();
			this.name = entity.getName();
		}

		public Entity toPersistenceModel() {
			Entity entity = new Entity();
			entity.setId(id);
			entity.setName(name);
			return entity;
		}
	}
}
