package co.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Iot.PreferencesActivity;
import co.kaua.palacepetz.R;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

@SuppressWarnings("deprecation")
public class DevicePairActivity extends AppCompatActivity {
    private ListView listView;
    private BluetoothAdapter mBTAdapter;
    private static final int BT_ENABLE_REQUEST = 10; // This is the code we use for BT Enable
    private static final int SETTINGS = 20;
    private UUID mDeviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private int mBufferSize = 50000; // Default
    public static final String DEVICE_EXTRA = "com.example.lightcontrol.SOCKET";
    public static final String DEVICE_UUID = "com.example.lightcontrol.uuid";
    private static final String DEVICE_LIST = "com.example.lightcontrol.devicelist";
    private static final String DEVICE_LIST_SELECTED = "com.example.lightcontrol.devicelistselected";
    public static final String BUFFER_SIZE = "com.example.lightcontrol.buffersize";
    private static final String TAG = "BlueTest5-PairDevice";
    private final LoadingDialog loadingDialog = new LoadingDialog(DevicePairActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_device);
        setTheme(R.style.DevicePresentation);
        listView = findViewById(R.id.listView_devices);

        if (savedInstanceState != null) {
            ArrayList<BluetoothDevice> list = savedInstanceState.getParcelableArrayList(DEVICE_LIST);
            if (list != null) {
                initList(list);
                MyAdapter adapter = (MyAdapter) listView.getAdapter();
                int selectedIndex = savedInstanceState.getInt(DEVICE_LIST_SELECTED);
                if (selectedIndex != -1) {
                    adapter.setSelectedIndex(selectedIndex);
                }
            } else {
                initList(new ArrayList<>());
            }
        } else {
            initList(new ArrayList<>());
        }

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBTAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not found", Toast.LENGTH_SHORT).show();
        } else if (!mBTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, BT_ENABLE_REQUEST);
        } else {
            new SearchDevices().execute();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_ENABLE_REQUEST:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, R.string.bluetooth_enable_success, Toast.LENGTH_SHORT).show();
                    new SearchDevices().execute();
                } else {
                    Toast.makeText(this, R.string.bluetooth_couldnt_success, Toast.LENGTH_SHORT).show();
                }

                break;
            case SETTINGS: //If the settings have been updated
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String uuid = prefs.getString("prefUuid", "Null");
                mDeviceUUID = UUID.fromString(uuid);
                Log.d(TAG, "UUID: " + uuid);
                String bufSize = prefs.getString("prefTextBuffer", "Null");
                mBufferSize = Integer.parseInt(bufSize);
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //  Initialize the List adapter
    private void initList(List<BluetoothDevice> objects) {
        final MyAdapter adapter = new MyAdapter(getApplicationContext(), R.layout.adapter_list_devices, R.id.lstContent, objects);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> adapter.setSelectedIndex(position));
    }

    /**
     * Searches for paired devices. Doesn't do a scan! Only devices which are paired through Settings-> Bluetooth
     * will show up with this. I didn't see any need to re-build the wheel over here
     *
     * @author Kauã Vitorio
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class SearchDevices extends AsyncTask<Void, Void, List<BluetoothDevice>> {

        @Override
        protected void onPreExecute() {
            loadingDialog.startLoading();
        }

        @Override
        protected List<BluetoothDevice> doInBackground(Void... params) {
            Set<BluetoothDevice> pairedDevices = mBTAdapter.getBondedDevices();
            List<BluetoothDevice> listDevices = new ArrayList<>();
            Activity context = DevicePairActivity.this;
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String[] arrayDeviceName = deviceName.split("_");
                if (arrayDeviceName[0].equals("Palace") || arrayDeviceName[0].equals("PALACE")){
                    listDevices.add(device);
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    //BluetoothDevice devices = device;
                    Intent intent = new Intent(getApplicationContext(), DeviceControlling.class);
                    intent.putExtra(DEVICE_EXTRA, device);
                    intent.putExtra(DEVICE_UUID, mDeviceUUID.toString());
                    intent.putExtra(BUFFER_SIZE, mBufferSize);
                    startActivity(intent);
                    context.finish();
                    Log.d("PairStatus", "Device Is found");
                    /*if (deviceHardwareAddress.equals("00:21:13:00:BC:20")) {

                    }*/
                }else{
                    Log.d("PairStatus", "Device Is not found");
                    Intent goTo_tips = new Intent(getApplicationContext(), DeviceTipsActivity.class);
                    goTo_tips.putExtra("NotFound", true);
                    startActivity(goTo_tips);
                    context.finish();
                }
                return listDevices;
            }
            return listDevices;
        }

        @Override
        protected void onPostExecute(List<BluetoothDevice> listDevices) {
            super.onPostExecute(listDevices);
            loadingDialog.dimissDialog();
            if (listDevices.size() > 0) {
                MyAdapter adapter = (MyAdapter) listView.getAdapter();
                adapter.replaceItems(listDevices);
            } else {
                Log.d("PairStatus", "Device Is not found");
            }
        }

    }

    /**
     * Custom adapter to show the current devices in the list. This is a bit of an overkill for this
     * project, but I figured it would be good learning
     * Most of the code is lifted from somewhere but I can't find the link anymore
     *
     * @author Kauã Vitorio
     */
    public static class MyAdapter extends ArrayAdapter<BluetoothDevice> {
        private int selectedIndex;
        private final Context context;
        private final int selectedColor = Color.parseColor("#abcdef");
        private List<BluetoothDevice> myList;

        public MyAdapter(Context ctx, int resource, int textViewResourceId, List<BluetoothDevice> objects) {
            super(ctx, resource, textViewResourceId, objects);
            context = ctx;
            myList = objects;
            selectedIndex = -1;
        }

        public void setSelectedIndex(int position) {
            selectedIndex = position;
            notifyDataSetChanged();
        }

        public BluetoothDevice getSelectedItem() {
            return myList.get(selectedIndex);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public BluetoothDevice getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public static class ViewHolder {
            TextView tv;
            ImageView imgPairDevice;
        }

        public void replaceItems(List<BluetoothDevice> list) {
            myList = list;
            notifyDataSetChanged();
        }

        public List<BluetoothDevice> getEntireList() {
            return myList;
        }

        @SuppressLint({"InflateParams", "SetTextI18n"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            ViewHolder holder;
            if (convertView == null) {
                vi = LayoutInflater.from(context).inflate(R.layout.adapter_list_devices, null);
                holder = new ViewHolder();

                holder.tv = vi.findViewById(R.id.lstContent);
                holder.imgPairDevice = vi.findViewById(R.id.imgPairDevice);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            if (selectedIndex != -1 && position == selectedIndex) {
                holder.tv.setBackgroundColor(selectedColor);
            } else {
                holder.tv.setBackgroundColor(Color.WHITE);
            }
            BluetoothDevice device = myList.get(position);
            holder.tv.setText(device.getName() + "\n" + device.getAddress());
            String deviceName = device.getName();
            String[] array = deviceName.split("_");
            if(array[0].equals("Palace") || array[0].equals("PALACE")){
                holder.imgPairDevice.setImageResource(R.drawable.cat_sitting);
            }else{
                holder.imgPairDevice.setImageResource(R.drawable.bluetooth_icon);
            }
            return vi;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(DevicePairActivity.this, PreferencesActivity.class);
        startActivityForResult(intent, SETTINGS);
        return super.onOptionsItemSelected(item);
    }
}