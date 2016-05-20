package com.itu.jonathanlab.tcpclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView mList;
    private ArrayList<String> arrayList;
    private MyCustomAdapter mAdapter;
    private TCPClient mTcpClient;

    String commands = "";
    String ipAdress;
    //MainActivity activity = new MainActivity();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<String>();

        //final EditText editText = (EditText) findViewById(R.id.editText);
        Button send = (Button)findViewById(R.id.send_button);
        Button restart = (Button)findViewById(R.id.re_button);
        Button connect = (Button)findViewById(R.id.connect_button);

        //relate the listView from java to the one created in xml
        mList = (ListView)findViewById(R.id.list);
        mAdapter = new MyCustomAdapter(this, arrayList);
        mList.setAdapter(mAdapter);

        // connect to the server
        //mTcpClient.setIpadress("10.26.12.222");
       /*new connectTask().execute("");
        Connect();*/

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = "echo";


                //add the text in the arrayList
                arrayList.add("c: " + message);

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();

            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Connect");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setMessage("Connect to server");

                Context context = MainActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText InputIP = new EditText(context);
                InputIP.setHint("Insert IP");
                layout.addView(InputIP);

                builder.setView(layout);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            ipAdress = InputIP.getText().toString();
                            new connectTask().execute("");
                            Connect();
                        } catch (Exception ex) {
                            Toast.makeText(MainActivity.this, "Failed to connect!", Toast.LENGTH_LONG).show();
                        }

                        //ListView list = (ListView) getActivity().findViewById(R.id.list_item);
//                        ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();



                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                builder.show();

            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String message = editText.getText().toString();


                //add the text in the arrayList
                arrayList.add("Restarted");
                arrayList.add(mTcpClient.SERVERIP);
                mTcpClient.stopClient();
                new connectTask().execute("");
                mTcpClient.run();


                //refresh the list
                mAdapter.notifyDataSetChanged();
                //editText.setText("");
            }
        });

    }
public void Connect(){
    String message = "echo";


    //add the text in the arrayList
    arrayList.add("c: " + message);

    //sends the message to the server
    if (mTcpClient != null) {
        mTcpClient.sendMessage(message);
    }

    //refresh the list
    mAdapter.notifyDataSetChanged();

}
    public class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {


                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    // do the switch case for hand gestures

                    String t1 = "1";
                    String t2 = "2";
                    String t3 = "3";
                    String t4 = "4";
                    String t5 = "5";
                    String t6 = "6";
                    String t7 = "7";


                    if(t1.equals(message)){
                        commands = "gesture 1";
                        publishProgress("Called: " + commands);

                        swipeGestureInfo("swiped left",1);


                    }
                    else if(t2.equals(message)){
                        commands = "gesture 2";
                        publishProgress("Called: "+ commands);

                        swipeGestureInfo("swiped right", 2);

                    }
                    else if(t3.equals(message)){
                        commands = "gesture 3";
                        publishProgress("Called: "+ commands);

                        swipeGestureInfo("swiped up",3);

                    }
                    else if(t4.equals(message)){
                        commands = "gesture 4";
                        publishProgress("Called: "+ commands);
                        swipeGestureInfo("swiped down",4);

                    }
                    else if(t5.equals(message)){
                        commands = "gesture 5";
                        publishProgress("Called: " + commands);

                        swipeGestureInfo("zoomed in",5);

                    }
                    else if(t6.equals(message)){
                        commands = "gesture 6";
                        publishProgress("Called: "+ commands);
                        swipeGestureInfo("zoomed out",6);

                    }
                    else if (t7.equals(message)){
                        commands = "gesture 7";
                        publishProgress("Called: "+ commands + "(idle state)");
                        swipeGestureInfo("idle",7);
                    }
                    else{
                        publishProgress("Wrong input: " + message);
                        swipeGestureInfo("wrong input",7);
                    }

                }
            });
            //mTcpClient.setIpadress("10.2.26.100");
            //mTcpClient.setIpadress("192.168.1.19");
            mTcpClient.setIpadress(ipAdress);
            mTcpClient.run();

            return null;
        }



public void swipeGestureInfo(final String GestureMsg, final int animtype){
    MainActivity.this.runOnUiThread(new Runnable() {
        public void run() {
            Toast.makeText(MainActivity.this, GestureMsg, Toast.LENGTH_SHORT).show();
            if (animtype == 1) {
                ImageView image = (ImageView) findViewById(R.id.imageView);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.swipeleft);
                image.startAnimation(animation);
            }
            else if (animtype == 2) {
                ImageView image = (ImageView) findViewById(R.id.imageView);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.swiperight);
                image.startAnimation(animation);
            }
            else if (animtype == 3) {
                ImageView image = (ImageView) findViewById(R.id.imageView);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.swipeup);
                image.startAnimation(animation);
            }
            else if (animtype == 4) {
                ImageView image = (ImageView) findViewById(R.id.imageView);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.swipedown);
                image.startAnimation(animation);
            }
            else if (animtype == 5) {
                ImageView image = (ImageView) findViewById(R.id.imageView);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                image.startAnimation(animation);
            }
            else if (animtype == 6) {
                ImageView image = (ImageView) findViewById(R.id.imageView);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomout);
                image.startAnimation(animation);
            }
            else{
                return;
            }

        }
    });
}
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);


            //in the arrayList we add the messaged received from server
            arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();
        }
    }
    }
