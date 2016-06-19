package com.meshtasks.components;

import com.meshtasks.constants.AppConstants;
import com.meshtasks.metadata.beans.MessageBean;
import com.meshtasks.network.listeners.NetworkMessageListener;
import com.meshtasks.utils.JsonUtils;

public class SocketMessageComponent implements NetworkMessageListener {

	
	
	@Override
	public void messageReceived(String data) {
		MessageBean messageBean = JsonUtils.createObjectFromJsonData(data, MessageBean.class);
		data = JsonUtils.createJSONDataFromObject(messageBean.getData());
		if ( messageBean.getType().equals(AppConstants.MASTER_CONNECTION_RESPONSE) ) {
			
		}
	}
	
}