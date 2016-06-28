package com.meshtasks.network.listeners.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.meshtasks.network.listeners.NetworkMessageListener;
import com.meshtasks.network.listeners.TransportListener;

public class SocketTransportListener extends Thread implements TransportListener {

	private NetworkMessageListener listener;
	
	private boolean listen = true;
	
	private ServerSocketChannel serverSocket = null;
	
	private ByteBuffer readBuff = ByteBuffer.allocate(512);
	
	public SocketTransportListener() {
		
	}

	@Override
	public void init( String hostAddress, int port ) {
		try {
			serverSocket = ServerSocketChannel.open();
			serverSocket.configureBlocking(false);
			InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
			serverSocket.socket().bind(inetSocketAddress);
			System.out.println("Socket Metadata. Host "+hostAddress +" on Port "+port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void startListener() {
		start();
	}
	
	@Override
	public void run() {
		try {
			Selector acceptSelector = Selector.open();
			serverSocket.register(acceptSelector, SelectionKey.OP_ACCEPT);
			System.out.println("Socket Listener started");
			while (listen) {
				int count = acceptSelector.select();
	            if (count != 0) {
	                for (Iterator<SelectionKey> i = acceptSelector.selectedKeys().iterator(); i.hasNext();) {
	
	                    SelectionKey key = i.next();
	                    if ( key.isAcceptable() ) {
							ServerSocketChannel sscNew = (ServerSocketChannel) key.channel();
							SocketChannel sc = sscNew.accept();
							sc.configureBlocking(false);
							// Add the new connection to the selector
							sc.register(acceptSelector, SelectionKey.OP_READ);
	                    }else if( key.isReadable() )  {
	                    	SocketChannel sChannel = (SocketChannel) key.channel();
	                        String data = readSocketData(sChannel);
	                        listener.messageReceived(data, sChannel);
	                    }
	                    i.remove();
	                }
	            }
			}
		} catch (IOException e) {
            System.out.println("Problem to start the Sensor Data Server");
            e.printStackTrace();
        }
	}

	@Override
	public void setMessageListener(NetworkMessageListener l) {
		this.listener = l;
	}
	
	private void disConnect() {
		try {
			if (serverSocket != null && !serverSocket.isRegistered()) {
				serverSocket.close();
				serverSocket = null;
			}
		} catch (IOException e) {}
	}

	@Override
	public void stopListener() {
		listen = false;
		disConnect();
	}
	
	/**
     * Method reads data from given {@link SocketChannel}.
     * @param c
     * @return {@link String}
     */
    private String readSocketData(SocketChannel c) {
        try {
            c.read(readBuff);
            readBuff.flip(); 
            return Charset.forName("UTF-8").decode(readBuff).toString().trim();
        } catch (IOException e) {
        } finally {
        	readBuff.clear();
        }
        return null;
    }

	@Override
	public void sendMessage(String message) {
		// TODO Auto-generated method stub
		
	}
}