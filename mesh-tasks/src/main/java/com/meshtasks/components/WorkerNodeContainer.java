package com.meshtasks.components;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import com.meshtasks.executor.NodeMessageExecutorImpl;
import com.meshtasks.metadata.beans.NetworkNodeBean;
import com.meshtasks.network.listeners.WorkerMessageListener;

public class WorkerNodeContainer {

	private final Map<NetworkNodeBean, WorkerNodeComponent> nodeMap;
	private final WorkerMessageListener workerMessageListener;
	
	public WorkerNodeContainer() {
		nodeMap = new HashMap<NetworkNodeBean, WorkerNodeComponent>(3, 0.8f);
		workerMessageListener = new NodeMessageExecutorImpl();
	}
	
	/**
	 * Method will be called by MASTER node only.
	 * @param nodeBean
	 */
	public void addWorkerNode(NetworkNodeBean nodeBean, SocketChannel channel) {
		// Connection request received from client.
		WorkerNodeComponent component = new WorkerNodeComponent(workerMessageListener);
		component.setNetworkBean(nodeBean);
		component.setChannel(channel);
		nodeMap.put(nodeBean, component);
		
		/* Connect to Client to create Read channel */
	}
	
	public void createWorkerNode( NetworkNodeBean nodeBean ) {
		/*SocketChannel channel = getChannel(nodeBean.getIpAddress(), nodeBean.getPort());
		workerNodeListener = new WorkerSocketListener(channel, nodeBean, workerMessagelistener);
		Thread workerThread = new Thread(workerNodeListener);
		workerThread.start();
		
		MessageBean messageBean = new MessageBean();
		messageBean.setType(AppConstants.WORKER_NODE_CON_REQ);
		nodeBean.setPort(configuration.getSocketPort()+"");
		String ipAddress = null;
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
		nodeBean.setIpAddress(ipAddress);
		nodeBean.setMaster(false);
		messageBean.setData(nodeBean);
		String message = JsonUtils.createJSONDataFromObject(messageBean);
		System.out.println("Application Mode" + configuration.getApplicationMode() 
			+ " Create Worker Node JSON\n"+message);
		
		workerNodeListener.sendMessage(message);
		runClientPushThread();*/
	}
	
	private SocketChannel getChannel( String hostAddress, String port ) {
		SocketChannel channel = null;
		try {
			channel = SocketChannel.open(new InetSocketAddress(hostAddress, Integer.parseInt(port)));
			channel.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return channel;
	}
	
}
