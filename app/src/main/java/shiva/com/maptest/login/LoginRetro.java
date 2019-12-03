package shiva.com.maptest.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shiva.com.maptest.R;
import shiva.com.maptest.apiresponse.Login;
import shiva.com.maptest.retrofit.ApiInterfaceNew;
import shiva.com.maptest.retrofit.RetrofitApiClientNew;

public class LoginRetro extends AppCompatActivity {

    Button sgin_ctnue_btnn,get_rstrnts_btnn;
    EditText sgin_emal_edtxt,sgin_pswd_edtxt;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrofit_login);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        init();
        //validations();
    }

    public void init(){

        sgin_emal_edtxt = (EditText)findViewById(R.id.sgin_emal_edtxt);
        sgin_pswd_edtxt = (EditText)findViewById(R.id.sgin_pswd_edtxt);

        sgin_ctnue_btnn = (Button)findViewById(R.id.sgin_ctnue_btnn);
        get_rstrnts_btnn = (Button)findViewById(R.id.get_rstrnts_btnn);

        sgin_ctnue_btnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()){
                    sgin_emal_edtxt.setText("marketing@gmail.com");
                    sgin_pswd_edtxt.setText("123");
                    String email = sgin_emal_edtxt.getText().toString().trim();
                    String pwd = sgin_pswd_edtxt.getText().toString().trim();
                    Login(email,pwd);
                }

            }
        });

    }

    public void Login(String email,String pwd) {

        final ApiInterfaceNew service = RetrofitApiClientNew.getClient().create(ApiInterfaceNew.class);

        Call<Login> call = service.loginn(email,pwd);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                Login mLoginObject = response.body();

                Integer status = mLoginObject.getStatus();
                String message = mLoginObject.getMessage();

                Toast.makeText(LoginRetro.this, "Returned " + status, Toast.LENGTH_LONG).show();

                if(status.equals(200)){
                    //redirect to Main Activity page
                    //Intent loginIntent = new Intent(LoginRetro.this, LoginSucess.class);
                    //loginIntent.putExtra("EMAIL", email);
                    //startActivity(loginIntent);
                    Toast.makeText(LoginRetro.this, "Returned " + message, Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                Toast.makeText(LoginRetro.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
        // startActivity(new Intent(getApplicationContext(), Home.class));
    }
}
