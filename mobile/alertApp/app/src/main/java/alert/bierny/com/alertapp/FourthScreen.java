package alert.bierny.com.alertapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

/**
 * Created by damian.biernat on 09.01.2018.
 */

public class FourthScreen extends Activity {
    String str ="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fourth_layout);
    }

    public void onSend(View view) {
        Intent fromIntent = getIntent();
        Incident data = (Incident) fromIntent.getSerializableExtra("data");

        EditText text = (EditText) findViewById(R.id.fourth);
        String str = String.valueOf(text.getText());
        data.setHowManyVictims(Integer.valueOf(str));
        Intent toIntent = new Intent(this, FifthScreen.class);
        toIntent.putExtra("data", data);
        final int result = 1;
        startActivityForResult(toIntent, result);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Incident fromData = (Incident) data.getSerializableExtra("data");

        Intent resultIntent = new Intent();
        resultIntent.putExtra("data", fromData);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
