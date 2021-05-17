package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import com.airbnb.lottie.LottieAnimationView;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Iot.ActivityHelper;
import co.kaua.palacepetz.R;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 * @author Kaua Vitorio
 **/

public class DeviceControlling extends AppCompatActivity {

    private static final String TAG = "BlueTest5-Controlling";
    int mMaxChars = 50000;//Default//change this to string......
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private ReadInput mReadThread = null;

    boolean mIsUserInitiatedDisconnect = false;
    private boolean mIsBluetoothConnected = false;
    int MEU_REQUEST_CODE = 100;

    //  Bluetooth Device
    private BluetoothDevice mDevice;

    //  Bluetooth Commands
    final static String passPutWater = "92"; // Password to device put water
    final static String lightOn = "79";//off
    final static String lightOFF = "78";//off
    final static String offDisconect = "000";//off
    final static String onConnectBlue = "001";//off
    SharedPreferences prefs;


    LoadingDialog loadingDialog;
    LottieAnimationView arrowGoBackDeviceControlling, btnVoice_Controlling;
    ConstraintLayout btn_PutWater, btn_PutRacao, btn_Disconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_controlling);
        setTheme(R.style.DevicePresentation);

        //ActivityHelper.initialize(this);
        loadingDialog = new LoadingDialog(this);
        btn_PutWater = findViewById(R.id.btn_PutWater);
        btn_PutRacao = findViewById(R.id.btn_PutRacao);
        btn_Disconnect = findViewById(R.id.btn_Disconnect);
        arrowGoBackDeviceControlling = findViewById(R.id.arrowGoBackDeviceControlling);
        btnVoice_Controlling = findViewById(R.id.btnVoice_Controlling);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        /*String orientation = prefs.getString("prefOrientation", "Null");
                Log.d(TAG, "Orientation: " + orientation);
                switch (orientation) {
                    case "Landscape":
                    case "Portrait":
                    case "Auto":
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                }*/


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(DevicePairActivity.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(DevicePairActivity.DEVICE_UUID));
        mMaxChars = b.getInt(DevicePairActivity.BUFFER_SIZE);
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new ConnectBT().execute();
        }

        Log.d(TAG, "Ready");

        btnVoice_Controlling.setOnClickListener(v -> {
            Intent i = new Intent(
                    RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

            i.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "Say something");

            startActivityForResult(i, MEU_REQUEST_CODE);
        });


        btn_PutWater.setOnClickListener(v -> {
            try {
                mBTSocket.getOutputStream().write(passPutWater.getBytes());
                //onResume();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*String orientation = prefs.getString("prefOrientation", "Null");
        Log.d(TAG, "Orientation: " + orientation);
        switch (orientation) {
            case "Landscape":
            case "Portrait":
            case "Auto":
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
        }*/

        if (requestCode == MEU_REQUEST_CODE
                && resultCode == RESULT_OK) {

            // Contém a lista com os resultados
            ArrayList matches =
                    data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);
            String VoiceResult = (String) matches.get(0);
            Toast.makeText(this, VoiceResult, Toast.LENGTH_SHORT).show();
            if (VoiceResult.equals("put water") || VoiceResult.equals("Put Water") || VoiceResult.equals("Coloque Água") || VoiceResult.equals("coloque água")){
                try {
                    mBTSocket.getOutputStream().write(lightOn.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
            if (VoiceResult.equals("remove water") || VoiceResult.equals("Remove Water") || VoiceResult.equals("Remova a Água") || VoiceResult.equals("remova a água")){
                try {
                    mBTSocket.getOutputStream().write(lightOFF.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "No Results", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        try {
            mBTSocket.getOutputStream().write(offDisconect.getBytes());
            finish();
            if (mBTSocket != null && mIsBluetoothConnected) {
                new DisConnectBT().execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private class ReadInput implements Runnable {

        private boolean bStop = false;
        private final Thread t;

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            InputStream inputStream;
            try {
                inputStream = mBTSocket.getInputStream();
                while (!bStop) {
                    byte[] buffer = new byte[256];
                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);
                        int i;
                        /*
                         * This is needed because new String(buffer) is taking the entire buffer i.e. 256 chars on Android 2.3.4 http://stackoverflow.com/a/8843462/1287554
                         */
                        //noinspection StatementWithEmptyBody
                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                        }
                        final String strInput = new String(buffer, 0, i);

                        /*
                         * If checked then receive text, better design would probably be to stop thread if unchecked and free resources, but this is a quick fix
                         */
                    }
                    Thread.sleep(500);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

        public void stop() {
            bStop = true;
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class DisConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {//cant inderstand these dotss

            if (mReadThread != null) {
                mReadThread.stop();
                while (mReadThread.isRunning())
                    ; // Wait until it stops
                mReadThread = null;

            }

            try {
                mBTSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mIsBluetoothConnected = false;
            if (mIsUserInitiatedDisconnect) {
                finish();
            }
        }

    }
/*
    @Override
    protected void onPause() {
        if (mBTSocket != null && mIsBluetoothConnected) {
            new DisConnectBT().execute();
        }
        Log.d(TAG, "Paused");
        super.onPause();
    }
/*
    @Override
    protected void onResume() {
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new ConnectBT().execute();
        }
        Log.d(TAG, "Resumed");
        super.onResume();
    }
*/
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {
            loadingDialog.startLoading();
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (mBTSocket == null || !mIsBluetoothConnected) {
                    mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                }
            } catch (IOException e) {
                // Unable to connect to device
                mConnectSuccessful = false;
                Log.d("ConnectBTStatus", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!mConnectSuccessful) {
                Toast.makeText(getApplicationContext(), R.string.could_connect_verify_device, Toast.LENGTH_LONG).show();
                Intent goTo_main = new Intent(DeviceControlling.this, MainActivity.class);
                startActivity(goTo_main);
                finish();
            } else {
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput();
                try {
                    mBTSocket.getOutputStream().write(onConnectBlue.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }// Kick off input reader
            }
            loadingDialog.dimissDialog();
        }
    }
}
