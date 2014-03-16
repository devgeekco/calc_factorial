package com.devgeekslab.calcfactorial;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final String LOG_TAG = "CalcLargeFacts";

	EditText factInput;
	Button calcFactButton;
	ProgressDialog progressDialog = null;

	protected long inpLong;

	private native int[] getFactorial( long input);

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

		calcFactButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String userInput = factInput.getText().toString();

				if(!userInput.isEmpty()) {
					inpLong = Long.parseLong(userInput);
					Log.i(LOG_TAG, "Input From User: "+inpLong);
					new ExecuteFactCalc().execute();
					//StringBuilder strBuilder = new StringBuilder();
					/*int[] factResult =  getFactorial(inpLong);
					System.out.println("Length:: "+factResult.length);
					// strBuilder.append("UserInput: ").append(getFactorial((long) inpLong)).append(System.getProperty("line.separator"));
					int temp=0;
					for(int i=factResult.length-1;i>=0;i--){

						if((factResult[i]!=0) || (temp!=0)){
							System.out.println(factResult[i]);
							temp=1;
						}
					}*/
					//executeFactorialCalc(inpLong); // do in background

					// TextView tv = (TextView) findViewById(R.id.textView1);
					// tv.setText(strBuilder.toString());
				} else
					Toast.makeText(getApplicationContext(), "Oopss! Need Input to find Factorial!", Toast.LENGTH_SHORT).show();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class ExecuteFactCalc extends AsyncTask<Void, Void, Void> {
		int[] factResult;

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
				progressDialog= callProgressDialog("Seriously!!! :-|","Woooopp!! Get some coffee & snacks. It will take a while..... ;-D");
			else
				progressDialog= callProgressDialog("STOP!!!! DANGER!!!","You wanna fry your device! :-/");
		}

		@Override
		protected Void doInBackground(Void... nothing) {
			try {
				factResult =  getFactorial(inpLong);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void nothing) {
			if (progressDialog !=null) {
				progressDialog.dismiss();
				int temp=0;
				for(int i=factResult.length-1;i>=0;i--){

					if((factResult[i]!=0) || (temp!=0)){
						System.out.println(factResult[i]);
						temp=1;
					}
				}
				
			}
		}
		
		private ProgressDialog callProgressDialog(String first, String second) {
			return ProgressDialog.show(MainActivity.this, first,second, true);
		}

	};
}


