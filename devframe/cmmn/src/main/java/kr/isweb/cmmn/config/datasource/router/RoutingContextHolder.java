package kr.isweb.cmmn.config.datasource.router;

import org.springframework.util.Assert;

public class RoutingContextHolder {

	private static ThreadLocal<RoutingKey> context = new ThreadLocal<>();

	public static void set(RoutingKey clientDatabase) {
		Assert.notNull(clientDatabase, "clientDatabase cannot be null");
		context.set(clientDatabase);
	}

	public static RoutingKey getClientDatabase() {
		return context.get();
	}

	public static void clear() {
		context.remove();
	}
}
