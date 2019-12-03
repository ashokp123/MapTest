package shiva.com.maptest.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClientNew {

    public static final String BASE_URL = "http://demos.customerwebdemo.com/mobipug/app/";

    private static Retrofit retrofit = null;
    private static Retrofit retrofit_1 = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}