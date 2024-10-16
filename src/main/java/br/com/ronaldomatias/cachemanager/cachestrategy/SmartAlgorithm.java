package br.com.ronaldomatias.cachemanager.cachestrategy;

public class SmartAlgorithm {
	// TODO: Avaliar criar um singleton dessa classe;
	private final KeyStats keyStats;

	private final LruAlgorithm lruAlgorithm;

	public SmartAlgorithm() {
		this.keyStats = new KeyStats();
		this.lruAlgorithm  = new LruAlgorithm();;
	}

	public void executeCleaningStrategy(Object value) {
		lruAlgorithm.executeCleaningStrategy(value, this.keyStats);
	}

	public KeyStats getKeyStats() {
		return keyStats;
	}

}
