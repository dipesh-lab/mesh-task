package com.meshtasks.metadata.beans;

public class NetworkNodeBean {

	private String ipAddress;
	private String port;
	private boolean isMaster;

	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public boolean isMaster() {
		return isMaster;
	}
	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
	@Override
	public boolean equals(Object arg0) {
		NetworkNodeBean bean = (NetworkNodeBean) arg0;
		return getIpAddress().equals(bean.getIpAddress()) && getPort().equals(bean.getPort());
	}
	
	@Override
	public int hashCode() {
		String[] parts = getIpAddress().split("\\.");
		int part1 = Integer.parseInt(parts[0]);
		int part2 = Integer.parseInt(parts[1]);
		int part3 = Integer.parseInt(parts[2]);
		int part4 = Integer.parseInt(parts[3]);
		int portInt = Integer.parseInt(getPort());
		int total = (part1+part2+part3+part4) * portInt;
		return total;
	}
}