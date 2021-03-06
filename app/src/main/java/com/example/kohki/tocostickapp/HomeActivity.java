package com.example.kohki.tocostickapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import jp.ksksue.driver.serial.FTDriver;

public class HomeActivity extends Activity {

    Button btnPseudoArduino, btnDataGetter,btnChat, btnBegin, btnEnd;
    FTDriver mSerial;
    private TextView mText;

    // [FTDriver] Permission String
    private static final String ACTION_USB_PERMISSION = "com.example.kohki.USB_PERMISSION"; //"jp.ksksue.tutorial.USB_PERMISSION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar. This have to write setContentView() .  err:
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);


        changeBtnAccesser(false);

        btnBegin         = (Button) findViewById(R.id.btnBegin);
        btnEnd           = (Button) findViewById(R.id.btnEnd);
        btnDataGetter    = (Button) findViewById(R.id.btn_data_getter);
        btnPseudoArduino = (Button) findViewById(R.id.btn_pse_ard);
        btnChat         = (Button) findViewById(R.id.chat);
        mText = (TextView) findViewById(R.id.textView1);

        // [FTDriver] Create Instance
        mSerial = new FTDriver((UsbManager)getSystemService(Context.USB_SERVICE));

        // [FTDriver] setPermissionIntent() before begin()
        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        mSerial.setPermissionIntent(permissionIntent);

        btnPseudoArduino.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReceiveActivity.class);
                startActivity(intent);
            }
        });
        btnDataGetter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ReceiveActivity.class);
                startActivity(intent);
            }
        });
        btnChat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });

    }

    public void changeBtnAccesser(boolean is_access){
        btnBegin          = (Button) findViewById(R.id.btnBegin);
        btnEnd            = (Button) findViewById(R.id.btnEnd);
        btnDataGetter    = (Button) findViewById(R.id.btn_data_getter);
        btnPseudoArduino = (Button) findViewById(R.id.btn_pse_ard);
        btnChat           = (Button) findViewById(R.id.chat);
        mText             = (TextView) findViewById(R.id.textView1);

        btnDataGetter.setEnabled(is_access);
        btnPseudoArduino.setEnabled(is_access);
    //    btnChat.setEnabled(is_access);
        btnBegin.setEnabled(!is_access);
        btnEnd.setEnabled(is_access);

        if(is_access) {
            mText.setText("接続中");
            mText.setTextColor(Color.BLUE);
        }else{
            mText.setText("未接続");
            mText.setTextColor(Color.RED);
        }
    }
    public void onBeginClick(View view) {
       // testOfWrite();

        // [FTDriver] Open USB Serial
        if(mSerial.begin(FTDriver.BAUD115200)) {
            changeBtnAccesser(true);
    //        String wbuf = ":788001000F0000000000000000F8\r\n";
    //        mSerial.write(wbuf.getBytes());
        }
    }
    public void onEndClick(View view) {
        changeBtnAccesser(false);
        //    String wbuf = ":788001000F0000000000000000F8\r\n";
        //    mSerial.write(wbuf.getBytes());
        if (mSerial.isConnected()) {
            Toast.makeText(this,"end()",Toast.LENGTH_SHORT).show();
            mSerial.end();
        }else{
            Toast.makeText(this,"ended",Toast.LENGTH_SHORT).show();
        }

    }
}
