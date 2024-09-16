package br.com.ronaldomatias.cachemanager.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InvalidateCache {
	String key() default "";
	String dynamicKey() default "";
	boolean invalidateOnError() default false;

}
