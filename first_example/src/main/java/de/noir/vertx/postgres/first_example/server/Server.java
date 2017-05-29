package de.noir.vertx.postgres.first_example.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLConnection;

public class Server extends AbstractVerticle{

	public void start(Future<Void> future){
		
		vertx.eventBus().consumer("setKeyValue", message -> {
			String msg[];
			msg = message.body().toString().split(";");
			setKeyValue(msg[0], msg[1]);
        });
		
		vertx.eventBus().consumer("getValue", message -> {
            
        });
		
		JsonObject postgreSQLClientConfig = new JsonObject().put("host", "localhost");
		postgreSQLClientConfig.put("username", "postgres");
		postgreSQLClientConfig.put("password", "postgres");
		postgreSQLClientConfig.put("port", "5432");
		postgreSQLClientConfig.put("database", "");
		
		AsyncSQLClient postgreSQLClient = PostgreSQLClient.createShared(vertx, postgreSQLClientConfig);
		
		
		postgreSQLClient.getConnection(res -> {
					  if (res.succeeded()) {
			
					    SQLConnection connection = res.result();
			
					    // Got a connection
			
					  } else {
					    // Failed to get connection - deal with it
					  }
				});
		
		future.complete();
	}
	
	public void setKeyValue(String key, String value){
		
	}
	
	public void getValue(String key){
		
		vertx.eventBus().publish("resultToClient", "qqq");
	}
}
