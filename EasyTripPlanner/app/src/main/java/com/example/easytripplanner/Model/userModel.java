package com.example.easytripplanner.Model;


import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.easytripplanner.DTO.user;
import com.example.easytripplanner.View.MainActivity;

public class userModel {

    user user ;
 private static userModel userModel;
    MutableLiveData <String> Registered ;

    public static userModel getInstance() {
        if (userModel==null)
            userModel=new userModel();
        return userModel;
    }

    public MutableLiveData<String> getLogined() {
        return Logined;
    }

    public void setLogined(MutableLiveData<String> logined) {
        Logined = logined;
    }

    MutableLiveData <String> Logined ;

    public void setRegistered(MutableLiveData<String> registered) {
        Registered = registered;
    }

    public MutableLiveData<String> getRegistered() {
        return Registered;
    }

    private userModel() {
        this.user = new user();
        Registered= new MutableLiveData<>();
        Logined= new MutableLiveData<>();
    }
   public void registerUser(user user )
    {
        BackendlessUser backendlessUser = new BackendlessUser();
        backendlessUser.setEmail(user.getEmail());
        backendlessUser.setProperty("name",user.getName());
        backendlessUser.setPassword(user.getPassword());
        Backendless.UserService.register(backendlessUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {

                Registered.setValue(null);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                System.out.println(fault.getCode() + "   " + fault.getMessage());
                Registered.setValue(fault.getCode());
            }
        })
;
    }
public void loginUser(String username, String password)
{
    Backendless.UserService.login(username, password, new AsyncCallback<BackendlessUser>() {
        @Override
        public void handleResponse(BackendlessUser response) {

            Logined.setValue(null);
        }

        @Override
        public void handleFault(BackendlessFault fault) {
            System.out.println(fault.getCode() + "   " + fault.getMessage());

            Logined.setValue(fault.getCode());
          }
    });
}


}
