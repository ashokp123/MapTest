package shiva.com.maptest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import shiva.com.maptest.db.DataBase;
import shiva.com.maptest.db.ListDataActivityMain;
import shiva.com.maptest.db.SQLite;
import shiva.com.maptest.login.LoginRetro;
import shiva.com.maptest.maps.MainActivity;

public class FirstScreen extends AppCompatActivity{

   Button getMapp,getDB,getDB_list,getLogin,getIPPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.firstscreen);

    init();

  }

  public void init(){

        getMapp = (Button)findViewById(R.id.getMapp);
        getDB = (Button)findViewById(R.id.getDB);
        getDB_list = (Button)findViewById(R.id.getDB_list);
        getLogin = (Button)findViewById(R.id.getLogin);
      getIPPB = (Button)findViewById(R.id.getIPPB);

      getMapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreen.this, MainActivity.class));
            }
        });

      getDB.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //startActivity(new Intent(FirstScreen.this, SQLite.class));
              startActivity(new Intent(FirstScreen.this, DataBase.class));
          }
      });

      getDB_list.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(FirstScreen.this, ListDataActivityMain.class));
          }
      });

      getLogin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(FirstScreen.this, LoginRetro.class));
          }
      });

  }


}
