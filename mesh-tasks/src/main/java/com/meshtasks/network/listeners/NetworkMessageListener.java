package com.meshtasks.network.listeners;

import java.nio.channels.SocketChannel;

public interface NetworkMessageListener {

	public void messageReceived(String data, SocketChannel channel);
	
}