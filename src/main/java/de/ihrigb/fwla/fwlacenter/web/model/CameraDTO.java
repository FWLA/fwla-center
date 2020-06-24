package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.ihrigb.commons.Assert;
import de.ihrigb.fwla.fwlacenter.persistence.model.Camera;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CameraDTO {

	private String id;
	private String name;
	private String uri;

	public CameraDTO(Camera camera) {
		Assert.notNull(camera, "Camera must not be null.");
		this.id = camera.getId();
		this.name = camera.getName();
		this.uri = camera.getUri();
	}

	@JsonIgnore
	public Camera getPersistenceModel() {
		Camera camera = new Camera();
		camera.setId(this.id);
		camera.setName(this.name);
		camera.setUri(this.uri);
		return camera;
	}
}
