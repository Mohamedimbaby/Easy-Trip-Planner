package com.example.easytripplanner.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.easytripplanner.Model.userModel;
import com.example.easytripplanner.DTO.user;

public class UserVM extends ViewModel {
  private static   userModel userModel ;
  private static  UserVM userVM ;
  public static UserVM getInstance (){
      if(userVM==null)
          userVM= new UserVM();
      return userVM;
  }



public  userModel getUserModel(){
 if(userModel==null)
 {
     userModel= com.example.easytripplanner.Model.userModel.getInstance();
 }
 return userModel;

}
    private UserVM() {
    }
     public void registerUser(user user)
     {
         userModel.registerUser(user);
     }

    public void Login(String username, String password) {
    userModel.loginUser(username,password);
    }
}
