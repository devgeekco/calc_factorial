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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * @author Ankit Singh
 * @copyright DevGeeksLab 2014
 * Refer License in the projects folder for terms and condition
 * to use this Android application.
 * 
 * @TODO: Send and recieve STRUCT between native and java android i.e. factorial array,
 * maximum result length etc.
 * @TODO: find a way to include result array in ResultActivity.
 * @TODO: About me activiy class
 * @TODO: add way to show different fun-facts about factorial.
 * @TODO: find best advertisement payment subscriber. 
 */
public class MainActivity extends Activity {
	private static final int REQUEST_CODE = 10;
	private final String LOG_TAG = "CalcLargeFacts";

	EditText factInput;
	Button calcFactButton;
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
					if(inpLong < 100000)
						new ExecuteFactCalc().execute();
					else {	
						Toast.makeText(getApplicationContext(), "STOP!!!! DANGER!!!\nYou wanna fry your device! We support upto " +
						"factorial of 999999 but not on your device! \n Please Try again !!! :-/", Toast.LENGTH_LONG).show();
					}
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
				progressDialog= callProgressDialog("Yup! We can do it! ;-)","Wooohoo!! Get some coffee & snacks. \nIt will take a while..... ;-D");
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


