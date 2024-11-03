package br.com.ronaldomatias.cachemanager.redis.monitor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class StatisticsMonitor {
	// TODO: Fazer dessa classe um Singleton.

	Date updatedAt;

	List<String> topFiveKeysMenosUtilizados;

	List<String> errosDeComposicaoDeChave;

	public void updateTopFiveLFUKeys() {
		if (this.isStatsStillValid()) return;

		// TODO: Atualiza o top5.
	}

	private boolean isStatsStillValid() {
		return updatedAt.toInstant().plus(10, ChronoUnit.MINUTES).isAfter(Instant.now());
	}

}
