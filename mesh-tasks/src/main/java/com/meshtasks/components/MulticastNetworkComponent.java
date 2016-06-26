package com.meshtasks.components;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import com.meshtasks.config.AppConfiguration;
import com.meshtasks.constants.AppConstants;
import com.meshtasks.metadata.beans.MessageBean;
import com.meshtasks.metadata.beans.NetworkNodeBean;
import com.meshtasks.network.listeners.NetworkMessageListener;
import com.meshtasks.network.listeners.TransportListener;
import com.meshtasks.network.listeners.impl.MulticastTransportListener;
import com.meshtasks.utils.CommonUtils;
import com.meshtasks.utils.JsonUtils;

public class MulticastNetworkComponent implements NetworkMessageListener {
	
	private AppConfiguration configuration = AppConfiguration.getInstance();
	private final TransportListener multicastTransport;
	
	public MulticastNetworkComponent() {
		multicastTransport = new MulticastTransportListener();
		multicastTransport.init(configuration.getProperty("multicast.packet.listener.address"), 
				Integer.parseInt(configuration.getProperty("multicast.packet.listener.port")));
		multicastTransport.setMessageListener(this);
		multicastTransport.startListener();
	}
	
	public void sendHandshakMessage() {
		MessageBean messageBean = new MessageBean();
		messageBean.setType(AppConstants.FIND_MASTER_REQ);
		NetworkNodeBean bean = new NetworkNodeBean();
		String ipAddress = null;
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
		bean.setIpAddress(ipAddress);
		bean.setPort(configuration.getSocketPort()+"");
		bean.setMaster(false);
		messageBean.setData(bean);
		String data = JsonUtils.createJSONDataFromObject(messageBean);
		multicastTransport.sendMessage(data);
	}

	@Override
	public void messageReceived(String data, SocketChannel channel) {
		if ( configuration.getApplicationMode() == null ) return;
		System.out.println("Multicast Mode : "+configuration.getApplicationMode()+ " : Message Received\n"+data);
		MessageBean messageBean = JsonUtils.createObjectFromJsonData(data, MessageBean.class);
		if ( messageBean.getType().equals(AppConstants.FIND_MASTER_REQ) && 
				configuration.getApplicationMode().equals(AppConstants.MASTER_MODE) ) {
			/* Reply Because you are master node */
			NetworkNodeBean nodeBean = JsonUtils.createObjectFromTree(messageBean.getData(), NetworkNodeBean.class);
			String targetHostAddress = nodeBean.getIpAddress();
			String targetPort = nodeBean.getPort();
			nodeBean.setMaster(true);
			String ipAddress = null;
			try {
				ipAddress = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {}
			nodeBean.setIpAddress(ipAddress);
			nodeBean.setPort(configuration.getSocketPort()+"");
			messageBean.setType(AppConstants.FIND_MASTER_RES);
			messageBean.setData(nodeBean);
			String message = JsonUtils.createJSONDataFromObject(messageBean);
			boolean status = CommonUtils.sendSocketData(targetHostAddress, targetPort, message);
		}
	}
}