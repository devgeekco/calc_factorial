package com.devgeekslab.calcfactorial;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog implements
android.view.View.OnClickListener {

	public Activity c;
	public Dialog d;
	public Button yes;
	
	TextView tv;
	String text;

	public CustomDialog(Activity a, String text) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
		this.text = text;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_box);
		
		tv = (TextView) findViewById(R.id.txt_dia);
		tv.setText(text);
		
		yes = (Button) findViewById(R.id.btn_yes);
		yes.setOnClickListener(this);
	}
	
	/*public void setDialogText(String text) {
		tv.setText(text);
	}*/

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yes:
			//c.;
			break;
		default:
			break;
		}
		dismiss();
	}
}