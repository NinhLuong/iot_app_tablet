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
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, container, false);

        rcvPerson = rootView.findViewById(R.id.rcv_person);
        FloatingActionButton btnAdPerson = rootView.findViewById(R.id.btnAddPerson);

        // Initialize the PersonAdapter
        if (rcvPerson != null) {
            // Set the layout manager
            rcvPerson.setLayoutManager(new LinearLayoutManager(getContext()));
// Initialize the PersonAdapter with the listener
            personAdapter = new PersonAdapter(new ArrayList<>(), this);
            rcvPerson.setAdapter(personAdapter);
        }
        btnAdPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.add_person_layout);

                EditText edtNamePerson = dialog.findViewById(R.id.edtNamePerson);
                EditText edtPhoneNum = dialog.findViewById(R.id.edtPhoneNum);
                EditText edtSkypeId = dialog.findViewById(R.id.edtSkypeId);

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
                        String skypeId = edtSkypeId.getText().toString();

                        if (name.isEmpty() || phoneNum.isEmpty()) {
                            Toast.makeText(getContext(), "Please enter all information", Toast.LENGTH_SHORT).show();
                        } else {
                            Person person = new Person(name, phoneNum, stringUri, skypeId);
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
        personAdapter = new PersonAdapter(listPerson, this);
        rcvPerson.setAdapter(personAdapter);

        return rootView;
    }
    public void savePersons() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("persons", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listPerson);
        editor.putString("personList", json);
        editor.apply();
        Log.d("WebFragment", "List after saving: " + listPerson.toString());
    }

    private void loadPersons() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("persons", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("personList", null);
        Type type = new TypeToken<ArrayList<Person>>() {}.getType();
        listPerson = gson.fromJson(json, type);


        if (listPerson == null) {
           listPerson = new ArrayList<>();
            /*listPerson.add(new Person("Lê Phan Nguyên Đạt", "0943205123", R.drawable.det, "live:.cid.f3b242efa0f1ec30"));
            listPerson.add(new Person("Trần Thị Ngọc Trâm", "0943205123", R.drawable.tran_tram_cut, "live:.cid.1d3e048641a89a6b"));
            listPerson.add(new Person("Nguyễn Ngọc Vân Châu", "0943205123", R.drawable.van_chau, "live:.cid.255c8733b13201a"));*/
        } else {
            for (Person person : listPerson) {
                String imageUri = person.getImageUri();
                Log.d("imageUri", imageUri);
                if (imageUri != null && !imageUri.equals("Image Default")) {
                    // Set the image URI for this Person
                    person.setImageUri(imageUri);
                }
            }
        }
        Log.d("WebFragment", "List after loading: " + listPerson.toString());
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
                        stringUri = copyFileToInternalStorage(uri, imgName);
                        txtNameImg.setText(imgName);
                    } else {
                        txtNameImg.setText("Image Default");
                    }
                }
            });

    private String copyFileToInternalStorage(Uri uri, String newFileName) {
        Uri returnUri = uri;

        Cursor returnCursor = getContext().getContentResolver().query(returnUri, new String[]{
                OpenableColumns.DISPLAY_NAME,
                OpenableColumns.SIZE
        }, null, null, null);

        /*
         * Get the column indexes of the data in the Cursor,
         * move to the first row in the Cursor, get the data,
         * and display it.
         */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = (newFileName != null && !newFileName.isEmpty()) ? newFileName : returnCursor.getString(nameIndex);
        File output;
        if (!newFileName.isEmpty()) {
            output = new File(getContext().getFilesDir() + "/" + newFileName);
        } else {
            output = new File(getContext().getFilesDir() + "/" + name);
        }

        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(output);
            int read = 0;
            int bufferSize = 1024;
            final byte[] buffers = new byte[bufferSize];

            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }

            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        return output.getPath();
    }

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