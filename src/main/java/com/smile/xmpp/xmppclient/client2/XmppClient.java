package com.smile.xmpp.xmppclient.client2;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

public class XmppClient {
	public static void main(String[] args) throws Exception {
		XmppClient gt = new XmppClient();
		System.out.print("if");
		gt.test();
	}

	public void test() throws Exception {

		boolean 인증필요 = false;

		ConnectionConfiguration config = null;
		if (인증필요) {
			System.out.print("if");
			config = new ConnectionConfiguration("10.240.229.89", 5223, "naver.com");
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, null, null);
			config.setSocketFactory(context.getSocketFactory());
		} else {
			System.out.print("else");
			config = new ConnectionConfiguration("10.240.229.89", 5222, "naver.com");
		}

		XMPPConnection conn = new XMPPConnection(config);

		conn.connect();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("아이디(@gmail.com 자동 첨부) : ");
		String username = reader.readLine();
		if (!username.endsWith("@gmail.com")) {
			username += "@gmail.com";
		}

		Console console = System.console();
		String password = "";
		if (console == null) {
			System.out.print("비밀번호 : ");
			password = reader.readLine();
		} else {
			password = new String(console.readPassword("비밀번호: "));
		}

		conn.login(username, password);

		System.out.println("{친구 목록}");
		Iterator<RosterEntry> entries = conn.getRoster().getEntries().iterator();
		while (entries.hasNext()) {
			RosterEntry e = entries.next();
			System.out.printf(" - %s\n", e.getUser());
		}

		System.out.println("{대화 상대 선택}");
		String target = reader.readLine();

		MessageListener listener = new MessageListener() {
			@Override
			public void processMessage(Chat chat, Message message) {
				if (message.getType() == Message.Type.chat && message.getBody() != null) {
					System.out.printf("%s : %s\n", chat.getParticipant(), message.getBody());
				}
			}
		};

		Chat chat = conn.getChatManager().createChat(target, listener);

		System.out.println("{대화 시작}");

		String msg = null;
		while (!(msg = reader.readLine()).equals("!종료")) {
			chat.sendMessage(msg);
		}

		conn.disconnect();

		System.out.println("{종료합니다}");
	}
}