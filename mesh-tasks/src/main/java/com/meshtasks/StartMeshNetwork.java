package com.meshtasks;


public class StartMeshNetwork {
	public static void main(String[] arg) {
		/*
		 * 1. Send multicast message in network.
		 *    - If someone replied with Master message then become SLAVE and connect to Master
		 *    - Otherwise become Master
		 * */
		BootstrapApplication app = new BootstrapApplication();
		app.start();
	}
}