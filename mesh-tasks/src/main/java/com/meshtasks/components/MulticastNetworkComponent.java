package com.meshtasks.components;

import com.meshtasks.config.AppConfiguration;
import com.meshtasks.metadata.beans.NetworkNodeBean;
import com.meshtasks.metadata.beans.NodeDiscoveryBean;
import com.meshtasks.utils.CommonUtils;
import com.meshtasks.utils.JsonUtils;

public class MulticastNetworkComponent {
	
	private AppConfiguration config = AppConfiguration.getInstance();
	
	public NetworkNodeBean getMasterNode() {
		NetworkNodeBean nodeBean = null;
		try {
			Thread.currentThread().sleep(15000);
		} catch( InterruptedException inpte ){}
		
		
		return nodeBean;
	}
	
	private String getNodeDiscoveryMessage() {
		NodeDiscoveryBean bean = new NodeDiscoveryBean();
		String ipAddress = CommonUtils.getIPAddress("eth");
		if ( ipAddress  == null ) {
			ipAddress = CommonUtils.getIPAddress("wlan");
		}
		bean.setCommIpAddress(ipAddress);
		bean.setCommPort(config.getProperty("network.socket.port"));
		return JsonUtils.createJSONDataFromObject(bean);
	}

}