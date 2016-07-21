package com.itheima.apisflorea.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.itheima.apisflorea.ApisFlorea;
import com.itheima.apisflorea.R;

public class MainActivity extends Activity {
	private ApisFlorea apisFlorea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apisFlorea = (ApisFlorea) View.inflate(this, R.layout.activity_main, null);
        setContentView(apisFlorea);
        
        new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				
				apisFlorea.start();
			}
		}, 1000);
        
    }


}
