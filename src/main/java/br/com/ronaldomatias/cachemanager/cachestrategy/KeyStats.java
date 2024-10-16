package br.com.ronaldomatias.cachemanager.cachestrategy;

import java.util.LinkedHashSet;

public class KeyStats {

	private final LinkedHashSet<String> recentUsedLast = new LinkedHashSet<>(); // A key mais recente fica em ultima posicao.

	public void addRecentUsedLast(String key) {
		recentUsedLast.remove(key);
		recentUsedLast.add(key);
	}

	public LinkedHashSet<String> getRecentUsedLast() {
		return recentUsedLast;
	}

}
