package br.com.ronaldomatias.cachemanager.exception;

public class CacheManagerException extends RuntimeException {
	public CacheManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheManagerException(String message) {
		super(message, null);
	}

}