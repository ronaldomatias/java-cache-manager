package br.com.ronaldomatias.cachemanager.cachestrategy;

import java.util.LinkedHashSet;

public class LruAlgorithm {

	public void executeCleaningStrategy(Object value, KeyStats keyStats) {
		LinkedHashSet<String> recentUsedLast = keyStats.getRecentUsedLast();
		// TODO: Define quanto de espaço é necessário para armazenar a solicitacao atual.
		int valueSize = value.toString().getBytes().length;
		// TODO: Libera o espaço necessario.

	}

}
