package shiva.com.maptest.db;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import shiva.com.maptest.R;

public class ListDataActivityMain extends AppCompatActivity {

    ListDataBaseHelper controller = new ListDataBaseHelper(this);
    Button add, view, update, delete;
    EditText placeid, place, country;
    TextView infotext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_db_main);

        placeid = (EditText) findViewById(R.id.edplaceid);
        place = (EditText) findViewById(R.id.edplace);
        country = (EditText) findViewById(R.id.edcountry);

        add = (Button) findViewById(R.id.btnadd);
        update = (Button) findViewById(R.id.btnupdate);
        delete = (Button) findViewById(R.id.btndelete);
        view = (Button) findViewById(R.id.btnview);

        infotext = (TextView) findViewById(R.id.txtresulttext);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListDataActivityMain.this, ListDataActivity.class);
                startActivity(i);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (place.getText().toString().trim().equals("") || country.getText().toString().trim().equals("")) {
                        infotext.setText("Please insert place name and country..");
                    } else {
                        controller = new ListDataBaseHelper(getApplicationContext());
                        SQLiteDatabase db = controller.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("place", place.getText().toString());
                        cv.put("country", country.getText().toString());
                        db.insert("places", null, cv);
                        db.close();
                        infotext.setText("Place added Successfully");
                    }
                } catch (Exception ex) {

                    infotext.setText(ex.getMessage().toString());
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if ((place.getText().toString().trim().equals("") && country.getText().toString().trim().equals("")) || placeid.getText().toString().trim().equals("")) {
                        infotext.setText("Please insert values to update..");
                    } else {
                        controller = new ListDataBaseHelper(getApplicationContext());
                        SQLiteDatabase db = controller.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("place", place.getText().toString());
                        cv.put("country", country.getText().toString());
                        db.update("places", cv, "id=" + placeid.getText().toString(), null);
                        Toast.makeText(ListDataActivityMain.this,
                                "Updated successfully", Toast.LENGTH_SHORT)
                                .show();
                        infotext.setText("Updated Successfully");
                    }
                } catch (Exception ex) {
                    infotext.setText(ex.getMessage().toString());
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (placeid.getText().toString().trim().equals("")) {
                        infotext.setText("Please insert place ID to delete..");
                    } else {
                        controller = new ListDataBaseHelper(getApplicationContext());
                        SQLiteDatabase db = controller.getWritableDatabase();
                        db.delete("places", "id=" + placeid.getText().toString(), null);
                        Toast.makeText(ListDataActivityMain.this,
                                "deleted successfully", Toast.LENGTH_SHORT)
                                .show();
                        infotext.setText("Deleted Successfully");
                    }
                } catch (Exception ex) {
                    infotext.setText(ex.getMessage().toString());
                }
            }
        });
    }

}
