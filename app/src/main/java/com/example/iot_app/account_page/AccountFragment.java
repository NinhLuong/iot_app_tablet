package com.example.iot_app.account_page;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.gms.fitness.data.Field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.iot_app.R;
import com.example.iot_app.sign_in.LoginActivity;
import com.example.iot_app.sign_up.SignUpActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AccountFragment extends Fragment {

    private String username;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

//        /*Button btnLogout = view.findViewById(R.id.btnLogout);
        Button btnChangePW = view.findViewById(R.id.btnChangePass);
//        Button btnFollow = view.findViewById(R.id.btnfollow);
        TextView getUsername = view.findViewById(R.id.getUsername);
        TextView titleUsername = view.findViewById(R.id.titleUsername);
        TextView getEnail = view.findViewById(R.id.getEnail);
        TextView getPhone = view.findViewById(R.id.getPhone);

//        DatabaseReference emailref = FirebaseDatabase.getInstance().getReference("users").child(username).child("email");
//        DatabaseReference numberref = FirebaseDatabase.getInstance().getReference("users").child(username).child("phoneNumber");

        getUsername.setText("Grandmother");
        titleUsername.setText("Grandmother");
        getEnail.setText("grandmoother@gmail.com");
        getPhone.setText("0826767891");


        /*emailref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the data from the snapshot
                String emailfb = dataSnapshot.getValue(String.class);

                if(emailfb != null){
                    getEnail.setText(emailfb);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        /*numberref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the data from the snapshot
                String numberfb = dataSnapshot.getValue(String.class);

                if(numberfb != null){
                    getPhone.setText(numberfb);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        // Add an action listener to the button
/*
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("btnFollow", "Button Follow clicked");
                    // Execute the shell command
                    Process process = Runtime.getRuntime().exec("sh data/dat_ninh/script.sh");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String shellRes = bufferedReader.lines().collect(Collectors.joining(","));
                    }
                    // Wait for the command to finish
                    process.waitFor();

                    // Check the exit value
                    if (process.exitValue() == 0) {
                        // The command was successful
                        Log.d("Shell Command", "Command executed successfully");
                    } else {
                        // The command failed
                        Log.e("Shell Command", "Command execution failed");
                    }
                } catch (InterruptedException | IOException e) {
                    // Something went wrong
                    Log.e("Shell Command", "Command execution failed", e);
                }
            }
        });
        */
       /* btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Exit the app with a normal exit code
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });


        btnChangePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Exit the app with a normal exit code
                Intent intent = new Intent(getActivity(), ChangePW.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });*/

        return view;
    }
}