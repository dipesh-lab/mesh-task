package com.meshtasks.socket.listeners;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
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
	
	public NetworkMulticastListener( String address, String port ) {
		this.multiCastAddress = address;
		this.multiCastPort = port;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
			InetAddress address = InetAddress.getByName(this.multiCastAddress);
	        byte[] buffer = new byte[1024];
	        System.out.println("Start multicast listener at "+this.multiCastAddress+" on port "+this.multiCastPort);
	        this.serverSocket = new MulticastSocket(Integer.parseInt(multiCastPort));
	        this.serverSocket.joinGroup(address);
			while ( this.keepAlive ) {
				DatagramPacket msgPacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(msgPacket);
                String msg = new String(buffer, 0, buffer.length);
                System.out.println("Multicast Data\n"+msg);
                buffer = null;
                buffer = new byte[1024];
                processMessage(msg);
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
	 * Method will process received Multicast packet request.
	 * @param message
	 */
	private void processMessage(String message) {
		System.out.println("Received In Message\n"+message);
	}
	
	/**
	 * Method will send Multicast message in network to search/communicate with any wifi device.
	 * @param message
	 */
	public void sendMessageInNetwork(String message) {
	    System.out.println("Start message send in network\n"+message);
	    try {
            byte[] buf = message.getBytes();
            serverSocket = new MulticastSocket(Integer.parseInt(this.multiCastPort));
            InetAddress group = InetAddress.getByName(this.multiCastAddress);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4444);
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

}