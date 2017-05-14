package de.noir.vertx.postgres.first_example.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class Server extends AbstractVerticle{

	public void start(Future<Void> future){
		
		future.complete();
	}
}
