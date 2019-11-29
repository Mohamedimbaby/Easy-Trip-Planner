package com.example.easytripplanner.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.easytripplanner.Model.userModel;
import com.example.easytripplanner.R;
import com.example.easytripplanner.DTO.user;
import com.example.easytripplanner.ViewModel.UserVM;

import com.example.easytripplanner.databinding.ActivityMainBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    UserVM userVM;
    ProgressDialog progressDialog;

    userModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        String email = getIntent().getExtras().getString("email");
        if(email!=null)
        {
            binding.usernameET.setText(email);
        }
        userVM=UserVM.getInstance();
        Backendless.initApp(this, "FF93F3AF-7561-D86E-FF0D-42803F557C00", "8EDBF2A9-135B-4A3A-9710-CEFB2541D9B9");

        userModel = userVM.getUserModel();

    }

    public void signUp(View view) {
        Intent in = new Intent(this, RegisterAct.class);
        startActivity(in);
        finish();
    }

    public void LogIn(View view) {
        userModel.getLogined().observe(this, new Observer<String>() {

            @Override
            public void onChanged(String o) {
                if (o == null) {
                    Toast.makeText(MainActivity.this, "Login successfully ..", Toast.LENGTH_SHORT).show();
                    HomeActivity.editor.putString("login", Backendless.UserService.CurrentUser().getUserId());
                    HomeActivity.editor.putString("name", (String) Backendless.UserService.CurrentUser().getProperty("name"));
                    HomeActivity.editor.commit();
                    //------ get user id ;
                    Intent in = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Login failed ..", Toast.LENGTH_SHORT).show();
                    binding.usernameET.setError("must contains @ and . Symbols ");

                    if (o.equals("3044")){Toast.makeText(MainActivity.this,   "Multiple login limit for the same user account has been reached. ", Toast.LENGTH_SHORT).show(); }
                    else if(o.equals("3003")){Toast.makeText(MainActivity.this,   "Invalid login or password ", Toast.LENGTH_SHORT).show(); }

                }
                progressDialog.cancel();
                userModel.setLogined(new MutableLiveData<>());
            }
        });
        String username = binding.usernameET.getText().toString();
        String password = binding.passwordET.getText().toString();

        if(username.equals("")||password.equals(""))
        {
          if(username.equals(""))
              binding.usernameET.setError(" the field is empty ");

        if ( password.equals(""))
        binding.passwordET.setError(" the field is empty ");
            Toast.makeText(this, "Please fill all the fields ..", Toast.LENGTH_SHORT).show();
        }
        else if(password.trim().length()<8)
        {
            binding.passwordET.setError(" at least 8 characters ");

        }
        else{
            userVM.Login(username,password);
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Login..");
            progressDialog.setIcon(R.drawable.login);
            progressDialog.show();

        }
    }


    public void LoginWithGoogle(View view) {
        BackendlessUser user = new BackendlessUser();
        Backendless.UserService.loginWithGooglePlusSdk("644815912333-bgspsiu417gjcqdu70gqvp1709473fkj.apps.googleusercontent.com", new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(MainActivity.this, "Successful ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.i("ERROR", fault.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    CallbackManager callbackManager;

    public void loginWithFacebook(View view) {
        callbackManager = CallbackManager.Factory.create();

        ((LoginButton) view).setReadPermissions(Arrays.asList("email", "public_profile","password"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getUserData(loginResult.getAccessToken());
                        System.out.println("permission Granted : "+ loginResult.getRecentlyGrantedPermissions().toString());
                        Toast.makeText(MainActivity.this, user.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.i("tag", exception.toString());
                    }
                });

    }

    user user;

    void getUserData(AccessToken accessToken) {
        System.out.println("access "+ accessToken);
        user = new user();
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {


                    user.setEmail(object.getString("email"));
                    System.out.println("object . get String "+ object.getString("email"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
        System.out.println(user.getEmail());
    }

}
