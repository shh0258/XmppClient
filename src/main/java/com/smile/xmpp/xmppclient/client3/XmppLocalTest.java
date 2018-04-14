package com.smile.xmpp.xmppclient.client3;

public class XmppLocalTest {
	 //Total number of messages sent during one minute.
    public static final int MESSAGES_NUMBER = 120;

    //Each message will have a random number between 0 and MAX_RANDOM.
    public static final int MAX_RANDOM = 100;

    private static final String SENDER_USERNAME = "big0258";
    private static final String SENDER_PASSWORD = "passpass";

    public static void main(String[] args) throws Exception {

        String username = SENDER_USERNAME;
        String password = SENDER_PASSWORD;
        XmppManager xmppManager = new XmppManager("10.240.229.89", 5222,
                "test");

        xmppManager.init();

        xmppManager.performLogin(username, password);
        System.out.println("Login performed with success, setting status.");
        xmppManager.setStatus(true, "한상현의 테스트");

        System.out.println("Setting up receivers");
        xmppManager.createEntry("tester", "tester");

        //go message!
        xmppManager.sendMessage("Hello world!", "imessage@10.240.229.89");

        xmppManager.destroy();
    }
}
