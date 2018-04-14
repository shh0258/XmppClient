package com.smile.xmpp.xmppclient.client;

public class XmppTest {

	public static void main(String[] args) throws Exception {

		String username = "big0258";
		String password = "gmlehd2";

		XmppManager xmppManager = new XmppManager("10.240.229.89", 5222);

		xmppManager.init();
		xmppManager.performLogin(username, password);
		xmppManager.setStatus(true, "Hello everyone");

		String buddyJID = "tester";
		String buddyName = "tester";
		xmppManager.createEntry(buddyJID, buddyName);

		xmppManager.sendMessage("Hello mate", "big0258@naver.com");

		boolean isRunning = true;

		while (isRunning) {
			Thread.sleep(50);
		}

		xmppManager.destroy();

	}

}