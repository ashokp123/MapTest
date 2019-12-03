package shiva.com.maptest.maps;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import shiva.com.maptest.R;

public class MainActivity extends AppCompatActivity {

    TextView getMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

public void init(){

    getMap = (TextView)findViewById(R.id.getMap);
    fragmentcalling(new Main_Screen());

    getMap.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });

}

    public void fragmentcalling(android.app.Fragment fragment) {
        //fragment = new FeedBack();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }

}
