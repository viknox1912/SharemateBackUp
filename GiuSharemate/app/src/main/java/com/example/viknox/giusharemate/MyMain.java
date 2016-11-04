package com.example.viknox.giusharemate;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MyMain extends AppCompatActivity {

    //Strings for profile
    Boolean isLogedIn;
    String user_id;
    String user_name;
    String email;
    String profile_pic;
    int     age;
    char    gender;
    String[] tags;
    //Ui
    Button bt_search,bt_post;
    //Facebook variables
    GraphRequest request;
    AccessToken mAccessToken;
    LoginButton bt_login;
    Profile user_profile;
    String[] permissions = { "public_profile", "user_friends", "email", "user_birthday" };
    CallbackManager mCallback;
    FacebookCallback<LoginResult> fCallback  = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            mAccessToken=loginResult.getAccessToken();
            user_profile = Profile.getCurrentProfile();
            user_id = user_profile.getId();
            Toast.makeText(MyMain.this, "Welcome" + user_profile.getName().toString(), Toast.LENGTH_SHORT).show();

            request= GraphRequest.newMeRequest(mAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.v("FBPROFILE", response.toString());
                    try
                    {
                        user_name = object.getString("name");
                        email = object.getString("email");
                        String tGender = object.getString("gender");
                        gender = tGender.charAt(1);
                        String dob = object.getString("birthday");
                        age = 2016 - Integer.parseInt(dob.substring(dob.length()-4));
                        JSONObject PicURLObj = object.getJSONObject("picture");
                        JSONObject data = PicURLObj.getJSONObject("data");
                        profile_pic = data.getString("uri");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("MyMain", user_name + email + age + gender + profile_pic);
                   /* Intent i = new Intent(getApplicationContext(),ActivityOverview.class);
                    i.putExtra("name",user_name);
                    i.putExtra("email",email);
                    i.putExtra("gender",gender);
                    i.putExtra("age", age);
                    i.putExtra("userID", user_id);*/

                    //startActivity(i);
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday,picture");
            request.setParameters(parameters);
            request.executeAsync();




        }

        @Override
        public void onCancel() {
            Toast.makeText(MyMain.this, "Interrupted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(MyMain.this, "Failed" + error.toString(), Toast.LENGTH_SHORT).show();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_my_main);
        mCallback = CallbackManager.Factory.create();

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                Log.v("MyMain","Accesstoken tracker Started");


            }
        };

        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile profile) {
                Log.v("MyMain","Profile tracker Started");
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        initUI();
        Log.v("MyMain","Activity Initiated");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(this, "SettingsPRessed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.overview:
                Intent i = new Intent(getApplicationContext(),ActivityOverview.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return true;
    }
    public void CreateUserPost(){
        RequestQueue MyQueue = Volley.newRequestQueue(this);


        String url = "https://162.208.10.214/smbackend/webapi/user/";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.v("Server",response);
                //USER ID
                user_id = response;
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.v("Server", error.toString());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                //NOT NULL FIELDS!
                MyData.put("facebookAuthToken","facebooktoko21812e");
                MyData.put("username","Wlaker Boh");
                MyData.put("email","Cougline@hotmail.com");
                MyData.put("gender","m");
                MyData.put("age","25");
                MyData.put("pictureurl","https://www.facebook.com/photo.php?fbid=885665898137372&set=a.145390462164923.19867.100000819115677&type=3&source=11");
                MyData.put("tags","elf,wishsong,shannara,elfstones");
                //MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        MyQueue.add(MyStringRequest);
    }
    public void SearchUsersGET(){
        // Needs user_id
        RequestQueue MyQueue = Volley.newRequestQueue(this);

        String url = "http://162.208.10.214/smbackend/webapi/users/" + "04e95ce5c0ff22564404647ba339ecbf" + "/"+"emo";
        // url + userID + tags from advanced search + name from edit text
        StringRequest searchUserRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Parse from json into list view
                Log.v("Server",response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> SearchData = new HashMap<String, String>();
                return  SearchData;
            }
        };
        MyQueue.add(searchUserRequest);
    }

    private void initUI() {
        bt_search = (Button)findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                SearchUsersGET();
                    Log.v("Server","Searching User");
                }catch (Exception e){
                    Log.v("Server", e.toString());

                }
            }
        });
        bt_post = (Button)findViewById(R.id.bt_post);
        bt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CreateUserPost();
                    Log.v("Server","Post trying");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v("Server","Post failed");
                }
            }
        });

        bt_login = (LoginButton)findViewById(R.id.bt_login);
        bt_login.setReadPermissions(permissions);

        bt_login.registerCallback(mCallback,fCallback);
        Log.v("UI","UI initiated successfully");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallback.onActivityResult(requestCode, resultCode, data);
    }
}
