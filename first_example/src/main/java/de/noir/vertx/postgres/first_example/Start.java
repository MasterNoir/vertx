package de.noir.vertx.postgres.first_example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.hazelcast.config.Config;

import de.noir.vertx.postgres.first_example.client.Client;
import de.noir.vertx.postgres.first_example.server.Server;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class Start implements ActionListener{
	
	JFrame cFrame;
	JButton clientButton;
	JButton serverButton;
	
	
	/*
		Hier werden alle notwendigen Einstellungen getroffen um eine Vert.x-Instanz zu erstellen.
		Es wird TcpIp verwendet und Multicast deaktiviert.
	 */

    public static void main(String[] args) {
    	
    	Start start = new Start();
    	start.initGUI();

    }    
    
    public void initGUI(){
    	
    	System.out.println("test");
    	JFrame.setDefaultLookAndFeelDecorated(true);
        cFrame = new JFrame();
        cFrame.setTitle("Client");      
        cFrame.setSize(250,75);
        cFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel panel1 = new JPanel();

        container.add(panel1);
        
        clientButton = new JButton("Start Client");
        clientButton.addActionListener(this);
        panel1.add(clientButton);
        
        serverButton = new JButton("Start Server");
        serverButton.addActionListener(this);
        panel1.add(serverButton);
        
        cFrame.add(container);
        cFrame.setVisible(true);
    }
    
    public void actionPerformed (ActionEvent ae){
    	
    	// Erstellt eine Config-Variable
    	Config hazelcastConfig = new Config();
    	
              
              if(ae.getSource() == this.clientButton){
            	  
            	  hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("192.168.2.118").setEnabled(true);
                  // Deaktiviert Multicast.
                  hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
                  
                  // Verbindet die Config mit dem Hazelcast-ClusterManager
                  ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
                  
                  VertxOptions options = new VertxOptions().setClustered(true).setClusterManager(mgr).setClusterHost("192.168.2.112");
                  
                  
                  Vertx.clusteredVertx(options, res -> {
              		
              		cFrame.dispose();
              		
                      if (res.succeeded()) {
                        Vertx vertx = res.result();
                        vertx.deployVerticle(new Client());
                      }
                      else {
                          System.out.println("Clustered Vertx failed!");
                      }
                  });
              }else if(ae.getSource() == this.serverButton){
            	  
            	  hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
                  // Deaktiviert Multicast.
                  hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
                  
                  // Verbindet die Config mit dem Hazelcast-ClusterManager
                  ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);

                  
                  VertxOptions options = new VertxOptions().setClustered(true).setClusterManager(mgr).setClusterHost("192.168.2.118");
                  
                  Vertx.clusteredVertx(options, res -> {
              		
              		cFrame.dispose();
              		
                      if (res.succeeded()) {
                        Vertx vertx = res.result();
                        vertx.deployVerticle(new Server());
                      }
                      else {
                          System.out.println("Clustered Vertx failed!");
                      }
                  });
              }
              
    }

}


