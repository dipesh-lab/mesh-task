package com.meshtasks.network.listeners;

public interface TransportListener {

	public void init(String hostAddress, int port);
	
	public void startListener();
	
	public void setMessageListener(NetworkMessageListener listener);
	
	public void stopListener();
	
	public void sendMessage(String message);
	
}