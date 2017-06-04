package de.noir.vertx.postgres.first_example.client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalIconFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Verticle;

public class Client extends AbstractVerticle implements ActionListener{
    
	JFrame cFrame;
	JButton subButton;
	JButton clearButton;
	JButton searchButton;
	GridLayout experimentLayout;
	JTextField key;
    JTextField value;
    JTextField search;
    JTextField result;
	
    public void start(Future<Void> future){
    	
        vertx.eventBus().consumer("resultToClient", message -> {
            result.setText(message.body().toString());
        });
               
        initGUI();
        
	    future.complete();
    }
    
    public void initGUI(){
    	
    	JFrame.setDefaultLookAndFeelDecorated(true);
        cFrame = new JFrame();
        cFrame.setTitle("Client");      
        cFrame.setSize(500,300);
        cFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        container.add(panel1);
        container.add(panel2);
        container.add(panel3);
		 
        JLabel label1 = new JLabel("Database entry:");
        panel1.add(label1);
 
        key = new JTextField("NEW KEY", 15);
        value = new JTextField("NEW VALUE",15);

        panel1.add(key);
        panel1.add(value);
        
        subButton = new JButton("Send Entry");
        subButton.addActionListener(this);
        panel2.add(subButton);
        
        clearButton = new JButton("Clear Tabley");
        clearButton.addActionListener(this);
        panel2.add(clearButton);
        
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        panel3.add(searchButton);
        
        search = new JTextField("Key",15);
        panel3.add(search);
        
        JLabel label2 = new JLabel("Result:");
        panel3.add(label2);
        
        result = new JTextField("Result",15);
        result.setEditable(false);
        panel3.add(result);
        
        cFrame.add(container);
        cFrame.setVisible(true);
    }
    
    public void actionPerformed (ActionEvent ae){
    	
    	if(ae.getSource() == this.subButton){
            System.out.println(key.getText() + " | " + value.getText());
            vertx.eventBus().publish("setKeyValue", key.getText() + ";" + value.getText());
            key.setText("NEW KEY");
            value.setText("NEW VALUE");
        }else if(ae.getSource() == this.searchButton){
        	System.out.println("Search: " + search.getText());
        	vertx.eventBus().publish("getValue", search.getText());
        }else if(ae.getSource() == this.clearButton){
        	System.out.println("Clear");
        	vertx.eventBus().publish("clearTable", search.getText());
        }
    	
    }

}

