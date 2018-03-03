package com.hospicebangladesh.pms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;

import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.util.ChatBot;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.hospicebangladesh.pms.http.HttpRequest;
import com.hospicebangladesh.pms.http.HttpRequestCallBack;
import com.hospicebangladesh.pms.model.User;
import com.hospicebangladesh.pms.utils.Session;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import io.hypertrack.smart_scheduler.Job;
import io.hypertrack.smart_scheduler.SmartScheduler;
import okhttp3.Response;

public class ChatViewActivity extends AppCompatActivity implements SmartScheduler.JobScheduledCallback {

    private static final String TAG = "ChatViewActivity";
    private ChatView mChatView;
    public String chatPostUrl = "chat.php";

    private static final int JOB_ID = 1;

    private static final String JOB_PERIODIC_TASK_TAG = "io.hypertrack.android_scheduler_demo.JobPeriodicTask";
    User me, you;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        //User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        String myName = "Me";

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Help Line";

        me = new User(myId, myName, myIcon);
        you = new User(yourId, yourName, yourIcon);

        mChatView = (ChatView) findViewById(R.id.chat_view);

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBackground));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.black));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);

        addJob();

        try {
            chat("");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Click Send Button
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "click");
                //new message
           /*          Message message = new Message.Builder()
                        .setUser(me)
                        .setRight(true)
                        .setText(mChatView.getInputText())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                mChatView.send(message);*/

                try {
                    chat(mChatView.getInputText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Reset edit text
                mChatView.setInputText("");


                //Receive message
           /*     final Message receivedMessage = new Message.Builder()
                        .setUser(you)
                        .setRight(false)
                        .setText(ChatBot.INSTANCE.talk(me.getName(), message.getText()))
                        .build();

                // This is a demo bot
                // Return within 3 seconds
                int sendDelay = (new Random().nextInt(4) + 1) * 1000;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.receive(receivedMessage);
                    }
                }, sendDelay);*/


            }

        });

    }


    public void chat(String txt) throws JSONException {


        String user_id = Session.getPreference(getApplicationContext(), Session.user_id);

        JSONObject postBody = new JSONObject();
        postBody.put("user_id", user_id);
        postBody.put("chat_text", txt);

        try {
            HttpRequest.postRequest(chatPostUrl, postBody.toString(), new HttpRequestCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    final String serverResponse = response.body().string();
                    Log.d(TAG, serverResponse);
                    ChatViewActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject json = new JSONObject(serverResponse);
                                int success = json.getInt("success");
                                String message = json.getString("message");
                                if (success == 1) {
                                    JSONArray jsonArrayChats = json.getJSONArray("chats");
                                    mChatView.getMessageView().removeAll();
                                    for (int i = 0; i < jsonArrayChats.length(); i++) {
                                        JSONObject objChats = jsonArrayChats.getJSONObject(i);

                                        String chat_text = objChats.getString("chat_text");
                                        String user_type = objChats.getString("user_type");

                                        if (user_type.equals("1")) {
                                            Message sendMessage = new Message.Builder()
                                                    .setUser(me)
                                                    .setRight(true)
                                                    .setText(chat_text)
                                                    .hideIcon(true)
                                                    .build();
                                            //Set to chat view
                                            mChatView.send(sendMessage);


                                        } else {

                                             Message receivedMessage = new Message.Builder()
                                                    .setUser(you)
                                                    .setRight(false)
                                                    .setText(chat_text)
                                                     .hideIcon(true)
                                                    .build();

                                            mChatView.receive(receivedMessage);


                                        }

                                    }

                                } else {
                                 //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onFail() {
                    ChatViewActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d(TAG, " onFail");
                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void addJob() {

        SmartScheduler jobScheduler = SmartScheduler.getInstance(this);

        // Check if any periodic job is currently scheduled
        if (jobScheduler.contains(JOB_ID)) {
           // removePeriodicJob();
            return;
        }

        // Create a new job with specified params
        Job job = createJob();
        if (job == null) {
            Toast.makeText(ChatViewActivity.this, "Invalid paramteres specified. " +
                    "Please try again with correct job params.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Schedule current created job
        if (jobScheduler.addJob(job)) {
          //  Toast.makeText(ChatViewActivity.this, "Job successfully added!", Toast.LENGTH_SHORT).show();

            if (job.isPeriodic()) {
                Log.d(TAG, "JobPeriodic");
            } else {
                Log.d(TAG, "JobNonPeriodic");
            }
        }


    }


    private Job createJob() {


        String intervalInMillisString = "1000";


        Long intervalInMillis = Long.parseLong(intervalInMillisString);

        Job.Builder builder = new Job.Builder(JOB_ID, this, Job.Type.JOB_TYPE_PERIODIC_TASK, JOB_PERIODIC_TASK_TAG)
                .setRequiredNetworkType(Job.NetworkType.NETWORK_TYPE_ANY)
                .setRequiresCharging(false)
                .setIntervalMillis(intervalInMillis);

        if (true) {
            builder.setPeriodic(intervalInMillis);
        }

        Job job = builder.build();
        return job;
    }


    private void removePeriodicJob() {


        SmartScheduler jobScheduler = SmartScheduler.getInstance(this);
        if (!jobScheduler.contains(JOB_ID)) {
            Toast.makeText(ChatViewActivity.this, "No job exists with JobID: " + JOB_ID, Toast.LENGTH_SHORT).show();
            return;
        }

        if (jobScheduler.removeJob(JOB_ID)) {
            Toast.makeText(ChatViewActivity.this, "Job successfully removed!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onJobScheduled(Context context, final Job job) {
        if (job != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        chat("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                 //   Toast.makeText(ChatViewActivity.this, "Job: " + job.getJobId() + " scheduled!", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d(TAG, "Job: " + job.getJobId() + " scheduled!");

            if (!job.isPeriodic()) {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Session.clearPreference(getApplicationContext());
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
