package com.meshtasks.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    
    public static byte[] getFileData( String path ) {
    	byte[] data = null;
    	FileInputStream inStream = null;
    	try {
    		inStream = new FileInputStream(new File(path));
    		data = new byte[inStream.available()];
    		byte[] chunk = new byte[512];
    		int totalRead = -1;
    		int counter = 0;
    		while ( (totalRead = inStream.read(chunk)) != -1 ) {
    			System.arraycopy(chunk, 0, data, counter, totalRead);
    			counter += totalRead;
    		}
    		return data;
    	} catch(IOException ioe) {
    	} finally {
    		try {
    			inStream.close();
    		} catch(IOException ioe){}
    	}
    	return null;
    }
    
    public static void writeFile(String path, String fileName, String content) {
    	File dirFile = new File(path);
    	if (!dirFile.exists()) dirFile.mkdirs();
    	
    	FileOutputStream outStream = null;
    	try {
    		outStream = new FileOutputStream(dirFile.getAbsolutePath() + File.separator + fileName );
    		outStream.write(content.getBytes());
    	} catch( IOException ioe ) {
    		ioe.printStackTrace();
    	} finally {
    		try {
    			outStream.flush();
    			outStream.close();
    		} catch(IOException ioe){}
    	}
    }
    
    public static byte[] readInputStream(InputStream inStream) {
    	byte[] data = null;
    	byte[] chunk = new byte[512];
        int counter = 0;
        int totalRead = -1;
        try {
        	int readBytes = inStream.available();
        	System.out.println("Read from Stream "+readBytes);
        	data = new byte[readBytes];
        	System.out.println("");
			while ( (totalRead = inStream.read(chunk)) != -1 ) {
				System.arraycopy(chunk, 0, data, counter, totalRead);
    			counter += totalRead;
			}
			return data;
		} catch ( IOException e ) {
			e.printStackTrace();
		}
    	return null;
    }

}