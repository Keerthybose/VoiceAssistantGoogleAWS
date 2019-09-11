package com.example.aiapiapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener
{
    private TextToSpeech textToSpeech;
    private AIService aiService;
    private Button listenButton;
    private TextView resultTextView;
    private CloudService cloudService;
    private CameraManager mCameraManager;
    private String mCameraId;
    private ConversationWidget conversationWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conversationWidget =  findViewById(R.id.conversation_widget);

        cloudService=new CloudService();
        listenButton = (Button) findViewById(R.id.listenButton);
       // resultTextView = (TextView) findViewById(R.id.resultTextView);

        final AIConfiguration config = cloudService.service();

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        requestAudioPermissions();
        //aiService.startListening();\

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }



    //Create placeholder for user's consent to record_audio permission.
//This will be used in handling callback
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
       listenButtonOnClick(listenButton);
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    listenButtonOnClick(listenButton);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //Handling result

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onResult(final AIResponse response) {

        Result result = response.getResult();
        String intentId="";
        String intentName="";
        // Show results in TextView.
        final Metadata metadata = result.getMetadata();
        if (metadata != null) {
         intentId=metadata.getIntentId();
         intentName= metadata.getIntentName();
        }
        final String speech = result.getFulfillment().getSpeech();



        conversationWidget.setHumanSpeech(result.getResolvedQuery());


        conversationWidget.setVoicySpeech(result.getFulfillment().getSpeech());
       // final String dt=result.getFulfillment().getDisplayText();

        //resultTextView.setText("Query :"+result.getResolvedQuery()+" Reply Speech :" +speech);
        int speechStatus = textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
        synchronized (aiService) {


            try {

                aiService.wait(2500);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                }
                try {
                    mCameraId = mCameraManager.getCameraIdList()[0];
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

                if (intentName.equalsIgnoreCase("lightON")) {

                    //ToDo something

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mCameraManager.setTorchMode(mCameraId, true);

                    }


                } else if (intentName.equalsIgnoreCase("lightOFF")) {

                    //ToDo something

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mCameraManager.setTorchMode(mCameraId, false);

                    }


                } else if (intentName.equalsIgnoreCase("youtube")) {

                    Intent launchYouTube = getPackageManager().getLaunchIntentForPackage("com.google.android." + intentName);
                    startActivity(launchYouTube);
                }



            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Exception flashLight()",
                        Toast.LENGTH_SHORT).show();
            }
            if (intentName.equalsIgnoreCase("ending")) {

                aiService.stopListening();
            }else
            {
                aiService.startListening();
            }

        }
    }
    @Override
    public void onError(final AIError error) {
        synchronized (aiService) {
            String errorMessage = error.toString();
            if (errorMessage.contains("SpeechTimeout")) {
                this.onDestroy();

            }
            /*else{
                conversationWidget.setHumanSpeech("No proper input .Give me proper input please");

                int speechStatus = textToSpeech.speak("No proper input .Give me proper input please", TextToSpeech.QUEUE_FLUSH, null);

                if (speechStatus == TextToSpeech.ERROR) {
                    Log.e("TTS", "Error in converting Text to Speech!");
                }            }*/

            /*try {
                aiService.wait(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }/*listenButtonOnClick(listenButton);*/
    }
    @Override
    public void onListeningStarted() {
aiService.resume();
    }

    @Override
    public void onListeningCanceled() {aiService.resume();}

    @Override
    public void onListeningFinished() {aiService.resume();}

    @Override
    public void onAudioLevel(final float level) {}



    @Override
    public void setTitle(CharSequence charSequence) {

    }

    @Override
    public void setTitle(int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
