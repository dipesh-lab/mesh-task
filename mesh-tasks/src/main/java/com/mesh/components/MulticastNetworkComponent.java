package com.mesh.components;

import com.mesh.config.AppConfiguration;
import com.mesh.metadata.beans.NetworkNodeBean;
import com.mesh.metadata.beans.NodeDiscoveryBean;
import com.mesh.utils.CommonUtils;
import com.mesh.utils.JsonUtils;

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