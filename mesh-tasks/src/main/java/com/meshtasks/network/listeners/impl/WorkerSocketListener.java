package com.meshtasks.network.listeners.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.meshtasks.config.AppConfiguration;
import com.meshtasks.metadata.beans.NetworkNodeBean;
import com.meshtasks.network.listeners.WorkerMessageListener;

public class WorkerSocketListener implements Runnable {

	private SocketChannel channel=null;
	private NetworkNodeBean nodeBean = null;
	private boolean listen = true;
	private ByteBuffer readBuff = ByteBuffer.allocate(512);
	private final WorkerMessageListener listener;
	private AppConfiguration configuration = AppConfiguration.getInstance();
	
	public WorkerSocketListener(SocketChannel ch, NetworkNodeBean bean, WorkerMessageListener l) {
		channel = ch;
		nodeBean = bean;
		listener = l;
	}

	@Override
	public void run() {
		try {
			Selector selector = Selector.open();
			channel.register(selector, SelectionKey.OP_READ);
			System.out.println("App Mode "+ configuration.getApplicationMode() +" Client connect to Master IP Address "+nodeBean.getIpAddress());
			while (listen) {
				while (channel.isConnected() && !channel.socket().isClosed()) {
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
								System.out.println(ioe.getMessage());
								break;
							}
						}
						keys.remove();
					}

				}
				System.out.println("Now Wait for connection");
				try {
					Thread.currentThread().sleep(20000);
				} catch (InterruptedException e) {}
				System.out.println("Wait OVer for connection");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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

}