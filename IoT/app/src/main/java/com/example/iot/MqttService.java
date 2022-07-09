package com.example.iot;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MqttService extends Service {

    public static final String topikSuhuOven = "esp/dht/temperatur";

    MqttAndroidClient client;
    byte[] encodedPayload = new byte[0];
    public static final String serverURI = "tcp://broker.hivemq.com:1883";


    public static final String clientId = MqttClient.generateClientId();
    int subscribeToTopicsAttempts = 0;

    public MqttService(Context context) {
        MqttConnectOptions options = new MqttConnectOptions();
        client = new MqttAndroidClient(context, serverURI, clientId);
        if (!client.isConnected()) {
            try {
                IMqttToken token = client.connect(options);
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // We are connected
                        Log.d("Status:", "onSuccess");
                        Toast.makeText(context, "Terhubung :)", Toast.LENGTH_SHORT).show();
                        if (subscribeToTopicsAttempts <= 0) {
                            subscribeKeTopik();
                            subscribeToTopicsAttempts++;
                        } else {
                            //do nun.
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        // Something went wrong e.g. connection timeout or firewall problems
                        Log.d("Status:", "onFailure");
                        Toast.makeText(context, "Gagal Terhubung :(", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            // do nun
        }
    }

    public void setCallback (MqttCallbackExtended callback){
        client.setCallback(callback);
    }

    public void subscribeKeTopik(){
        try {
            client.subscribe(topikSuhuOven, 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void kirimPesan(String topic, String m, Context context) {
        try {
            encodedPayload = m.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(false);
            client.publish(topic, message);
            Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
            Toast.makeText(context, "Gagal"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
