package de.ihrigb.fwla.fwlacenter.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
public class DataResponse<T> {

	private final T data;
	private final Long total;

	public DataResponse() {
		this(null, null);
	}

	public DataResponse(T data) {
		this(data, null);
	}

	public DataResponse(T data, Long total) {
		this.data = data;
		this.total = total;
	}
}
