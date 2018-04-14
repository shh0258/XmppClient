package com.smile.xmpp.xmppclient.client3;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

public class XmppManager {

    private static final int packetReplyTimeout = 500; // millis

    private String server;
    private int port;
    private String service;

    private ConnectionConfiguration config;
    private XMPPConnection connection; // 암호

    private ChatManager chatManager;
    private MessageListener messageListener;

    public XmppManager(String aServer, int aPort, String aService) {
        server = aServer;
        port = aPort;
        service = aService;
    }

    public XmppManager(String server, int port) {
        this(server, port, null);
    }

    public void init() throws XMPPException {

        System.out.println(String.format("Initializing connection to server " +  server + ", port " + port
                + ", service " + service));

        SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);

        if(service != null)
            config = new ConnectionConfiguration(server, port, service);
        else
            config = new ConnectionConfiguration(server, port);

        config.setSASLAuthenticationEnabled(true);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);

        connection = new XMPPConnection(config);
        connection.connect();

        System.out.println("Connected: " + connection.isConnected());

        chatManager = connection.getChatManager();
        messageListener = new MyMessageListener();

    }

    public void performLogin(String username, String password) throws XMPPException {

        if (connection!=null && connection.isConnected()) {
            System.out.println("Logging in: " + username + ", " + password);
            connection.login(username, password);
        }
    }

    public void setStatus(boolean available, String status) {

        Presence.Type type = available? Type.available: Type.unavailable;
        Presence presence = new Presence(type);

        presence.setStatus(status);
        connection.sendPacket(presence);

    }

    public void destroy() {
        if (connection!=null && connection.isConnected()) {
            connection.disconnect();
        }
    }

    public void sendMessage(String message, String buddyJID) throws XMPPException {
        System.out.println(String.format("Sending message " + message + " to user " + buddyJID));
        Chat chat = chatManager.createChat(buddyJID, messageListener);
        chat.sendMessage(message);
    }

    public void createEntry(String user, String name) throws Exception {
        System.out.println(String.format("Creating entry for buddy " + user + " with name " + name));
        Roster roster = connection.getRoster();
        roster.createEntry(user, name, null);
    }

    private class MyMessageListener implements MessageListener {

        @Override
        public void processMessage(Chat chat, Message message) {
            String from = message.getFrom();
            String body = message.getBody();
            System.out.println(String.format("Received message " + body + " from " + from));
        }

    }

}
