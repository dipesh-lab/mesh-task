package com.meshtasks.components;

import com.meshtasks.config.AppConfiguration;
import com.meshtasks.constants.AppConstants;
import com.meshtasks.metadata.beans.MessageBean;
import com.meshtasks.metadata.beans.NetworkNodeBean;
import com.meshtasks.network.listeners.impl.NetworkMulticastListener;
import com.meshtasks.utils.CommonUtils;
import com.meshtasks.utils.JsonUtils;

public class MulticastNetworkComponent {
	
	private AppConfiguration configuration = AppConfiguration.getInstance();
	private NetworkMulticastListener multicastListener;
	
	public MulticastNetworkComponent() {
		multicastListener = new NetworkMulticastListener(
			configuration.getProperty("multicast.packet.listener.address"), 
			configuration.getProperty("multicast.packet.listener.port"));
	}
	
	public void sendMessageToMasterNode() {
		MessageBean messageBean = new MessageBean();
		messageBean.setType(AppConstants.FIND_MASTER_REQUEST);
		NetworkNodeBean bean = new NetworkNodeBean();
		String ipAddress = CommonUtils.getIPAddress("eth");
		if ( ipAddress  == null ) {
			ipAddress = CommonUtils.getIPAddress("wlan");
		}
		bean.setIpAddress(ipAddress);
		bean.setPort(configuration.getProperty("network.socket.port"));
		bean.setMaster(false);
		messageBean.setData(bean);
		String data = JsonUtils.createJSONDataFromObject(messageBean);
		multicastListener.sendMessageInNetwork(data);
	}
}