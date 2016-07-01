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
import com.meshtasks.network.listeners.WorkerMessageListener;

public class WorkerNodeConnector extends Thread {

	private SocketChannel channel=null;
	private ByteBuffer readBuff = ByteBuffer.allocate(512);
	private final WorkerMessageListener listener;
	private AppConfiguration configuration = AppConfiguration.getInstance();
	private boolean connected = false;
	
	public WorkerNodeConnector(WorkerMessageListener l) {
		listener = l;
	}
	
	public void init(String hostAddress, String port) {
		try {
			channel = SocketChannel.open(new InetSocketAddress(hostAddress, Integer.parseInt(port)));
			channel.configureBlocking(false);
			connected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if ( !connected ) return;
		try {
			Selector selector = Selector.open();
			channel.register(selector, SelectionKey.OP_READ);
			System.out.println("App Mode "+ configuration.getApplicationMode() +" Reading Channel is created and listening");
			while (true) {
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
}