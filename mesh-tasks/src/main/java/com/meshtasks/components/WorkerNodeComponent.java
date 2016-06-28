package com.meshtasks.components;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import com.meshtasks.config.AppConfiguration;
import com.meshtasks.constants.AppConstants;
import com.meshtasks.executor.NodeMessageExecutorImpl;
import com.meshtasks.metadata.beans.MessageBean;
import com.meshtasks.metadata.beans.NetworkNodeBean;
import com.meshtasks.network.listeners.WorkerMessageListener;
import com.meshtasks.network.listeners.impl.WorkerSocketListener;
import com.meshtasks.utils.JsonUtils;

public class WorkerNodeComponent {

	private final Map<NetworkNodeBean, WorkerSocketListener> nodeMap;
	private WorkerSocketListener workerNodeListener = null;
	private final WorkerMessageListener workerMessagelistener;
	private AppConfiguration configuration = AppConfiguration.getInstance();

	public WorkerNodeComponent() {
		nodeMap = new HashMap<NetworkNodeBean, WorkerSocketListener>(3, 0.8f);
		workerMessagelistener = new NodeMessageExecutorImpl();
	}

	/**
	 * Method will be called by MASTER node only.
	 * @param nodeBean
	 */
	public void addWorkerNode(NetworkNodeBean nodeBean, SocketChannel channel) {
		// Connect to Master Node
		WorkerSocketListener socketListener = new WorkerSocketListener(channel, nodeBean, workerMessagelistener);
		Thread workerThread = new Thread(socketListener);
		workerThread.start();
		nodeMap.put(nodeBean, socketListener);
	}
	
	public void createWorkerNode( NetworkNodeBean nodeBean ) {
		SocketChannel channel = getChannel(nodeBean.getIpAddress(), nodeBean.getPort());
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
	
	public void runDataPushThread() {
		Thread runnable = new Thread() {
			
			@Override
			public void run() {
				while ( true ) {
					System.out.println("Send Data to total "+nodeMap.size());
					for ( WorkerSocketListener listener : nodeMap.values() ) {
						boolean send = listener.sendMessage("TestData");
						System.out.println("Send Status "+send);
					}
					try {
						sleep(60000);
					} catch (InterruptedException e) {}
				}
			}
		};
		runnable.start();
	}

}