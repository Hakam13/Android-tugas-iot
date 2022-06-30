package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    Button tombolOvenON;
    Button tombolOvenOFF;
    TextView valueSuhuOven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MqttService mqttService = new MqttService(getApplicationContext());

        valueSuhuOven = findViewById(R.id.valuePembacaanSuhu);
        tombolOvenON = findViewById(R.id.tombolLampuOn); tombolOvenON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttService.kirimPesan("esp/on/off", "on", getApplicationContext());
            }
        });
        tombolOvenOFF = findViewById(R.id.tombolLampuOff); tombolOvenOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttService.kirimPesan("esp/on/off", "off", getApplicationContext());
            }
        });

        mqttService.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String pesan = message.toString();

                valueSuhuOven.setText(pesan);
                Log.e("pesan masuk:", pesan);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }
}