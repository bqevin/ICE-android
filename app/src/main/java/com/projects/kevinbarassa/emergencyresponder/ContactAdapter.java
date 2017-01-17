package com.projects.kevinbarassa.emergencyresponder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Kevin Barassa on 17-Jan-17.
 */

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{

    private List<ContactItem> contacts;
    private Context context;


    public ContactAdapter(List<ContactItem> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ContactViewHolder holder, int position) {

        //Initiate the articles
        final ContactItem contact = contacts.get(position);

        ////inflate the created viewholders with actual data from model
        holder.ice_name.setText(contact.getIce_name());
        holder.ice_residence.setText("Lives in "+contact.getIce_residence());

        //Load image with Piccasso
//        Picasso.with(context)
//                .load(contact.getIce_avatar())
//                .into(holder.image);


        //Add click listener to contact items
        holder.contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Name
                String name  = contact.getIce_name();
                //Email
                String email  = contact.getIce_email();
                //Phone
                String phone  = contact.getIce_phone();
                //Residence
                String residence  = contact.getIce_residence();


                //Soft transfer
                Intent intent = new Intent(context, ContactDetailed.class);
                intent.putExtra("EXTRA_NAME", name);
                intent.putExtra("EXTRA_PHONE", phone);
                intent.putExtra("EXTRA_EMAIL", email);
                intent.putExtra("EXTRA_RESIDENCE", residence);


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

                 //Toast.makeText(context, contact.getIce_name(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Map items number
    @Override
    public int getItemCount() {
        return contacts.size();
    }


    //Viewholder class
    class ContactViewHolder extends RecyclerView.ViewHolder{
        TextView ice_name;
        TextView ice_residence;
        RelativeLayout contactLayout;

        public ContactViewHolder(View itemView) {
            super(itemView);

            ice_name = (TextView) itemView.findViewById(R.id.ice_view_name);
            ice_residence = (TextView) itemView.findViewById(R.id.ice_view_residence);
            contactLayout = (RelativeLayout) itemView.findViewById(R.id.contactLayout);

        }
    }
}
