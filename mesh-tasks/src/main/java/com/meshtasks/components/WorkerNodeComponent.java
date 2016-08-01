package com.meshtasks.components;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.meshtasks.constants.AppConstants;
import com.meshtasks.metadata.beans.NetworkNodeBean;
import com.meshtasks.network.listeners.WorkerMessageListener;
import com.meshtasks.network.listeners.impl.WorkerNodeConnector;

public class WorkerNodeComponent implements NodeComponent {

	private NetworkNodeBean networkNodeBean = null;
	/* To write result */
	private SocketChannel channel = null;
	/* To read result */
	private Thread workerThread = null;
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
		workerThread = new Thread(connector);
		workerThread.start();
	}

	@Override
	public boolean sendMessage(String message) {
		if ( channel ==null || !channel.isConnected() ) return false;
		try {
			byte[] byteData = AppConstants.SOCKET_CHAR_SET.encode(message).array();
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

	@Override
	public void stopListener() {
		if ( connector != null )
			connector.stopListener();
		
		if ( workerThread != null ) {
			workerThread.interrupt();
			workerThread = null;
		}
	}
	
	private Runnable connectionCheck() {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				while ( channel.isConnected() ) {
					
				}
			}
		};
		return runnable;
	}
	
	/*private void runDataPushThread() {
		Thread runnable = new Thread() {
			@Override
			public void run() {
				while ( true ) {
					String msg = "Push data = "+configuration.getSocketPort()+" = "+messageCounter.incrementAndGet();
					sendMessage(msg);
					try {
						sleep(10000);
					} catch (InterruptedException e) {}
				}
			}
		};
		runnable.start();
	}*/

}