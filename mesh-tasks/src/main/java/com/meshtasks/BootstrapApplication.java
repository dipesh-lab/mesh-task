package com.meshtasks;

import com.meshtasks.components.MulticastNetworkComponent;
import com.meshtasks.components.ServerSocketMessageComponent;
import com.meshtasks.config.AppConfiguration;
import com.meshtasks.network.listeners.NetworkMessageListener;
import com.meshtasks.network.listeners.TransportListener;
import com.meshtasks.network.listeners.impl.ServerSocketTransportListener;

public class BootstrapApplication {

	
	public void start() {
		
		MulticastNetworkComponent component = new MulticastNetworkComponent();
		System.out.println("Multicast listener is created");
		
	}
	
}