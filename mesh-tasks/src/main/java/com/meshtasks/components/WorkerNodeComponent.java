package com.meshtasks.components;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import com.meshtasks.metadata.beans.NetworkNodeBean;
import com.meshtasks.network.listeners.WorkerMessageListener;
import com.meshtasks.network.listeners.impl.WorkerNodeConnector;

public class WorkerNodeComponent implements NodeComponent {

	private NetworkNodeBean networkNodeBean = null;
	/* To write result */
	private SocketChannel channel = null;
	/* To read result */
	private WorkerNodeConnector connector = null;
	private final WorkerMessageListener workerMessagelistener;

	public WorkerNodeComponent(WorkerMessageListener listener) {
		workerMessagelistener = listener;
	}
	
	@Override
	public void setNetworkBean(NetworkNodeBean bean) {
		networkNodeBean = bean;
	}

	@Override
	public void connect() {
		connector = new WorkerNodeConnector(workerMessagelistener);
		connector.init(networkNodeBean.getIpAddress(), networkNodeBean.getPort());
		connector.start();
	}

	@Override
	public boolean sendMessage(String message) {
		if ( !channel.isConnected() ) return false;
		try {
			byte[] byteData = Charset.forName("UTF-8").encode(message).array();
			ByteBuffer data = ByteBuffer.wrap(byteData);
			channel.write(data);
			return true;
		} catch (IOException e) {}
		return false;
	}

	@Override
	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}	

}