package shiva.com.maptest.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import shiva.com.maptest.R;

public class SQLite extends Activity {

    EditText et1,et2,et3,et4;

    SQLiteDatabase dBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sqlite);

        et1=(EditText)findViewById(R.id.editText1);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText3);
        et4=(EditText)findViewById(R.id.editText4);

        dBase=openOrCreateDatabase("employee_db", Context.MODE_PRIVATE, null);

        dBase.execSQL("create table if not exists employee(eid number,ename varchar(40),desig varchar(40),dept varchar(40))");

    }

    public void insert(View v){

        ContentValues values=new ContentValues();

        values.put("eid",Integer.parseInt(et1.getText().toString()));
        values.put("ename",et2.getText().toString());
        values.put("desig",et3.getText().toString());
        values.put("dept",et4.getText().toString());

        dBase.insert("employee", null, values);

        Toast.makeText(getApplicationContext(), "Data inserted successfully...",2000).show();

        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");





    }

    public void read(View v){

    			/*Cursor c=dBase.rawQuery("select *from employee where eid=?", new String[]{et1.getText().toString()});

    			while(c.moveToNext()){

    			//	c.getInt(0);
    				et2.setText(c.getString(1));
    				et3.setText(c.getString(2));
    				et4.setText(c.getString(3));

    			}
    	*/

        Cursor c=dBase.rawQuery("select *from employee",null);

        while(c.moveToNext()){

            Toast.makeText(getApplicationContext(), "Eid :"+c.getInt(0)+"\n Ename :"+c.getString(1)+"\n desig: "+c.getString(2)+"\n dept :"+c.getString(3), 2000).show();

        }

    }

    public void update(View v){

        ContentValues cv=new ContentValues();
        cv.put("ename", et2.getText().toString());
        cv.put("desig", et3.getText().toString());
        cv.put("dept", et4.getText().toString());

        dBase.update("employee", cv, "eid=?", new String[]{et1.getText().toString()});

        // update table employee set ename='vijay' , desig='manager',dept='admin' where eid=123

    }

    public void delete(View v){

        dBase.delete("employee", "eid=?",new String[]{et1.getText().toString()});

    }

}
