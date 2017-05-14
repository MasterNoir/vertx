package de.noir.vertx.postgres.first_example;

import com.hazelcast.config.Config;

import de.noir.vertx.postgres.first_example.client.Client;

//import org.jruby.javasupport.ext.JavaUtilRegex.Matcher;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class start {
	
	/*
		Hier werden alle notwendigen Einstellungen getroffen um eine Vert.x-Instanz zu erstellen.
		Es wird TcpIp verwendet und Multicast deaktiviert.
	 */

    public static void main(String[] args) {
    	
    	// Erstellt eine Config-Variable
    	Config hazelcastConfig = new Config();
    	// Aktiviert TcpIp und bestimmt die Adressen.
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("192.168.2.112").setEnabled(true);
        // Deaktiviert Multicast.
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        
        // Verbindet die Config mit dem Hazelcast-ClusterManager
        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        
        // Setzt die Vertx-Optionen auf Cluster-Modus und verwendet den konfigurierten Hazelcast-CM. 
    	VertxOptions options = new VertxOptions().setClustered(true).setClusterManager(mgr).setClusterHost("192.168.178.52");
    	
    	// Erzeugt eine Vert.x-Instanz.
        Vertx.clusteredVertx(options, res -> {
          if (res.succeeded()) {
            Vertx vertx = res.result();
            
            // Erzeugt einen Receiver-Verticle.
            vertx.deployVerticle(new Client());
            
          } else {
            // failed!
          }
        });

    }    

}


