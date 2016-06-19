package com.meshtasks.network.listeners.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
/**
 * This class is {@link MulticastSocket} data listener for communication with Mobile device.
 * @author dipeshkumar mistry
 *
 */
public class NetworkMulticastListener implements Runnable {
	
	private String multiCastAddress;
	private String multiCastPort;
	private boolean keepAlive = true;
	private MulticastSocket serverSocket = null;
	private InetAddress inetAddress;
	
	public NetworkMulticastListener( String address, String port) {
		this.multiCastAddress = address;
		this.multiCastPort = port;
		
		try {
			inetAddress = InetAddress.getByName(this.multiCastAddress);
			this.serverSocket = new MulticastSocket(Integer.parseInt(multiCastPort));
		    this.serverSocket.joinGroup(inetAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
	        byte[] buffer = new byte[1024];
	        System.out.println("Start multicast listener at "+this.multiCastAddress+" on port "+this.multiCastPort);
			while ( this.keepAlive ) {
				DatagramPacket msgPacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(msgPacket);
                String message = new String(buffer, 0, buffer.length);
                System.out.println("Multicast Data\n"+message);
                buffer = null;
                buffer = new byte[1024];
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
	 * Method will send Multicast message in network to search/communicate with any wifi device.
	 * @param message
	 */
	public void sendMessageInNetwork(String message) {
	    System.out.println("Start message send in network\n"+message);
	    try {
            byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, inetAddress, 4444);
            serverSocket.send(packet);
            System.out.println("Multicast packet sent in network");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
	 * Method stop Multicast listener.
	 */
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
}