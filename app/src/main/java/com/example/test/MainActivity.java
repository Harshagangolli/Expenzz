package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();
        assert uid != null;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        Objects.requireNonNull(getSupportActionBar()).setTitle("Hello,");

        ArrayList<itemModel> arrItems = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerItemAdapter adapter = new RecyclerItemAdapter(this, arrItems);

        Query query = myRef.child(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, HashMap<String, String>> userdata = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                    int i=0;
                    for (DataSnapshot userSnap: snapshot.getChildren()) {
                        itemModel data = new itemModel(userdata.get("exp"+i).get("itemName"), userdata.get("exp"+i).get("itemPrice"), userdata.get("exp"+i).get("date"), userdata.get("exp"+i).get("description"));
                        arrItems.add(data);
                        i++;
                    }
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);
        FloatingActionButton addNewDialog = findViewById(R.id.addNewDialog);


        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_expenditure_dialog);


        addNewDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText itemName_1 = dialog.findViewById(R.id.itemType);
                itemName_1.requestFocus();
                EditText itemPrice_1 = dialog.findViewById(R.id.itemAmount);
                EditText date_1 = dialog.findViewById(R.id.date);
                EditText description_1 = dialog.findViewById(R.id.itemDescription);
                Button addExp_1 = dialog.findViewById(R.id.btn_addExpenditure);

                itemPrice_1.setText("");
                description_1.setText("");
                itemName_1.setText("");
                LocalDate today = LocalDate.now();
                String formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                date_1.setText(formattedDate);


                addExp_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = itemName_1.getText().toString();
                        String price = itemPrice_1.getText().toString();
                        String payDate = date_1.getText().toString();
                        String des = description_1.getText().toString();

                        if (!name.equals("") && !price.equals("") && !payDate.equals("")) {
                            itemModel add = new itemModel(name, price, payDate, des);
                            arrItems.add(add);
                            myRef.child(uid).child("exp" + (arrItems.size()-1)).setValue(add);
                            recyclerView.setAdapter(adapter);
                            dialog.dismiss();

                        }
                    }
                });
                dialog.show();

            }
        });
        Button logout = findViewById(R.id.logout);
        Intent iTemp = new Intent(this, LoginActivity.class);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(iTemp);
                finish();
            }
        });
        final boolean[] flag = {true};


        TextView exp = findViewById(R.id.exp);
        exp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (flag[0]) {
                    setSum(arrItems);
                } else {
                    exp.setText(R.string.your_expenditure);
                }
                flag[0] = !flag[0];
            }
        });
    }
    //Expenditure summing and setting method in main activity
    public void setSum(ArrayList<itemModel> arrItems){
        TextView exp = findViewById(R.id.exp);
        int sum = 0;
        if (arrItems.size() != 0) {
            for (int i = 0; i < arrItems.size(); i++) {
                sum = sum + Integer.parseInt(arrItems.get(i).getItemPrice());
            }
            exp.setText("Total Expenditure : " + String.valueOf(sum));


    }}

}
