package com.example.batteryalarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Ringtone ringtone;
    private TextView unplug;
    private Switch darkmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        unplug = findViewById(R.id.unplug);
        darkmode = findViewById(R.id.switch1);

        ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        BroadcastReceiver broadcastReceiverBattrery = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // for linking
                ImageView batteryimage = (ImageView) findViewById(R.id.batteryimage);
                Integer integerBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                Integer chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                Boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                Boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
                Resources res = context.getResources();

                // for images
                if (integerBatteryLevel >= 90) {
                    batteryimage.setImageDrawable(res.getDrawable(R.drawable.b100));
                } else if (90 > integerBatteryLevel && integerBatteryLevel >= 65) {
                    batteryimage.setImageDrawable(res.getDrawable(R.drawable.b75));
                } else if (65 > integerBatteryLevel && integerBatteryLevel >= 40) {
                    batteryimage.setImageDrawable(res.getDrawable(R.drawable.b50));
                } else if (40 > integerBatteryLevel && integerBatteryLevel >= 15) {
                    batteryimage.setImageDrawable(res.getDrawable(R.drawable.b25));
                } else {
                    batteryimage.setImageDrawable(res.getDrawable(R.drawable.b0));
                }
                textView.setText("Current Battery Charging levels is " + integerBatteryLevel.toString() + "%");

                //for charging full

                if (integerBatteryLevel > 99) {
                    if (acCharge || usbbCharge) {
                        ringtone.play();
                        unplug.setText("Please Unplug The Charger");
                    }
                }

                //for charging remove toast
                if (acCharge == false && usbCharge == false) {
                    ringtone.stop();
                    unplug.setText("");
                    if(integerBatteryLevel == 100 && acCharge == false && usbCharge == false ){
                    Toast.makeText(getApplicationContext(),"THANK YOU FOR CHOOSING OUR APP.", Toast.LENGTH_SHORT).show();
                    }
                }

                //implementing dark mode
                darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        }
                    }
                });

            }
        };
                registerReceiver(broadcastReceiverBattrery, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
            public void stopButton(View view) {
                ringtone.stop();
    }
}

