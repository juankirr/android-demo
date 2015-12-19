package com.totti.Zxing.Demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.totti.Bmob.Goods;
import com.totti.Zxing.Demo.camera.CameraManager;
import com.totti.Zxing.Demo.decoding.CaptureActivityHandler;
import com.totti.Zxing.Demo.decoding.InactivityTimer;
import com.totti.Zxing.Demo.view.ViewfinderView;
import com.totti.activity.DetailListActivity;
import com.zijunlin.Zxing.Demo.R;

public class CaptureActivity extends Activity implements Callback,OnClickListener
{
	private View topbar;
	private Button bt_back;
	private Button bt_right;
	
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private Goods goods;
	private BmobQuery<Goods> query_goods;
	private int jobtype;
	private String currentNumber;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//初始化bmob
		//Bmob.initialize(this, "775ca9e12e9081f9440c00278afc5fb2");
		// 初始化 CameraManager
		
		//initView();
		
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}
	/*
	private void initView(){
		topbar = findViewById(R.id.a_capture_topbar);
		bt_back = (Button) topbar.findViewById(R.id.topbar_back);
		bt_right = (Button) topbar.findViewById(R.id.topbar_right);
		
		bt_back.setOnClickListener(this);
	}*/
	@Override
	protected void onResume(){
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface)
		{
			initCamera(surfaceHolder);
		}
		else
		{
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
		{
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (handler != null)
		{
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy()
	{
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder)
	{
		try
		{
			CameraManager.get().openDriver(surfaceHolder);
		}
		catch (IOException ioe)
		{
			return;
		}
		catch (RuntimeException e)
		{
			return;
		}
		if (handler == null)
		{
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		if (!hasSurface)
		{
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView()
	{
		return viewfinderView;
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void drawViewfinder()
	{
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(final Result obj, Bitmap barcode)
	{
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 处理
		 */
		currentNumber = obj.getText();
		if(DetailListActivity.datalist.contains(currentNumber)){
			Toast.makeText(this, currentNumber+"已扫描，请勿重复操作", Toast.LENGTH_LONG).show();	
		}else{
			Toast.makeText(this, currentNumber, Toast.LENGTH_LONG).show();
			DetailListActivity.datalist.add(currentNumber);
			DetailListActivity.datalist_mode.add("型号:未知");
		}
	}

	private void initBeepSound()
	{
		if (playBeep && mediaPlayer == null)
		{
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try
			{
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			}
			catch (IOException e)
			{
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate()
	{
		if (playBeep && mediaPlayer != null)
		{
			mediaPlayer.start();
		}
		if (vibrate)
		{
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener()
	{
		public void onCompletion(MediaPlayer mediaPlayer)
		{
			mediaPlayer.seekTo(0);
		}
	};
	
	private void Input(){
		query_goods = new BmobQuery<Goods>();
		query_goods.addWhereEqualTo("number", currentNumber);
		query_goods.addWhereEqualTo("state",0);
		query_goods.findObjects(this, new FindListener<Goods>() {
			@Override
			public void onSuccess(List<Goods> arg0) {
				// TODO Auto-generated method stub
				if(arg0.size()==0){
					goods = new Goods();
					goods.setNumber(currentNumber);
					goods.setState(0);
					goods.save(CaptureActivity.this,new SaveListener() {
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							System.out.println("succeed");
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							System.out.println("failed:"+arg1);
						}
					});
				}else{
					Toast.makeText(CaptureActivity.this,"请勿重复入库", Toast.LENGTH_SHORT).show();
					System.out.println("请勿重复入库");
				}
			}
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("find error:"+arg1);
			}
		});
	}
	private void Output(){
		query_goods = new BmobQuery<Goods>();
		query_goods.addWhereEqualTo("number", currentNumber);
		query_goods.findObjects(CaptureActivity.this, new FindListener<Goods>() {
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("find error:"+arg1);
			}

			@Override
			public void onSuccess(List<Goods> arg0) {
				// TODO Auto-generated method stub
				if(arg0.size()==0){
					System.out.println("不存在，无法出库");
					Toast.makeText(CaptureActivity.this,"不存在，无法出库", Toast.LENGTH_SHORT).show();
				}else{
					if(arg0.get(0).getState()==0){
						arg0.get(0).setState(1);
						arg0.get(0).update(CaptureActivity.this, new UpdateListener() {
							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								System.out.println("出库成功");
								Toast.makeText(CaptureActivity.this,"出库成功", Toast.LENGTH_SHORT).show();
							}
							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								System.out.println("出库失败:"+arg1);
							}
						});
					}else{
						//state error
					}
				}
			}
		});
	}
	private void Borrow(){
		query_goods = new BmobQuery<Goods>();
		query_goods.addWhereEqualTo("number", currentNumber);
		query_goods.findObjects(this, new FindListener<Goods>() {
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("find error:"+arg1);
			}
			@Override
			public void onSuccess(List<Goods> arg0) {
				// TODO Auto-generated method stub
				if(arg0.size()==1){
				if(arg0.get(0).getState()==0){//2借出
					arg0.get(0).setState(2);
					arg0.get(0).update(CaptureActivity.this, new UpdateListener() {
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							System.out.println("借出成功");
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							System.out.println("借出error:"+arg1);
						}
					});
				}else if(arg0.get(0).getState()==1){//1或2
					Toast.makeText(CaptureActivity.this, "所借物品已出库", Toast.LENGTH_SHORT).show();
				}else if(arg0.get(0).getState()==2){
					Toast.makeText(CaptureActivity.this, "所借物品已借出", Toast.LENGTH_SHORT).show();
				}
				}else if(arg0.size()==0){
					Toast.makeText(CaptureActivity.this, "所借物品不存在", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(CaptureActivity.this, "所借物品重复", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	private void Return(){
		query_goods = new BmobQuery<Goods>();
		query_goods.addWhereEqualTo("number", currentNumber);
		query_goods.findObjects(this, new FindListener<Goods>() {
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("find error:"+arg1);
			}
			@Override
			public void onSuccess(List<Goods> arg0) {
				// TODO Auto-generated method stub
				if(arg0.size()==1){
				if(arg0.get(0).getState()==2){//2借出
					arg0.get(0).setState(0);
					arg0.get(0).update(CaptureActivity.this,new UpdateListener() {
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							System.out.println("归还成功");
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							System.out.println("return error:"+arg1);
						}
					});
				}else if(arg0.get(0).getState()==1){
					Toast.makeText(CaptureActivity.this, "所借物品已出库", Toast.LENGTH_SHORT).show();
				}else if(arg0.get(0).getState()==0){
					Toast.makeText(CaptureActivity.this, "所借物品未出库", Toast.LENGTH_SHORT).show();
				}
				}else if(arg0.size()==0){
					Toast.makeText(CaptureActivity.this, "所还物品不存在", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(CaptureActivity.this, "所还物品存在重复", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.topbar_back:
			finish();
			break;

		default:
			break;
		}
	}
}