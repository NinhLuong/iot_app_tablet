package com.example.iot_app.webview_page;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_app.R;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{
    private WebFragment webFragment;
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

    public List<Person> mListPerson;

    public PersonAdapter(List<Person> mListPerson, WebFragment webFragment) {
        this.mListPerson = mListPerson;
        this.webFragment = webFragment; // Initialize the WebFragment reference
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = mListPerson.get(position);
        if (person == null) {
            return;
        }
        String imageUri = person.getImageUri();
//        int imageResId = person.getImageResId();
        if (imageUri != null && !imageUri.isEmpty()) {
            holder.imageAvatar.setImageURI(Uri.parse(imageUri));
        } else if(imageUri.equals("Image Default")) {
            holder.imageAvatar.setImageResource(R.drawable.img_ava);
        }
        else {
            holder.imageAvatar.setImageResource(person.getImageResId());
        }

        /*if (imageUri == null || imageUri.equals("Image Default")) {
            holder.imageAvatar.setImageResource(R.drawable.img_ava);
        } else {
            holder.imageAvatar.setImageURI(Uri.parse(imageUri));
        }*/

        holder.txt_name.setText(person.getName());
        holder.txt_phone_number.setText(person.getPhone_number());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int currentPosition = holder.getBindingAdapterPosition();

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this person?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mListPerson.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                                webFragment.savePersons(); // Save the updated list to SharedPreferences
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                return true;
            }
        } );
    }

    @Override
    public int getItemCount() {
        return mListPerson.size();
    }

    public void addPerson(Person person) {
        mListPerson.add(person);
        notifyItemInserted(mListPerson.size() - 1);
    }


    public class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private TextView txt_phone_number;
        private ImageView imageAvatar;
        private Button btnCall;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_phone_number = itemView.findViewById(R.id.txt_phone_number);
            imageAvatar = itemView.findViewById(R.id.img_person);
            btnCall = itemView.findViewById(R.id.btn_call);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Person person = mListPerson.get(position);
                        String skypeId = person.getSkypeId();
                        if (skypeId != null && !skypeId.isEmpty()) {
                            initiateSkypeUri(itemView.getContext(), "skype:" + skypeId + "?call&video=true");
                        } else {
                            Toast.makeText(itemView.getContext(), "No Skype ID available for this person", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
