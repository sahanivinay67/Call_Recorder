package com.example.dailapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.dailapp.utils.CountryCode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.i18n.phonenumbers.NumberParseException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

   TextInputEditText et_name,et_number;
    private static String NULL_START = "";
    private Vibrator vibrator;
    private CountryCode mCountryCode;
    ToggleButton toggleButton;
    TextView textSubHeader;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        textSubHeader = (TextView) findViewById(R.id.textSubHeader);
        et_name = (TextInputEditText) findViewById(R.id.et_name);
        et_number = (TextInputEditText) findViewById(R.id.et_number);

        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnLongClickListener(this);

        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_0).setOnLongClickListener(this);

        findViewById(R.id.btn_call).setOnClickListener(this);


        et_number.setText(NULL_START);

        vibrator=(Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);


        // Automatic get the country code
        mCountryCode = new CountryCode(getSystemService(Context.TELEPHONY_SERVICE));
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                et_number.setText(NULL_START);

                break;
            case R.id.btn_0:
                try {
                    addNumberToDialer(getString(R.string.plus));
                } catch (NumberParseException e) {
                    e.printStackTrace();
                }
                break;
        }
        if(vibrator!=null)
        {
            vibrator.vibrate(getResources().getInteger(R.integer.digit_erase_all_vibration_milliseconds));
        }


        
        return true;
    }

    @Override
    public void onClick(View v) {
        String mNumber = et_number.getText().toString();

        switch (v.getId()) {
            case R.id.btn_delete:
                // If the content of the textView is not empty
                if (!et_number.getText().toString().isEmpty()) {
                    //Delete the last digit
                    et_number.setText(
                            mNumber.substring(0, mNumber.length() - 1)
                    );
                }
                break;
            case R.id.btn_call:
                // getting number from all inputs

                String mNumberToCall = new NumberFormatter(et_number.getText()
                        .toString()).getmRawNumber();
                //getting name from user
                String name=et_name.getText().toString();

                if (mNumberToCall.isEmpty()&&name.isEmpty())
                {
                    Toast.makeText(this, "Please Check Name And Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                try {
                    intent = new Intent(Intent.ACTION_CALL, Uri.parse(
                            mCountryCode.getmIntentCallNumber(mNumberToCall)
                    ));
                    //checking toggle is on or off

                    boolean checked = (toggleButton).isChecked();
                    if (checked) {

                        Intent serviceIntent = new Intent(MainActivity.this, CallRecorder.class);
                        serviceIntent.putExtra("Phone",mNumberToCall);
                        serviceIntent.putExtra("Name",name);
                        startService(serviceIntent);



                    } else {

                        Toast.makeText(getApplicationContext(), "Auto Call Recording is set OFF", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberParseException e) {
                    e.printStackTrace();
                }

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);
                } else {
                    startActivity(intent);
                }

                break;
            default:
                // get the button clicked
                Button button = (Button) v;

                // Get the text from the button clicked
                String number = button.getText().toString();
                try {
                    addNumberToDialer(number);
                } catch (NumberParseException e) {
                    e.printStackTrace();
                }

                break;
        }
        if(vibrator!=null)
        {
            vibrator.vibrate(getResources().getInteger(R.integer.digit_erase_once_vibration_milliseconds));
        }



    }

    @Override
    protected void onResume() {
        super.onResume();

        // Runtime permission
        try {

            boolean permissionGranted_OutgoingCalls = ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_phoneState = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_recordAudio = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_WriteExternal = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            boolean permissionGranted_ReadExternal = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


            if (permissionGranted_OutgoingCalls) {
                if (permissionGranted_phoneState) {
                    if (permissionGranted_recordAudio) {
                        if (permissionGranted_WriteExternal) {
                            if (permissionGranted_ReadExternal) {
                                try {
                                    toggleButton.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
                            }
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                        }
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 400);
                    }
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 500);
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 600);
            }

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }


    @SuppressLint("ResourceAsColor")
    public void toggleButtonClick(View view) {

        boolean checked = ((ToggleButton)view) .isChecked();
        if (checked) {

            Toast.makeText(getApplicationContext(), "Auto Call Recording is set ON", Toast.LENGTH_SHORT).show();
            textSubHeader.setText("Switch on Toggle to record your calls");


        } else {

            Toast.makeText(getApplicationContext(), "Auto Call Recording is set OFF", Toast.LENGTH_SHORT).show();
            textSubHeader.setText("Switch Off Toggle to stop recording your calls");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 || requestCode == 300 || requestCode == 400 || requestCode == 500 || requestCode == 600) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    toggleButton.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private void addNumberToDialer(String number) throws NumberParseException {
        et_number.append(number);
    }
}