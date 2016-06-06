package com.meshtasks.network.listeners;

public interface TransportListener {

	public void init(String hostAddress, int port);
	
	public void startListener();
	
	public void reStartListener();
	
	public void setInMessageListener(InMessageListener listener);
	
	public void stopListener();
	
}