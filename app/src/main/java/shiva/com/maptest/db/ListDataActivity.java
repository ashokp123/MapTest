package shiva.com.maptest.db;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import shiva.com.maptest.R;

public class ListDataActivity extends AppCompatActivity {
    ListDataBaseHelper controller = new ListDataBaseHelper(this);
    ListView ls;
    TextView infotext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_db_main_one);
        ls = (ListView) findViewById(R.id.placeslist);
        infotext = (TextView) findViewById(R.id.txtresulttext);

        try {
            List<HashMap<String, String>> data = controller.getAllPlace();
            if (data.size() != 0) {
                // Srno, RMCode, Fileno, Loc, FileDesc, TAGNos
                SimpleAdapter adapter = new SimpleAdapter(
                        ListDataActivity.this, data, R.layout.list_db_main_two,
                        new String[]{"id", "place", "country"}, new int[]{
                        R.id.txtplaceid, R.id.txtplacename,
                        R.id.txtcountry});
                ls.setAdapter(adapter);
                String length = String.valueOf(data.size());
                infotext.setText(length + " places");
            } else {
                infotext.setText("No data in database");
            }
        } catch (Exception ex) {
            infotext.setText(ex.getMessage().toString());
        }
    }
}
