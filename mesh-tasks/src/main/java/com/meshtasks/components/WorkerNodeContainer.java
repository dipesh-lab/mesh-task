package com.meshtasks.components;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import com.meshtasks.executor.NodeMessageExecutorImpl;
import com.meshtasks.metadata.beans.NetworkNodeBean;
import com.meshtasks.network.listeners.WorkerMessageListener;

public class WorkerNodeContainer {

	private final Map<NetworkNodeBean, WorkerNodeComponent> nodeMap;
	private final WorkerMessageListener workerMessageListener;
	private WorkerNodeComponent workerComponent = null;
	
	public WorkerNodeContainer() {
		nodeMap = new HashMap<NetworkNodeBean, WorkerNodeComponent>(3, 0.8f);
		workerMessageListener = new NodeMessageExecutorImpl();
	}
	
	/**
	 * Method will be called by MASTER node only.
	 * @param nodeBean
	 */
	public void addWorkerNode( NetworkNodeBean nodeBean, SocketChannel channel ) {
		// Connection request received from client.
		System.out.println("Add Worker Node "+nodeBean.getIpAddress() +" : "+nodeBean.getPort());
		WorkerNodeComponent component = nodeMap.get(nodeBean);
		if ( component == null ) {
			component = new WorkerNodeComponent(workerMessageListener);
			nodeMap.put(nodeBean, component);
		}
		component.setNetworkBean(nodeBean);
		component.setChannel(channel);
		component.stopListener();
		/* Connect to Client to create Read channel */
		component.connect();
	}
	
	public void createWorkerNode( NetworkNodeBean nodeBean ) {
		workerComponent = new WorkerNodeComponent(workerMessageListener);
		workerComponent.setNetworkBean(nodeBean);
		workerComponent.connect();
	}

	public void setWorkerWriter(NetworkNodeBean nodeBean, SocketChannel channel) {
		workerComponent.setChannel(channel);
	}

}