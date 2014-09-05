package com.appstore.client;

//import com.appstore.client.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView text;
	WebView show;
	private String username="";
	private String deviceid="";
	private String password="";
	private int isConnected=0;
	private static final String TAG = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		show = (WebView) findViewById(R.id.webView1);
		show.setWebViewClient(new MyWebViewClient());
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		isConnected=LoginAppstore.connect();
		show.loadUrl("http://push.6appstore.com/action.php?action=tologin&did=android&type=android");
		//LoginAppstore.login("android","60e7c3af2ad09c48c1eb753b21c7e88220130529");
		//LoginAppstore.listen();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@SuppressLint("SimpleDateFormat")
	public void readUrl(String url){
		Log.v(TAG, "--------readUrl--------");
		String[] temp,temp2,urlValue;
		if(url.equals("http://www.6auth.net/"))
			return;
		temp2=url.split("[?]");
		//Log.v(TAG, temp2[1]);
    	if(temp2.length>1){
    		temp=temp2[1].split("[&]");
    		urlValue=temp[0].split("[=]");
    		//Log.v(TAG, urlValue[0]);
    		//Log.v(TAG, urlValue[1]);
    		if(urlValue[0].equals("stat")&&urlValue[1].equals("0")){
	    		for(int i=1;i<temp.length;i++){
	    			urlValue=temp[i].split("[=]");
	    			//Log.v(TAG, urlValue[0]);
	        		//Log.v(TAG, urlValue[1]);
	    			if(urlValue[0].equals("name")){
	    				username=urlValue[1];
	    				deviceid="android";
	    			}
	    			else if(urlValue[0].equals("token"))
	    				password=urlValue[1];
	    		}
    		}
    		else
    			return;
    	}
    	Log.v(TAG, "usr:"+deviceid);
    	//Log.v(TAG, "pwd:"+password);
    	Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        password=password+sdf.format(date);
        Log.v(TAG, "pwd:"+password);
    	if(deviceid!=""&password!=""&&isConnected==1){
    		int i=LoginAppstore.login(deviceid,password);
    		if(i==1){
    			setContentView(R.layout.userinfo);
    			text=(TextView) findViewById(R.id.textView4);
    			text.setText(username);
    			text=(TextView) findViewById(R.id.textView5);
    			date=new Date();
    			sdf=new SimpleDateFormat("yyyy-MM-dd");
    			text.setText(sdf.format(date));
    			text=(TextView) findViewById(R.id.textView6);
				text.setText("");
    			LoginAppstore.listen();
    		}
    	}
	}
	
	private class MyWebViewClient extends WebViewClient {  

		/** 
         *  ���ϣ������������Լ������������¿�Android��ϵͳbrowser����Ӧ�����ӡ� 
         *  ��WebView���һ���¼���������WebViewClient)����д���е�һЩ������ 
         *  shouldOverrideUrlLoading������ҳ�г����Ӱ�ť����Ӧ��������ĳ������ʱ 
         *  WebViewClient�������������������ݲ��������µ�url�����統webview��Ƕ 
         *  ��ҳ��ĳ�����ֱ����ʱ�������Զ���Ϊ����һ���绰���󣬻ᴫ��url��tel:123, 
         *  ����㲻ϣ����˿�ͨ����дshouldOverrideUrlLoading������� 
         */  
        @Override  
        public boolean shouldOverrideUrlLoading(WebView view, String url) {  
            // �������µ�ҳ���ʱ����webview�����д����������ϵͳ�Դ������������   
            // �������ã�webView.loadUrl("http://www.google.com");    
            // �����ļ��ã�webView.loadUrl("file:///android_asset/XX.html");  �����ļ����   
        	Log.v(TAG, "URL:"+url);
        	view.loadUrl(url);
        	readUrl(url);
            return true;  
        }
	}
}
