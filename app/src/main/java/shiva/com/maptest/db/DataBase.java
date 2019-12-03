package shiva.com.maptest.db;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import shiva.com.maptest.R;
import shiva.com.maptest.constants.ArrayLists;
import shiva.com.maptest.constants.Constants;
import shiva.com.maptest.constants.Pojo;

import static shiva.com.maptest.constants.ArrayLists.arrayList;

public class DataBase extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName,editSurname,editMarks ,editTextId;
    Button btnAddData;
    Button btnviewAll;
    Button btnDelete;
    Button btnviewUpdate;
    Button button_getData;

    String id="",name="",email="",address="",gender="",mobile="";

    ArrayList<Pojo> places_list = new ArrayList<Pojo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);
        myDb = new DatabaseHelper(this);

        editName = (EditText)findViewById(R.id.editText_name);
        editSurname = (EditText)findViewById(R.id.editText_surname);
        editMarks = (EditText)findViewById(R.id.editText_Marks);
        editTextId = (EditText)findViewById(R.id.editText_id);


        button_getData = (Button)findViewById(R.id.button_getData);
        btnAddData = (Button)findViewById(R.id.button_add);
        btnviewAll = (Button)findViewById(R.id.button_viewAll);
        btnviewUpdate= (Button)findViewById(R.id.button_update);
        btnDelete= (Button)findViewById(R.id.button_delete);


        btnAddData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddData();
                    }
                });

        btnviewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAll();
            }
        });
        btnviewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteData();
            }
        });


        button_getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute();
            }
        });


    }


    class PlacesTask extends AsyncTask<String, String, String> {

        String msg="";

        @Override
        protected String doInBackground(String... params) {

            try{
                DefaultHttpClient client=new DefaultHttpClient();

                //HttpGet get=new HttpGet("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=17.4505,78.3809&radius=10000&types=restaurant&key=AIzaSyAhZQInvzvpf__o3zK67tbJ9j7eZ1d7kys");

                HttpGet get=new HttpGet("https://api.androidhive.info/contacts/");

                HttpResponse response=client.execute(get);

                InputStream isr = response.getEntity().getContent();

                int i=isr.read();

                while(i!=-1){
                    msg=msg+(char)i;
                    i=isr.read();
                }

            }catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Constants.LogMethod("Response:====",msg+"");

            setOutput(msg);
        }
    }

    public void setOutput(String msg){

        places_list.clear();

        try{

            JSONObject object=new JSONObject(msg);
            JSONArray places=object.getJSONArray("contacts");

            for(int i=0;i<places.length();i++){

                Pojo pojo=new Pojo();

                JSONObject place=places.getJSONObject(i);

                pojo.setName(place.getString("name"));
                pojo.setEmail(place.getString("email"));
                pojo.setId(place.getString("id"));
                pojo.setAddress(place.getString("address"));
                pojo.setGender(place.getString("gender"));

                JSONObject geo_metry=place.getJSONObject("phone");

                //JSONObject location_obj=geo_metry.getJSONObject("location");

                pojo.setMobile(geo_metry.getString("mobile"));

                pojo.setHome(geo_metry.getString("home"));

                places_list.add(pojo);

            }

            Constants.LogMethod("ArraySize:======",places_list.size()+"");

            for (int j = 0; j < places_list.size(); j++) {

                id = places_list.get(j).getId()+"";
                name = places_list.get(j).getName()+"";
                email = places_list.get(j).getEmail()+"";
                address = places_list.get(j).getAddress()+"";
                gender = places_list.get(j).getGender()+"";
                mobile = places_list.get(j).getMobile()+"";

                Toast.makeText(DataBase.this,id+"\n"+name+"\n"+email+"\n"+address+"\n"+gender+"\n"+mobile,1000).show();

            }

            /*ArrayList<Pojo> result = new ArrayList<>();
            for (int i = 0; i < places_list.size(); i++) {
                result.add(i, places_list.get(i));
            }*/

            /*ListView lView=(ListView)findViewById(R.id.listView1);

            lView.setAdapter(new PlacesAdapter(MainActivity.this,places_list));

            lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    Toast.makeText(getApplicationContext(), arg2+" item selected ...",2000).show();

                    ApplicationConstants.lati = places_list.get(arg2).getLati();
                    ApplicationConstants.longi = places_list.get(arg2).getLongi();

                    ApplicationConstants.vicinity = places_list.get(arg2).getVicinity();
                    ApplicationConstants.image = places_list.get(arg2).getImage();

                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));

                }
            });*/

        }catch (Exception e) {
            // TODO: handle exception
        }

    }







    public void DeleteData() {

        Integer deletedRows = myDb.deleteData(editTextId.getText().toString());
        if(deletedRows > 0)
            Toast.makeText(DataBase.this,"Data Deleted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(DataBase.this,"Data not Deleted", Toast.LENGTH_LONG).show();

    }

    public void UpdateData() {

        boolean isUpdate = myDb.updateData(editTextId.getText().toString(),
                editName.getText().toString(),
                editSurname.getText().toString(),editMarks.getText().toString());
        if(isUpdate == true)
            Toast.makeText(DataBase.this,"Data Update",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(DataBase.this,"Data not Updated",Toast.LENGTH_LONG).show();

    }


    public  void AddData() {

        /*boolean isInserted = myDb.insertData(editName.getText().toString(),
                editSurname.getText().toString(),
                editMarks.getText().toString());*/

        boolean isInserted = myDb.insertData(name,email,mobile);

        if(isInserted == true)
            Toast.makeText(DataBase.this,"Data Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(DataBase.this,"Data not Inserted",Toast.LENGTH_LONG).show();

    }

    public void viewAll() {

        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            // show message
            showMessage("Error","Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :"+ res.getString(0)+"\n");
            buffer.append("Name :"+ res.getString(1)+"\n");
            buffer.append("Surname :"+ res.getString(2)+"\n");
            buffer.append("Marks :"+ res.getString(3)+"\n\n");
        }

        // Show all data
        showMessage("Data",buffer.toString());

    }




    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }



}
