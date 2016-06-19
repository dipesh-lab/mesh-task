package com.meshtasks.components;

import com.meshtasks.config.AppConfiguration;
import com.meshtasks.network.listeners.NetworkMessageListener;
import com.meshtasks.network.listeners.TransportListener;
import com.meshtasks.network.listeners.impl.ServerSocketTransportListener;

public class ServerSocketMessageComponent implements NetworkMessageListener {

	private AppConfiguration configuration = AppConfiguration.getInstance();
	private final TransportListener listener;
	
	public ServerSocketMessageComponent() {
		listener = new ServerSocketTransportListener();
		listener.init("localhost", Integer.parseInt(configuration.getProperty("network.socket.port")));
		listener.setMessageListener(this);
		listener.startListener();
	}
	
	@Override
	public void messageReceived(String data) {
		
		
		/*MessageBean messageBean = JsonUtils.createObjectFromJsonData(data, MessageBean.class);
		data = JsonUtils.createJSONDataFromObject(messageBean.getData());
		if ( messageBean.getType().equals(AppConstants.MASTER_CONNECTION_RESPONSE) ) {
			
		}*/
	}
	
}