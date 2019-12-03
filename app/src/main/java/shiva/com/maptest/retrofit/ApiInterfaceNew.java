package shiva.com.maptest.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import shiva.com.maptest.apiresponse.Login;

public interface ApiInterfaceNew {

    @FormUrlEncoded
    @POST("marketing_user_login.php?")
    Call<Login> loginn(@Field("email") String username, @Field("password") String password);

    //@GET("/maps/api/place/nearbysearch/json?location=17.4505,78.3809&radius=10000&types=restaurant&key=AIzaSyAhZQInvzvpf__o3zK67tbJ9j7eZ1d7kys")
    //Call<Example> getPlaces();

    /*@FormUrlEncoded
    @POST("customer/signup")
    Call<SignupResponse> signUp(@Field("first_name") String username, @Field("password") String password, @Field("email") String email, @Field("mobile") String mobile);*/

    /*https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=17.4505,78.3809&radius=10000&types=restaurant&key=AIzaSyAhZQInvzvpf__o3zK67tbJ9j7eZ1d7kys*/

}

