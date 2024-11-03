package br.com.ronaldomatias.cachemanager.annotation.dto;

import lombok.Data;

@Data
public class AnnotationFieldsValueDTO {

	private String key;

	private Long ttl;

	private Boolean invalidateOnError;

	public AnnotationFieldsValueDTO(String key, Long ttl, Boolean invalidateOnError) {
		this.key = key;
		this.ttl = ttl;
		this.invalidateOnError = invalidateOnError;
	}

}