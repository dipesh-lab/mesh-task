package com.meshtasks.network.listeners.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import com.meshtasks.network.listeners.NetworkMessageListener;
import com.meshtasks.network.listeners.TransportListener;

/**
 * This class is {@link MulticastSocket} data listener for communication with Mobile device.
 * @author dipeshkumar mistry
 *
 */
public class MulticastTransportListener extends Thread implements TransportListener {
	
	private boolean keepAlive = true;
	private MulticastSocket serverSocket = null;
	private InetAddress inetAddress = null;
	private NetworkMessageListener messageListener = null;
	
	public MulticastTransportListener() {
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
	        byte[] buffer = new byte[1024];
	        System.out.println("Multicast Listener started");
			while ( this.keepAlive ) {
				DatagramPacket msgPacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(msgPacket);
                String message = new String(buffer, 0, buffer.length);
                buffer = null;
                buffer = new byte[1024];
                messageListener.messageReceived(message, null);
			}
		} catch( Exception uhe ) {
			uhe.printStackTrace();
		} finally {
			if ( this.serverSocket != null && this.serverSocket.isConnected() ) {
				this.serverSocket.disconnect();
				this.serverSocket.close();
			}
		}
	}
	
	/**
	 * Method stop Multicast listener.
	 */
	@Override
	public void stopListener() {
		this.keepAlive = false;
		if ( this.serverSocket != null && this.serverSocket.isConnected() ) {
			this.serverSocket.disconnect();
			this.serverSocket.close();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		stopListener();
		super.finalize();
	}

	@Override
	public void init(String hostAddress, int port) {
		try {
			inetAddress = InetAddress.getByName(hostAddress);
			this.serverSocket = new MulticastSocket(port);
		    this.serverSocket.joinGroup(inetAddress);
		    System.out.println("Multicast listener metadata. Host "+hostAddress +" Port "+port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startListener() {
		start();
	}

	@Override
	public void setMessageListener(NetworkMessageListener listener) {
		messageListener = listener;
	}

	/**
	 * Method will send Multicast message in network to search/communicate with any wifi device.
	 * @param message
	 */
	@Override
	public void sendMessage(String message) {
	    try {
            byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, inetAddress, 4444);
            serverSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}