package com.meshtasks.executor;

import com.meshtasks.config.AppConfiguration;
import com.meshtasks.network.listeners.WorkerMessageListener;

public class NodeMessageExecutorImpl implements WorkerMessageListener {

	private AppConfiguration configuration = AppConfiguration.getInstance();
	
	@Override
	public void messageReceived(String message) {
		System.out.println("App Mode "+configuration.getApplicationMode()+"\nMessage Received\n"+message);
		
	}

}