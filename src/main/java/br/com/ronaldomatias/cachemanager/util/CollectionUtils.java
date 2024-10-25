package br.com.ronaldomatias.cachemanager.util;

import java.util.Collection;

public class CollectionUtils {

	public static Boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

}
