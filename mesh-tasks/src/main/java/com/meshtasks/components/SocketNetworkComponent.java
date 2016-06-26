package com.meshtasks.components;

import java.nio.channels.SocketChannel;

import com.meshtasks.config.AppConfiguration;
import com.meshtasks.constants.AppConstants;
import com.meshtasks.metadata.beans.MessageBean;
import com.meshtasks.metadata.beans.NetworkNodeBean;
import com.meshtasks.network.listeners.NetworkMessageListener;
import com.meshtasks.network.listeners.TransportListener;
import com.meshtasks.network.listeners.impl.SocketTransportListener;
import com.meshtasks.utils.CommonUtils;
import com.meshtasks.utils.JsonUtils;

public class SocketNetworkComponent implements NetworkMessageListener {

	private AppConfiguration configuration = AppConfiguration.getInstance();
	private final TransportListener listener;
	private WorkerNodeComponent workerNodeComponent = null;
	
	public SocketNetworkComponent(WorkerNodeComponent component) {
		listener = new SocketTransportListener();
		int availablePort = CommonUtils.getSocketPort(
				Integer.parseInt(configuration.getProperty("network.socket.port")));
		listener.init("localhost", availablePort);
		listener.setMessageListener(this);
		listener.startListener();
		configuration.setSocketPort(availablePort);
	}
	
	@Override
	public void messageReceived(String data, SocketChannel channel) {
		if ( CommonUtils.isEmpty(data) ) return;
		System.out.println("Mode Socket : " + configuration.getApplicationMode() + " : Message Received\n"+data);
		MessageBean message = JsonUtils.createObjectFromJsonData(data, MessageBean.class);
		System.out.println("Socket Message Type "+message.getType());
		if ( message.getType().equals(AppConstants.FIND_MASTER_RES) ) {
			NetworkNodeBean nodeBean = JsonUtils.createObjectFromTree(message.getData(),
					NetworkNodeBean.class);
			System.out.println("Socket message received. Master "+nodeBean.getIpAddress()+" : "+nodeBean.getPort());
			configuration.setApplicationMode(AppConstants.CLIENT_MODE);
			workerNodeComponent.addWorkerNode(nodeBean, channel);
		}
	}

}