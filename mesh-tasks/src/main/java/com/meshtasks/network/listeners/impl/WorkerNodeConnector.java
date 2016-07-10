package com.meshtasks.network.listeners.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.meshtasks.config.AppConfiguration;
import com.meshtasks.constants.AppConstants;
import com.meshtasks.metadata.beans.MessageBean;
import com.meshtasks.metadata.beans.NetworkNodeBean;
import com.meshtasks.network.listeners.WorkerMessageListener;
import com.meshtasks.utils.JsonUtils;

public class WorkerNodeConnector implements Runnable {

	private SocketChannel channel=null;
	private ByteBuffer readBuff = ByteBuffer.allocate(512);
	private final WorkerMessageListener listener;
	private AppConfiguration configuration = AppConfiguration.getInstance();
	private boolean connected = false;
	
	public WorkerNodeConnector(WorkerMessageListener l) {
		listener = l;
	}
	
	public void init(String hostAddress, String port) {
		System.out.println("Create Node Connection. Host "+hostAddress +" Port "+port);
		try {
			channel = SocketChannel.open(new InetSocketAddress(hostAddress, Integer.parseInt(port)));
			channel.configureBlocking(false);
			connected = true;
			
			MessageBean bean = new MessageBean();
			bean.setType(AppConstants.WORKER_NODE_CON_REQ);
			if ( configuration.getApplicationMode().equals(AppConstants.MASTER_MODE) ) {
				/* Send CON_RES */
				bean.setType(AppConstants.WORKER_NODE_CON_RES);
			}
			NetworkNodeBean nodeBean = new NetworkNodeBean();
			nodeBean.setIpAddress(hostAddress);
			nodeBean.setPort(port);
			bean.setData(nodeBean);
			String jsonData = JsonUtils.createJSONDataFromObject(bean);
			ByteBuffer buffer = ByteBuffer.wrap(jsonData.getBytes());
			channel.write(buffer);
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if ( !connected ) return;
		try {
			Selector selector = Selector.open();
			channel.register(selector, SelectionKey.OP_READ);
			System.out.println("App Mode "+ configuration.getApplicationMode() +" WorkerNodeConnector started listening");
			while (connected) {
				selector.selectNow();
					Iterator<SelectionKey> keys = selector.selectedKeys()
							.iterator();
					while (keys.hasNext()) {
						SelectionKey key = keys.next();
						if (key.isReadable()) {
							try {
								SocketChannel channel = (SocketChannel) key.channel();
								if (channel.read(readBuff) != -1) {
									readBuff.flip();
									String content = Charset.forName("UTF-8")
											.decode(readBuff).toString().trim();
									listener.messageReceived(content);
									readBuff.clear();
								}
							} catch (IOException ioe) {
								break;
							}
						}
						keys.remove();
					}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopListener() {
		connected = false;
		try {
			channel.close();
		} catch (Exception e) {}
	}
}