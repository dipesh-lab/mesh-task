package com.meshtasks.components;

import java.nio.channels.SocketChannel;

import com.meshtasks.metadata.beans.NetworkNodeBean;

public interface NodeComponent {

	public void setNetworkBean(NetworkNodeBean bean);
	
	public void connect();
	
	public boolean sendMessage(String message);
	
	public void setChannel(SocketChannel channel);
	
	public void stopListener();
}