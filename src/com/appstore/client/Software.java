package com.appstore.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

public class Software {
	private static final String TAG = null;
	
	public String ownusr="";

	public int Action=0;
	
	private String Sname="";
	private String Sid="";
	private String Sversion="";
	
	public void readXml(String xmlStr){
		XmlPullParser parser=Xml.newPullParser();
		int event=0,i=0;
		try {
			parser.setInput(new StringReader(xmlStr));
			event = parser.getEventType();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(event!=XmlPullParser.END_DOCUMENT){
			switch(event){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if("Action".equals(parser.getName())){
						Log.v(TAG, "---Action-----");
						try {
							Action=Integer.parseInt(parser.nextText());
							Log.v(TAG, Action+"");
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if("Name".equals(parser.getName())){
						Log.v(TAG, "---Name-----");
						try {
							i=setName(parser.nextText());
							if(i==0)
								Log.v(TAG, "---Name_error-----");
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if("Content".equals(parser.getName())){
						Log.v(TAG, "---Content-----");
						try {
							i=setId(parser.nextText());
							if(i==0)
								Log.v(TAG, "---Id_error-----");
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if("Note".equals(parser.getName())){
						Log.v(TAG, "---Note-----");
						try {
							i=setVersion(parser.nextText());
							if(i==0)
								Log.v(TAG, "---Version_error-----");
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				case XmlPullParser.END_TAG:
					break;
			}
			try {
				event=parser.next();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(i!=0){
			try {
				Sdownload();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
			
	}
	
	private int setName(String Name){
		if(Name!=""){
			Sname=Name;
			return 1;
		}
		return 0;
	}
	
	private int setId(String Id){
		if(Id!=""){
			Sid=Id;
			return 1;
		}
		return 0;
	}
	
	private int setVersion(String Version){
		if(Version!=""){
			Sversion=Version;
			return 1;
		}
		return 0;
	}
	
	public void Sdownload() throws IOException{
		
		String url="http://www.6appstore.com/app/download?u="+ownusr+"&s="+Sid;
		String path=Environment.getExternalStorageDirectory()+"/zft/";

		URL Url = new URL(url);
		URLConnection conn = Url.openConnection();
		conn.connect();
		InputStream is = conn.getInputStream();
		int fileSize = conn.getContentLength();// 根据响应获取文件大小
		if (fileSize <= 0) { // 获取内容长度为0
			throw new RuntimeException("无法获知文件大小 ");
		}
		if (is == null) { // 没有下载流
			//sendMsg(Down_ERROR);
			throw new RuntimeException("无法获取文件");
		}
		FileOutputStream FOS = new FileOutputStream(path + this.Sname + ".exe"); // 创建写入文件内存流，通过此流向目标写文件

		byte buf[] = new byte[1024];
		int downLoadFilePosition = 0;
		int numread;
		
		while ((numread=is.read(buf))!=-1){
			FOS.write(buf, 0, numread);
			downLoadFilePosition += numread;
		}
		
		try {
			is.close();
		} catch (Exception ex) {
			;
		}
	}
}
