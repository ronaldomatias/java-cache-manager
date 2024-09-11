package br.com.ronaldomatias.cachemanager.annotation;

import lombok.Data;

@Data
public class AnnotationDTO {

	private String key;

	private Long ttl;

	private Boolean invalidateOnError;

	public AnnotationDTO(String key, Long ttl, Boolean invalidateOnError) {
		this.key = key;
		this.ttl = ttl;
		this.invalidateOnError = invalidateOnError;
	}

}