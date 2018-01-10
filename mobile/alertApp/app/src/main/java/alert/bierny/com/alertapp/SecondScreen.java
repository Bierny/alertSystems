package alert.bierny.com.alertapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by damian.biernat on 09.01.2018.
 */

public class SecondScreen extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
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

    public void back(View view) {
    }

    public void yes(View view) {
         Intent fromIntent = getIntent();
        Incident data = (Incident) fromIntent.getSerializableExtra("data");

        data.setIsNotifierVictim(Boolean.TRUE);
        Intent toIntent = new Intent(this, ThirdScreen.class);
        toIntent.putExtra("data", data);
        final int result = 1;
        startActivityForResult(toIntent, result);
    }

    public void no(View view) {
        Intent fromIntent = getIntent();
        Incident data = (Incident) fromIntent.getSerializableExtra("data");

        data.setIsNotifierVictim(Boolean.FALSE);
        Intent toIntent = new Intent(this, ThirdScreen.class);
        toIntent.putExtra("data", data);
        final int result = 1;
        startActivityForResult(toIntent, result);
    }
}
