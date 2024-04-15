package com.example.iot_app.webview_page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_app.R;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{

    public List<Person> mListPerson;

    public PersonAdapter(List<Person> mListPerson) {
        this.mListPerson = mListPerson;
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
        Uri imageUri = Uri.parse(person.getImageUri());
        holder.imageAvatar.setImageURI(imageUri);
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
        return 0;
    }

    public void addPerson(Person person) {
        mListPerson.add(person);
        notifyItemInserted(mListPerson.size() - 1);
    }


    public class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private TextView txt_phone_number;
        private ImageView imageAvatar;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_phone_number = itemView.findViewById(R.id.txt_phone_number);
            imageAvatar = itemView.findViewById(R.id.img_person);
        }
    }
}
