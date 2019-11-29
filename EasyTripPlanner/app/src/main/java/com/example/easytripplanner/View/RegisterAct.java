package com.example.easytripplanner.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.servercode.annotation.BackendlessEvent;
import com.example.easytripplanner.Model.userModel;
import com.example.easytripplanner.R;
import com.example.easytripplanner.ViewModel.UserVM;
import com.example.easytripplanner.databinding.ActivityRegisterBinding;
import com.example.easytripplanner.DTO.user;

public class RegisterAct extends AppCompatActivity {
    UserVM userVM;
    userModel userModel;

    ProgressDialog progressDialog;
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        userVM = UserVM.getInstance();
        userModel = userVM.getUserModel();

    }

    public void register(View view) {
        userModel.getRegistered().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String user) {
                progressDialog.cancel();
                if (user == null) {
                    Toast.makeText(RegisterAct.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(RegisterAct.this, MainActivity.class);
                    in.putExtra("email", binding.emailET.getText().toString());
                    startActivity(in);
                    finish();
                } else {
                    if (user.equals("3041")) {
                        Toast.makeText(RegisterAct.this, "A value for a required property is missing ", Toast.LENGTH_SHORT).show();
                    } else if (user.equals("3033")) {
                        Toast.makeText(RegisterAct.this, "User with the same identity already exists ", Toast.LENGTH_SHORT).show();
                    } else if (user.equals("3040")) {
                        Toast.makeText(RegisterAct.this, "Provided email has wrong format.", Toast.LENGTH_SHORT).show();
                    }
                    binding.emailET.setError("must contains @ and . Symbols ");

                }
            }
        });

        String email = binding.emailET.getText().toString();
        String password = binding.passwordET.getText().toString();
        String name = binding.nameET.getText().toString();
        user user = new user();
        if (email.equals("") || password.equals("") || name.equals("")) {
            if (email.equals(""))
                binding.emailET.setError(" the field is empty ");
             if (password.equals(""))
                binding.passwordET.setError(" the field is empty ");
             if (name.equals(""))
                binding.nameET.setError(" the field is empty ");

            Toast.makeText(this, "please fill al the fields ..", Toast.LENGTH_SHORT).show();

        } else if (password.trim().length() < 8) {
            binding.passwordET.setError("al least 8 characters");
        } else {
            user.setEmail(email);
            user.setName(name);
            user.setPassword(password);
            userVM.registerUser(user);
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Registration ..");
            progressDialog.setIcon(R.drawable.login);
            progressDialog.show();


        }
    }
}
