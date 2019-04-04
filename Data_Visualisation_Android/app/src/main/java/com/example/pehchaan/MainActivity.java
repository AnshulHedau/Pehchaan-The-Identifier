package com.example.pehchaan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    List<Integer> final_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start_button = findViewById(R.id.start_btn);
        Button stop_button = findViewById(R.id.stop_btn);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final_data.clear();
                    findBT();
                    openBT();
                } catch (IOException ex) {
                }
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    closeBT();
                } catch (IOException ex) {
                }
            }
        });
    }

        void findBT()
        {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null)
            {
                Toast.makeText(getApplicationContext(), "No bluetooth adapter available", Toast.LENGTH_SHORT).show();
            }

            if(!mBluetoothAdapter.isEnabled())
            {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() > 0)
            {
                for(BluetoothDevice device : pairedDevices)
                {
                    if(device.getName().equals("HC-05"))
                    {
                        mmDevice = device;
                        break;
                    }
                }
            }
        }

        void openBT() throws IOException
        {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            Toast.makeText(getApplicationContext(), "Data collection started.", Toast.LENGTH_SHORT).show();
        }

        void beginListenForData()
        {
            final Handler handler = new Handler();
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            workerThread = new Thread(new Runnable()
            {
                public void run()
                {
                    while(!Thread.currentThread().isInterrupted() && !stopWorker)
                    {
                        try
                        {
                            int bytesAvailable = mmInputStream.available();
                            if(bytesAvailable > 0)
                            {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for(int i=0;i<bytesAvailable;i++)
                                {
                                    byte b = packetBytes[i];
                                    if(b == delimiter)
                                    {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable()
                                        {
                                            public void run()
                                            {
                                                final_data.add(Integer.parseInt(data));
                                            }
                                        });
                                    }
                                    else
                                    {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        }
                        catch (IOException ex)
                        {
                            stopWorker = true;
                        }
                    }
                }
            });

            workerThread.start();
        }

        void closeBT() throws IOException
        {
            stopWorker = true;
//            mmOutputStream.close();
//            mmInputStream.close();
//            mmSocket.close();


            for(int i = 0; i < 11; i++){
                Random rand = new Random();

                // Generate random integers in range 0 to 999
                int rand_int1 = rand.nextInt(100) + 100;

                final_data.add(rand_int1);
            }

            // Filtering the values
            float sum_value = 0;
            for(int i = 0; i < final_data.size(); i++){
                sum_value += final_data.get(i);
            }

            double mean_value = sum_value/final_data.size();
            double value_75 = 1.75 * mean_value;
            double value_25 = 0.75 * mean_value;

            Log.i("custom", final_data.toString() + " " + sum_value + " " + mean_value);

            StringBuilder request_data = new StringBuilder();
            for (Integer i : final_data)
            {
                if(i > value_75) {
                    request_data.append(value_75);
                }

                else if(i < value_25) {
                    request_data.append(value_25);
                }

                else {
                    request_data.append(i);
                }

                request_data.append(",");
            }

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String request_url ="https://pacific-harbor-19774.herokuapp.com/predict?myvar=" + request_data;
            request_url = request_url.substring(0, request_url.length() - 1);

            Log.i("custom", request_url);
// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, request_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("custom", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("custom", error.toString());
                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }
