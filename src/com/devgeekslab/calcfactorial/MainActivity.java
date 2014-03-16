package com.devgeekslab.calcfactorial;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final String LOG_TAG = "CalcLargeFacts";

	EditText factInput;
	Button calcFactButton;

	private native boolean getFactorial(long input);

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
					long inpLong = Long.parseLong(userInput);
					Log.i(LOG_TAG, "Input From User: "+inpLong);
					StringBuilder strBuilder = new StringBuilder();
					strBuilder.append("UserInput: ").append(getFactorial((long) inpLong)).append(System.getProperty("line.separator"));

					TextView tv = (TextView) findViewById(R.id.textView1);
					tv.setText(strBuilder.toString());
				} else
					Toast.makeText(getApplicationContext(), "Oopsss! Please Give Input for find Factorial!", Toast.LENGTH_SHORT).show();
			}
		});

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
