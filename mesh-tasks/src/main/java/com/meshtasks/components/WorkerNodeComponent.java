package com.meshtasks.components;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import com.meshtasks.metadata.beans.NetworkNodeBean;

public class WorkerNodeComponent {
	
	private final Map<NetworkNodeBean, SocketChannel> nodeMap;
	
	public WorkerNodeComponent() {
		nodeMap = new HashMap<NetworkNodeBean, SocketChannel>(5, 0.8f);
	}

	public void addWorkerNode(NetworkNodeBean nodeBean, SocketChannel channel) {
		nodeMap.put(nodeBean, channel);
	}

}