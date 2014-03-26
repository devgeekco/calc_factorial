package com.devgeekslab.calcfactorial;

import com.devgeekslab.calcfactorial.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class ResultActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	TextView t;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	Button showResultButton;
	long time, size;
	String factStr;
	ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_result);

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		time = extras.getLong("time");
		size = extras.getLong("size");

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		showResultButton = (Button) findViewById(R.id.dummy_button);

		showResultButton.setEnabled(false);

		TextView t = new TextView(this); 

		int count = 0;
		String tempFact = "";

		int temp=0;
		for(long i=size;i>=0;i--){
			if((MainActivity.factResult[(int)i]!=0) || (temp!=0)){
				tempFact += MainActivity.factResult[(int)i];
				count += 1;
				temp=1;
				if(count > 101)
					break;
			}
		}

		t=(TextView) findViewById(R.id.fullscreen_content); 
		if(tempFact.length() > 101)
			t.setText("Result Size: "+(size-5)+"\nTime: "+time+ " ms\n"+ ((double)time/(double)1000) +" sec\n" +
					"\nFactorial --> \n"+tempFact+"...\n\nDouble Tap to See Full Factorial");
		else
			t.setText("Result Size: "+(size-5)+"\nTime: "+time+ " ms\n"+ ((double)time/(double)1000) +" sec\n" +
					"\nFactorial --> \n"+tempFact);

		showResultButton.setEnabled(true);

		showResultButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(size >5999)
					Toast.makeText(getApplicationContext(), "Processing... Please Wait!", Toast.LENGTH_SHORT).show();
				setContentView(R.layout.show_factorial);
				showResultInLayout();
			}
		});

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
		.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					if (mControlsHeight == 0) {
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(
								android.R.integer.config_shortAnimTime);
					}
					controlsView
					.animate()
					.translationY(visible ? 0 : mControlsHeight)
					.setDuration(mShortAnimTime);
				} else {
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE
							: View.GONE);
				}

				if (visible && AUTO_HIDE) {
					// Schedule a hide().
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
			}
		});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					if(size >5999)
						Toast.makeText(getApplicationContext(), "Processing... Please Wait!", Toast.LENGTH_SHORT).show();
					setContentView(R.layout.show_factorial);
					showResultInLayout();
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(
				mDelayHideTouchListener);
	}

	/**
	 * Showing the big factorial result in the layout.
	 */
	private void showResultInLayout() {
		TextView tv = (TextView) findViewById(R.id.textViewResult);
		tv.setText("");
		factStr = "";
		int temp=0;
		for(long i=size;i>=0;i--){
			if((MainActivity.factResult[(int)i]!=0) || (temp!=0)){
				factStr += MainActivity.factResult[(int)i];
				tv.append(factStr);
				factStr="";
				temp=1;
			}
		}
		//new bigFactPrint().execute();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	/**
	 *  ProgressDialog common call with different message
	 * @param firstString
	 * @param secondString
	 * @return
	 */
	protected ProgressDialog callProgressDialog(String firstString, String secondString) {
		return ProgressDialog.show(ResultActivity.this, firstString, secondString, true);
	}
	
	class bigFactPrint extends AsyncTask<Void, Void, Void> {
		TextView tv = (TextView) findViewById(R.id.textViewResult);
		@Override
		protected void onPreExecute() {
			progressDialog= callProgressDialog("Processing....","Printing HUGE number will take sometime!");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			factStr = "";
			int temp=0;
			for(long i=size;i>=0;i--){
				if((MainActivity.factResult[(int)i]!=0) || (temp!=0)){
					factStr += MainActivity.factResult[(int)i];
					temp=1;
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void nothing) {
			if (progressDialog !=null) {
				progressDialog.dismiss();
			//	TextView tv = (TextView) findViewById(R.id.textViewResult);
				tv.setText(factStr);
			}
		}
		
		
		
		
		
	};
}
