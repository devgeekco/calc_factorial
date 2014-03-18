package com.devgeekslab.calcfactorial;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Ankit Singh
 * @copyright DevGeeksLab 2014
 * Refer License in the projects folder for terms and condition
 * to use this Android application.
 * 
 * @TODO: About me activity class
 * @TODO: add way to show different fun-facts about factorial.
 * @TODO: find best advertisement payment subscriber. 
 */
public class MainActivity extends Activity {
	private static final int REQUEST_CODE = 10;
	private final String LOG_TAG = "CalcLargeFacts";

	EditText factInput;
	Button calcFactButton;
	TextView tv;
	ProgressDialog progressDialog = null;

	protected long inpLong;
	protected static int[] factResult;

	// native function declaration
	private native int[] getFactorial(long input);
	private native long getSize();
	
	// loading facto library
	static {
		System.loadLibrary("facto");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		factInput = (EditText) findViewById(R.id.inputNumber);
		calcFactButton = (Button) findViewById(R.id.calcFact);
		TextView tv = (TextView) findViewById(R.id.textView1); 
		
		tv.setText("The factorial function (symbol: !) means to multiply a series of descending natural numbers.\n\nExamples:\n" +
					"* 4! = 4 × 3 × 2 × 1 = 24 \n" +
					"* 7! = 7 × 6 × 5 × 4 × 3 × 2 × 1 = 5040\n" +
					"* 1! = 1\n" + 
					"* 0! = 1 (Zero Factorial is interesting... it is generally agreed that 0! = 1)");

		factInput.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				calcFactButton.setEnabled(true);
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){}
		}); 

		// When calculate button pressed
		calcFactButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String userInput = factInput.getText().toString();

				if(!userInput.isEmpty()) {
					inpLong = Long.parseLong(userInput);
					Log.i(LOG_TAG, "Input From User: "+inpLong);
					if(inpLong <= 55555)
						new ExecuteFactCalc().execute();
					else if (inpLong > 55555 && inpLong < 99999 ) {	
						Toast.makeText(getApplicationContext(), "STOP!!!! DANGER!!!\nYou wanna fry your device??? We gladly support upto " +
								"factorial upto '999999' but not on your device! \nSorry but please try again with smaller number!!! :-/", Toast.LENGTH_LONG).show();
					} else if (inpLong >= 99999 && inpLong < 999999 ) {  
						Toast.makeText(getApplicationContext(),"Ooopppss!! \n" +
								"We gladly support upto factorial upto '999999' but not on your device And won't allow for your convenience!" +
								"\nSorry but please try again with smaller number!!! :-/", Toast.LENGTH_LONG).show();
					} else
						Toast.makeText(getApplicationContext(),"Ooochhh!! \n" +
								"Seriously, You wanna try all limits??? The number '"+inpLong+"' is insanely big and won't allow for your convenience!" +
								"\nSorry but please try again with smaller number!!! :-/", Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(getApplicationContext(), "Oopss! Need Input to find Factorial!", Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 *  ProgressDialog common call with different message
	 * @param firstString
	 * @param secondString
	 * @return
	 */
	protected ProgressDialog callProgressDialog(String firstString, String secondString) {
		return ProgressDialog.show(MainActivity.this, firstString, secondString, true);
	}

	/**
	 * Switching to Result activity for displaying result.
	 * @param time
	 */
	protected void switchToResult(long time, long size){
		Intent i = new Intent(this, ResultActivity.class);
		i.putExtra("time", time);
		i.putExtra("size", size);

		// Set the request code to any code you like, you can identify the
		// callback via this code
		startActivityForResult(i, REQUEST_CODE);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item2:
			Toast.makeText(this, "Developed by Ankit Singh, DevGeeks Lab (http://devgeek.co)", Toast.LENGTH_SHORT).show();
			break;
		case R.id.item3:
			Toast.makeText(this, "The algorithm is implemented in C and called using JNI. Please refer " +
					"\nhttp://blog.devgeek.co for more details.", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * Async Task for executing NATIVE C code for finding Factorial
	 */
	class ExecuteFactCalc extends AsyncTask<Void, Void, Void> {
		long startTime;
		long endTime;
		long size;

		@Override
		protected void onPreExecute() {
			if (inpLong <= 1200)
				progressDialog= callProgressDialog("Executing!","Common! Try me. I can do better! :-D");
			else if(inpLong > 1200 && inpLong <= 5000)
				progressDialog= callProgressDialog("Executing! Please wait!","OK! you are warming up! :-)");
			else if (inpLong >5000 && inpLong <= 9999)
				progressDialog= callProgressDialog("Executing! Please wait!","Woohoo!! You are taking this seriously! ;-)");
			else if(inpLong > 9999 && inpLong <= 12000)
				progressDialog= callProgressDialog("Executing! Please wait!","Wow!! Seems you want to Benchmark your device!! ;-D");
			else if(inpLong>12000 && inpLong <=99999)
				progressDialog= callProgressDialog("Yup! We can do it! ;-)","Wooohoo!! Get some coffee & snacks." +
				"\nIt will take a while.....\nHINT: May be more than a minute..... ;-D");
			else {
				progressDialog= callProgressDialog("STOP!!!! DANGER!!!","You wanna fry your device! We support upto " +
				"factorial of 999999 but not on your device! :-/");
			}
		}

		@Override
		protected Void doInBackground(Void... nothing) {
			startTime = System.currentTimeMillis();
			try {
				factResult =  getFactorial(inpLong); // Calling native library for processing Factorial calculation
				size = getSize();
			} catch (Exception e) {
				e.printStackTrace();
			}
			endTime = System.currentTimeMillis();
			return null;
		}

		@Override
		protected void onPostExecute(Void nothing) {
			if (progressDialog !=null) {
				progressDialog.dismiss();
				long duration = endTime - startTime;
				System.out.println("Time Taken: "+duration);
				System.out.println("Size: "+size);
				switchToResult(duration, size); 
			}
		}

	};
}


