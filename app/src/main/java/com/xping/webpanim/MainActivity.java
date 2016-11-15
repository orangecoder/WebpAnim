package com.xping.webpanim;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{

	private TextView tv_type;
	private GiftGLSurfaceView giftGLSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		initView();
	}

	private void initView() {
		giftGLSurfaceView = (GiftGLSurfaceView) findViewById(R.id.glsv_gift);

		tv_type = (TextView)findViewById(R.id.tv_type);
		tv_type.setText("");
		findViewById(R.id.btn_70).setOnClickListener(this);
		findViewById(R.id.btn_100).setOnClickListener(this);
		findViewById(R.id.btn_8sec).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		giftGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		giftGLSurfaceView.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_70:
//				giftGLSurfaceView.show_70();
				giftGLSurfaceView.showFireworksTube();
				tv_type.setText("70");
				break;
			case R.id.btn_100:
//				giftGLSurfaceView.show_100();
				giftGLSurfaceView.showScreenBomb();
				tv_type.setText("100");
				break;
			case R.id.btn_8sec:
//				giftGLSurfaceView.show_8sec();
				giftGLSurfaceView.showFireworkSeason();
				tv_type.setText("8秒");
				break;
		}
	}
}
