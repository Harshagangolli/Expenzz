package com.example.test;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemAdapter.ViewHolder> {
    Context context;
    ArrayList<itemModel> arrItem;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String uid = mAuth.getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");


    public RecyclerItemAdapter(Context context, ArrayList<itemModel> arrItem) {
        this.context = context;
        this.arrItem = arrItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.itemName.setText(arrItem.get(position).itemName);
        holder.itemPrice.setText(arrItem.get(position).itemPrice);
        holder.date.setText(arrItem.get(position).date);
        if (!arrItem.get(position).description.equals("")) {
            holder.description.setText("(" + arrItem.get(holder.getAdapterPosition()).description + ")");
        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_expenditure_dialog);

                EditText itemName = dialog.findViewById(R.id.itemType);
                EditText itemPrice = dialog.findViewById(R.id.itemAmount);
                EditText date = dialog.findViewById(R.id.date);
                EditText description = dialog.findViewById(R.id.itemDescription);
                Button update = dialog.findViewById(R.id.btn_addExpenditure);
                update.setText("Update");

                itemName.setText(arrItem.get(holder.getAdapterPosition()).itemName);
                itemPrice.setText(arrItem.get(holder.getAdapterPosition()).itemPrice);
                date.setText(arrItem.get(holder.getAdapterPosition()).date);
                description.setText(arrItem.get(holder.getAdapterPosition()).description);


                update.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View view) {

                        String name = itemName.getText().toString();
                        String price = itemPrice.getText().toString();
                        String payDate = date.getText().toString();
                        String des = description.getText().toString();

                        if (!name.equals("") && !price.equals("") && !payDate.equals("")) {
                            int n = holder.getAdapterPosition();
                            itemModel updatedData = new itemModel(name, price, payDate, des);
                            arrItem.set(holder.getAdapterPosition(), updatedData);
//                            myRef.child(uid).child("exp" + n).setValue(updatedData);
//                            notifyItemChanged(holder.getAdapterPosition());


                            int i = 0;
                            for (itemModel model :
                                    arrItem) {
                                myRef.child(uid).child("exp" + i).setValue(model);
                                i++;
                            }

//                            notifyItemRangeChanged(0,arrItem.size()-1);
                            notifyDataSetChanged();


                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Delete!")
                        .setMessage("Sure?")
                        .setIcon(R.drawable.baseline_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int n = holder.getAdapterPosition();
                                arrItem.remove(holder.getAdapterPosition());
                                myRef.child(uid).child("exp" + n).removeValue();
//                                notifyItemRemoved(holder.getAdapterPosition());




                                int j = 0;
                                for (itemModel model :
                                        arrItem) {
                                    myRef.child(uid).child("exp" + j).setValue(model);
                                    j++;
                                }
                                if (!arrItem.isEmpty()) {
                                    myRef.child(uid).child("exp" + arrItem.size()).removeValue();
                                }
                                notifyDataSetChanged();










                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrItem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, date, description;
        ImageButton edit, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.name);
            itemPrice = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);
            description = itemView.findViewById(R.id.description);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);


        }

    }
}
