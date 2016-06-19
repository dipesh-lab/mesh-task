package com.meshtasks.network.listeners.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.meshtasks.network.listeners.NetworkMessageListener;
import com.meshtasks.network.listeners.TransportListener;

public class SocketTransportListener extends Thread implements TransportListener {

	private NetworkMessageListener listener;
	
	private boolean listen = true;
	
	private String masterAddress;
	
	private int masterPort;

	private SocketChannel socketChannel = null;
	
	private Selector selector;
	
	private ByteBuffer readBuff = ByteBuffer.allocate(512);
	
	public SocketTransportListener() {
		
	}

	@Override
	public void init(String hostAddress, int port) {
		masterAddress = hostAddress;
		masterPort = port;
		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startListener() {
		System.out.println("Start Data Listener for " + masterAddress);
		while (listen) {
			if (socketChannel == null || !socketChannel.isConnected())
				connect();
			while ( socketChannel.isConnected() ) {
				try {
					selector.selectNow();
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
					while (keys.hasNext()) {
						SelectionKey key = keys.next();
						keys.remove();
						readBuff.clear();
						SocketChannel channel = (SocketChannel) key.channel();
						if (channel.read(readBuff) != -1) {
							readBuff.flip();
							String content = Charset.forName("UTF-8").decode(readBuff).toString();
							System.out.println("Message Received\n"+content);
							if( listener != null ) listener.messageReceived(content);
						}
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			System.out.println("Connection Lost with master...");
			try {
				disConnect();
				super.sleep(60 * 1000);
			} catch (InterruptedException inpte) {}
		}
	}
	
	public void connect() {
		if ( socketChannel != null || socketChannel.isConnected() ) return;
		try {
			socketChannel = SocketChannel.open(new InetSocketAddress(masterAddress, masterPort));
			socketChannel.socket().setSoTimeout(15000);
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
			System.out.println("Connected to Master");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setMessageListener(NetworkMessageListener l) {
		this.listener = l;
	}
	
	private void disConnect() {
		try {
			if (socketChannel != null && !socketChannel.isConnected())
				socketChannel.close();
			socketChannel = null;
		} catch (IOException e) {}
	}

	@Override
	public void stopListener() {
		listen = false;
		disConnect();
	}

	@Override
	public void sendMessage(String message) {
		// TODO Auto-generated method stub
		
	}
}