package com.teamjimroberts.riskytraveler;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.common.io.CharStreams;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    private SeekBar riskBar;
    private ImageButton megaButton;
    private Drawable[] drawables = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        riskBar = (SeekBar)findViewById(R.id.riskBar);
        megaButton = (ImageButton)findViewById(R.id.megaButton);
        drawables = new Drawable[]{getResources().getDrawable(R.drawable.button_off_120),
                getResources().getDrawable(R.drawable.button_on_120)};

        //
        //magic done by advisers


        class findLocation extends AsyncTask<Void,Void,JSONObject> {
            String jimString;
            public String getJimString(){
                return jimString;
            }

            @Override
            protected void onPostExecute( JSONObject data ) {
                //Log.e( TAG, data.toString() );
                jimString = "this is from PostExecute";
            }


            @Override
            protected JSONObject doInBackground(Void... params) {
                try {
                    InputStream open = getAssets().open("RiskDEN-1.json");
                    JSONObject jimsonObject = new JSONObject( CharStreams.toString( new InputStreamReader( open )) );
                    //test code on JSON objects
                    JSONObject obj = jimsonObject.getJSONObject("CntKey");
                    return obj;
                } catch ( Exception e ) {
                    Log.e( TAG, e.toString() );
                }
                return null;
            }


        };
        //end magic
        //now the data is in the main stream as a JSON Object
        //JSON Object manipulation
        //execute code when button is pressed
        megaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressButton(megaButton);
                nextPage();


                findLocation yourLocation = new findLocation();
                yourLocation.execute();
                TextView jimText = (TextView)findViewById(R.id.testText);
                jimText.setText(yourLocation.jimString);


            }
        });

        //alter search parameters when slider is moved
        riskBar.setProgress(1);
        riskBar.setMax(5);

    }


    public boolean releaseButton(ImageButton megaButton){
        megaButton.setImageDrawable(drawables[1]);
        return true; //return button clicked (set to 1)
    }

    public void pressButton(ImageButton megaButton){
        megaButton.setImageDrawable(drawables[0]);
    }
    public void nextPage(){
        Intent i = new Intent(this, Destination.class);
        startActivity(i);
    }
    public interface AsyncResponse{
        void processFinish(JSONObject output);
    }

}


/*
//magic done by advisers
new AsyncTask<Void,Void,JSONObject>(){

@Override
protected void onPostExecute( JSONObject data ) {
        Log.e( TAG, data.toString() );
        }

@Override
protected JSONObject doInBackground(Void... params) {
        try {
        InputStream open = getAssets().open("RiskDEN-1.json");
        return new JSONObject( CharStreams.toString( new InputStreamReader( open )) );
        } catch ( Exception e ) {
        Log.e( TAG, e.toString() );
        }
        return null;
        }
        }.execute();
//end magic
*/