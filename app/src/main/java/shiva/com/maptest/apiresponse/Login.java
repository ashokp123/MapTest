package shiva.com.maptest.apiresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

@SerializedName("status")
@Expose
private Integer status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("user_id")
@Expose
private Integer userId;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public Integer getUserId() {
return userId;
}

public void setUserId(Integer userId) {
this.userId = userId;
}

}