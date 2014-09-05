package com.appstore.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
//import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
//import android.widget.TextView;

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
							Log.v(TAG, Sname+"");
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
							Log.v(TAG, Sid+"");
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
							Log.v(TAG, Sversion+"");
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
			try {
				install();
			} catch (Exception e) {
				 //TODO Auto-generated catch block
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
	
	public void Sdownload() throws IOException
	{
		String url="http://www.6appstore.com/app/download?u="+ownusr+"&s="+Sid;
		System.out.println(url);
		
		String path=Environment.getExternalStorageDirectory().toString();

		URL Url = new URL(url);
		URLConnection conn = Url.openConnection();
		conn.connect();
		InputStream is = conn.getInputStream();
		int fileSize = conn.getContentLength();// 根据响应获取文件大小
		System.out.println("fileSize = "+fileSize);
		if (fileSize <= 0) { // 获取内容长度为0
			throw new RuntimeException("无法获知文件大小 ");
		}
		if (is == null) { // 没有下载流
			//sendMsg(Down_ERROR);
			throw new RuntimeException("无法获取文件");
		}
		
		FileOutputStream FOS = new FileOutputStream(path + File.separator + this.Sname + ".apk"); // 创建写入文件内存流，通过此流向目标写文件

		byte buf[] = new byte[1024];
		int downLoadFilePosition = 0;
		int numread;
		
		while ((numread=is.read(buf))!=-1){
			FOS.write(buf, 0, numread);
			downLoadFilePosition += numread;
		}
		
		try {
			is.close();
			FOS.close();
		} catch (Exception ex) {
			;
		}       
	}
	
	public void install() {
		String filePath = Environment.getExternalStorageDirectory()+"/"+this.Sname+".apk";
		System.out.println("filePath = "+filePath);
	    Intent i = new Intent(Intent.ACTION_VIEW);
	    
		i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MainActivity.getMainActivityInst().startActivity(i);
		
		
	}/*
	//安装apk  
	//注：若要获取弹出的安装界面是否点击了“取消”，则下面的intent.setFlags不能使用FLAG_ACTIVITY_NEW_TASK  
//	      因为使用FLAG_ACTIVITY_NEW_TASK无法获取到取消的事件，可有FLAG_ACTIVITY_SINGLE_TOP代替  
	public static void  installApk(Context context, String strFilePath){  
	    Uri uri = Uri.fromFile(new File(strFilePath));  
	    Intent  intent = new Intent(android.content.Intent.ACTION_VIEW, uri);  
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	//  intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP );  //若要获取安装界面的点击结果，则用此项，然后使用startActivityForResult  
	    intent.setDataAndType(uri, "application/vnd.android.package-archive");  
	    context.startActivity(intent);  
	}  
	  
	//卸载apk  
	public static void  uninstallApk(Context context, String strPackageName){  
	    Uri  uri = Uri.fromParts("package", strPackageName, null);  
	    Intent  intent = new Intent(Intent.ACTION_DELETE, uri);  
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	    context.startActivity(intent);  
	    
	}  */

}
