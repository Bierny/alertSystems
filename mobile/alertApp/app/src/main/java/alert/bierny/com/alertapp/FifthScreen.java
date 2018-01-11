package alert.bierny.com.alertapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by damian.biernat on 09.01.2018.
 */

public class FifthScreen extends Activity {
    String str ="";
    Spinner spinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fifth_layout);
        spinner = findViewById(R.id.edit_text_id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FifthScreen.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.kind));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected,this));
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

    public void next(View view) {
        Intent fromIntent = getIntent();
        Incident data = (Incident) fromIntent.getSerializableExtra("data");
        if(spinner.getSelectedItem() !=null){
            String str = String.valueOf(spinner.getSelectedItem().toString());
            data.setKind(str);
        }
        Intent toIntent = new Intent(this, SixthScreen.class);
        toIntent.putExtra("data", data);
        final int result = 1;
        startActivityForResult(toIntent, result);

    }
}
