package de.noir.vertx.postgres.first_example.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLConnection;

public class Server extends AbstractVerticle{
	
	SQLConnection connection;

	public void start(Future<Void> future){
		
		vertx.eventBus().consumer("setKeyValue", message -> {
			String msg[];
			msg = message.body().toString().split(";");
			setKeyValue(msg[0], msg[1]);
        });
		
		vertx.eventBus().consumer("getValue", message -> {
            getValue(message.body().toString());
        });
		
		vertx.eventBus().consumer("clearTable", message -> {
            clearTable();
        });
		
		JsonObject postgreSQLClientConfig = new JsonObject().put("host", "localhost");
		postgreSQLClientConfig.put("port", 5432);
		postgreSQLClientConfig.put("username", "postgres");
		postgreSQLClientConfig.put("password", "postgres");
		postgreSQLClientConfig.put("database", "vertx_database");
		
		AsyncSQLClient postgreSQLClient = PostgreSQLClient.createShared(vertx, postgreSQLClientConfig);
		
		
		postgreSQLClient.getConnection(res -> {
					  if (res.succeeded()) {
			
						connection = res.result();						
			
					  } else {
						  System.out.println("Connection failed!");
					  }
				});
		
		future.complete();
	}
	
	public void setKeyValue(String key, String value){
		connection.query("INSERT INTO dictionary VALUES('" + key + "', '" + value + "')", arg1 -> {
	    	System.out.println("New key:   " + key);
	    	System.out.println("New value: " + value);
	    });
	}
	
	public void getValue(String key){

		connection.query("SELECT value FROM dictionary WHERE  key = '" + key + "'", arg1 -> {
	    	System.out.println("Queried key:    " + key);
	    	System.out.println("Returned value: " + arg1.result().getResults().get(0).getString(0));
	    	vertx.eventBus().publish("resultToClient", arg1.result().getResults().get(0).getString(0));
	    });
	}
	
	public void clearTable(){
		connection.query("DELETE FROM dictionary", arg1 -> {
			System.out.println("Table cleared!");
		});
	}
}
