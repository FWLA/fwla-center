package de.ihrigb.fwla.fwlacenter.persistence.jpa;

import java.io.IOException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.geojson.FeatureCollection;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter(autoApply = true)
public class FeatureCollectionConverter implements AttributeConverter<FeatureCollection, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(FeatureCollection attribute) {
		if (attribute == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (IOException e) {
			log.error("Excetion while serializing feature collection for database storage.", e);
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}

	@Override
	public FeatureCollection convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		try {
			return objectMapper.readValue(dbData, FeatureCollection.class);
		} catch (IOException e) {
			log.error("Exception while deserializing feature collection from database storage.", e);
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}
}
