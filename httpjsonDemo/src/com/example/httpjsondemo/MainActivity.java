package com.example.httpjsondemo;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity implements OnClickListener{
	Button btn_get;
	Button btn_post;
	Button btn_json;
	TextView tv_result;
	/*****************HttpClient*******************/
	HttpClient httpClient = new DefaultHttpClient();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req);
        
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_json = (Button) findViewById(R.id.btn_json);
        btn_post = (Button) findViewById(R.id.btn_post);
        tv_result = (TextView) findViewById(R.id.tv_result);
        
        btn_get.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        btn_json.setOnClickListener(this);

    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_get:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String url = "http://forshuqi.tunnel.mobi/slowtime/myservlet";
					HttpGet get = new HttpGet(url);
					HttpParams params = new BasicHttpParams();
					params.setParameter("key1", "value1");
					params.setParameter("key2", "value2");
					get.setParams(params);
					System.out.println("get");
					try {
						HttpResponse resp = httpClient.execute(get);
						
						
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.btn_post:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String url = "http://forshuqi.tunnel.mobi/slowtime/myservlet";
					HttpPost post = new HttpPost(url);
					HttpParams params = new BasicHttpParams();
					params.setParameter("key1", "value1");
					params.setParameter("key2", "value2");
					params.setParameter("key3", "value3");
					post.setParams(params);
					System.out.println("post");
					try {
						HttpResponse resp = httpClient.execute(post);
						
						
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.btn_json:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
				}
			}).start();
			break;
		default:
			break;
		}
	}

}
