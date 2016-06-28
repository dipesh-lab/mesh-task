package com.meshtasks.executor;

import com.meshtasks.network.listeners.WorkerMessageListener;

public class NodeMessageExecutorImpl implements WorkerMessageListener {

	
	
	@Override
	public void messageReceived(String message) {
		System.out.println("Message Received from Master\n"+message);
		
	}

}