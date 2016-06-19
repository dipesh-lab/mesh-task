package com.meshtasks;

import com.meshtasks.components.MulticastNetworkComponent;
import com.meshtasks.components.SocketMessageComponent;
import com.meshtasks.config.AppConfiguration;
import com.meshtasks.network.listeners.NetworkMessageListener;
import com.meshtasks.network.listeners.TransportListener;
import com.meshtasks.network.listeners.impl.SocketTransportListener;

public class BootstrapApplication {

	private AppConfiguration configuration = AppConfiguration.getInstance();
	
	public void start() {
		NetworkMessageListener socketMessageListener = new SocketMessageComponent();
		
		TransportListener socketListener = new SocketTransportListener();
		socketListener.setInMessageListener(socketMessageListener);
		socketListener.startListener();
		
		MulticastNetworkComponent component = new MulticastNetworkComponent();
		component.sendMessageToMasterNode();
		
		
	}
	
}