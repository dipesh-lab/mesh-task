package com.meshtasks.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;

public class CommonUtils {

	
	public static boolean isEmpty( String data ) {
		return data == null || data.trim().isEmpty();
	}
	
	public static boolean isEmpty(Collection<?> col) {
		return col == null || col.isEmpty();
	}
	
	/**
     * Method returns Gateway's WLAN IP address.
     * @return {@link String}
     */
    public static String getIPAddress(String interfaceName) {
        String ipAddress = null;
        try {
            Enumeration<NetworkInterface> inter = NetworkInterface.getNetworkInterfaces();
            boolean cycle=true;
            while( cycle && inter.hasMoreElements() ) {
                NetworkInterface interfaceObject = inter.nextElement();
                if ( !interfaceObject.getName().startsWith(interfaceName) ) continue;
                Enumeration<InetAddress> addressEnum = interfaceObject.getInetAddresses();
                while (cycle && addressEnum.hasMoreElements()) {
                    InetAddress inetAddress = addressEnum.nextElement();
                    if (inetAddress != null && inetAddress.getHostAddress().indexOf(".") > 0 ) {
                    	ipAddress = inetAddress.getHostAddress();
                    	if ( !ipAddress.equals("127.0.0.1") ) {
                    		cycle=false;
                    		break;
                    	}
                    }
                }
            }
        } catch( SocketException se ) {
            se.printStackTrace();
        }
        return ipAddress;
    }
    
    public static int getSocketPort(int initialPort) {
    	boolean loop = true;
    	while ( loop ) {
    		Socket socket = null;
    		try {
    			socket = new Socket(InetAddress.getLocalHost(), initialPort);
    			initialPort++;
    		} catch(IOException ioe) {
    			loop = false;
    		} finally {
    			try {
    				if ( socket!= null && socket.isConnected()) {
    					socket.close();
    				}
    			} catch(IOException e){}
    		}
    	}
    	return initialPort;
    }
    
    public static boolean sendSocketData(String hostAddress, String port, String request) {
    	SocketAddress targetAddress = new InetSocketAddress(hostAddress, Integer.parseInt(port));
    	SocketChannel channel = null;
        try {
			channel = SocketChannel.open(targetAddress);
			boolean status = channel.finishConnect();
			if ( status ) {
				byte[] byteData = Charset.forName("UTF-8").encode(request).array();
				ByteBuffer header = ByteBuffer.wrap(byteData);
		        channel.write(header);
		        return true;
			}
        } catch (IOException e) {
		} finally {
			try {
				if ( channel != null ) channel.close();
			} catch(IOException ioe){}
		}
        return false;
	}

    public static String getLocalIpAddress() {
    	String ipAddress = null;
    	try {
    		ipAddress = InetAddress.getLocalHost().getHostAddress();
    	} catch(IOException ioe){}
    	return ipAddress;
    }
}