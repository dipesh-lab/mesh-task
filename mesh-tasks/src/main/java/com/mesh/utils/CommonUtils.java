package com.mesh.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
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
                    if (inetAddress != null    && inetAddress.getHostAddress().indexOf(".") > 0 ) {
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
}