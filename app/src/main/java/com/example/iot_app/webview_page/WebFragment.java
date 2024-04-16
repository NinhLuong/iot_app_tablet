package com.example.iot_app.webview_page;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_app.device.FanFragment;
import com.example.iot_app.R;
import com.example.iot_app.device.AirFragment;
import com.example.iot_app.device.LampFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WebFragment extends Fragment {
//    private WebView myWebView;
    private RecyclerView rcvPerson;
    private PersonAdapter personAdapter;
    private List<Person> listPerson;
    private TextView txtNameImg;
    public String imgName;
    public String stringUri;

    public void initiateSkypeUri(Context myContext, String mySkypeUri) {

        // Make sure the Skype for Android client is installed.
        if (!isSkypeClientInstalled(myContext)) {
            goToMarket(myContext);
            return;
        }

        // Create the Intent from our Skype URI.
        Uri skypeUri = Uri.parse(mySkypeUri);
        Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

        // Restrict the Intent to being handled by the Skype for Android client only.
        myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Initiate the Intent. It should never fail because you've already established the
        // presence of its handler (although there is an extremely minute window where this could fail,
        // but you don't need to worry about that here).
        myContext.startActivity(myIntent);

        return;
    }

    public void goToMarket(Context myContext) {
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (myIntent.resolveActivity(myContext.getPackageManager()) != null) {
            myContext.startActivity(myIntent);
        }
        return;
    }

    public boolean isSkypeClientInstalled(Context myContext) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try {
            myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
        }
        catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, container, false);

        rcvPerson = rootView.findViewById(R.id.rcv_person);
        FloatingActionButton btnAdPerson = rootView.findViewById(R.id.btnAddPerson);

        // Initialize the PersonAdapter
        if (rcvPerson != null) {
            // Set the layout manager
            rcvPerson.setLayoutManager(new LinearLayoutManager(getContext()));
            // Initialize the PersonAdapter
            personAdapter = new PersonAdapter(new ArrayList<>());
            rcvPerson.setAdapter(personAdapter);
        }
        btnAdPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.add_person_layout);

                EditText edtNamePerson = dialog.findViewById(R.id.edtNamePerson);
                EditText edtPhoneNum = dialog.findViewById(R.id.edtPhoneNum);
                Button btnChooseImage = dialog.findViewById(R.id.btn_choose_img);
                txtNameImg = dialog.findViewById(R.id.txt_name_img);

                btnChooseImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGetContent.launch("image/*");
                    }
                });

                Button btnAddInfor = dialog.findViewById(R.id.btnAddInfor);
                btnAddInfor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edtNamePerson.getText().toString();
                        String phoneNum = edtPhoneNum.getText().toString();

                        if (name.isEmpty() || phoneNum.isEmpty()) {
                            Toast.makeText(getContext(), "Please enter all information", Toast.LENGTH_SHORT).show();
                        } else {
                            Person person = new Person(name, phoneNum, stringUri);
                            personAdapter.addPerson(person);
                            savePersons();
                            personAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });

        // Load the list of persons from SharedPreferences
        loadPersons();

        // Initialize the PersonAdapter with the loaded list of persons
        personAdapter = new PersonAdapter(listPerson);
        rcvPerson.setAdapter(personAdapter);

        return rootView;
    }
    private void savePersons() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("persons", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listPerson);
        editor.putString("personList", json);
        editor.apply();
    }

    private void loadPersons() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("persons", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("personList", null);
        Type type = new TypeToken<ArrayList<Person>>() {}.getType();
        listPerson = gson.fromJson(json, type);

        if (listPerson == null) {
            listPerson = new ArrayList<>();
        }
    }

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    /*String imageName;
                    if (uri != null) {
                        imageName = getImageName(uri);
                    } else {
                        imageName = "Image Default";
                    }
                    txtNameImg.setText(imageName);*/

                    // Handle the returned Uri
                    if (uri != null) {
                        imgName = getImageName(uri);
                        stringUri = uri.toString();
                        txtNameImg.setText(imgName);
                    } else {
                        txtNameImg.setText("Image Default");
                    }
                }
            });

    private String getImageName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex);
                    } else {
                        // Handle the case where the DISPLAY_NAME column doesn't exist
                        // For example, set a default value or throw an exception
                    }
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}