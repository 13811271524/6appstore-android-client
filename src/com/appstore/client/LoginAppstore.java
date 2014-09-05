package com.appstore.client;

import java.util.Vector;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.os.StrictMode;
import android.util.Log;

public class LoginAppstore {
	private static final String TAG = null;
	static Software s1;
	static ConnectionConfiguration connConfig;
	static XMPPConnection mXmppConnection;
	private static String usr=""; 
	private Vector packList = new Vector();
	
	public static int connect(){
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		connConfig=new ConnectionConfiguration("push.6appstore.com",5222);
		mXmppConnection=new XMPPConnection(connConfig);
		Log.v(TAG,"第一步创建服务器连接对象");
		 try {
			mXmppConnection.connect();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.v(TAG,"第二步连接服务器");
		if(mXmppConnection.isConnected()){
		        Log.v(TAG, "连接成功");
		        return 1;
		}else{
		        Log.v(TAG, "连接未成功");
		        return 0;
		}
	}
	
	public static int login(String username,String deviceid,String password){
		usr=username;
		try {
			mXmppConnection.login(deviceid+"@push.6appstore.com",password);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.v(TAG, "第三步登陆"); 
		if(mXmppConnection.isAuthenticated()){
		        Log.v(TAG, "登录成功");
		        return 1;
		}else{
		        Log.v(TAG, "登录未成功");
		        return 0;
		}
	}
	
	public static void listen(){
		mXmppConnection.addPacketListener(new PacketListener() {
			public void processPacket(Packet packet) {
				Message message = (Message) packet;
				System.out.println("收到消息" + message.toXML());
				
				String pack=message.toXML();
				readPacket(pack);
			}
		},null); 
	}
	
	public static void readPacket(String pack){
		pack=pack.replaceAll("&lt;", "<");
		pack=pack.replaceAll("&gt;", ">");
		System.out.println("处理后" + pack);
		
		s1=new Software();
		s1.ownusr=usr;
		s1.readXml(pack);
	}
}
