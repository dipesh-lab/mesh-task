package com.meshtasks;

import com.meshtasks.components.MulticastNetworkComponent;
import com.meshtasks.components.SocketNetworkComponent;
import com.meshtasks.components.WorkerNodeComponent;
import com.meshtasks.config.AppConfiguration;

public class BootstrapApplication {

	
	public void start() {
		
		WorkerNodeComponent workerNodeComponent = new WorkerNodeComponent();
		
		SocketNetworkComponent socketNetworkComponent = new SocketNetworkComponent(workerNodeComponent);
		
		MulticastNetworkComponent multicastNetworkComponent = new MulticastNetworkComponent();
		multicastNetworkComponent.sendHandshakMessage();
		try {
			Thread.currentThread().sleep(20000);
		} catch (InterruptedException e) {}
		// Check for received message
		AppConfiguration configuration = AppConfiguration.getInstance();
		String mode = configuration.getApplicationMode();
		if ( mode == null ) {
			/* We didn't received message for Master node information
				Create self as Master node
			*/
			configuration.setApplicationMode("master");
			/* Create connection to self to process the tasks */
		}
		System.out.println("After.  System Mode "+configuration.getApplicationMode());
	}
}