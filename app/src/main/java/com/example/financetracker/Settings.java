package com.example.financetracker;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Settings extends AppCompatActivity implements RecyclerViewInterface{
    private ArrayList<transactionInformation> transactionInfo;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        recyclerView = findViewById(R.id.transactionRecycler);
        transactionInfo = new ArrayList<>();

        try {
            setTransactionInfo();
            setAdapter();
        }catch (Exception e){
            TextView transaction_view = findViewById(R.id.TransactionLabel);
            transaction_view.setText("No Transactions Recorded");
        }

    }

    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(transactionInfo, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setTransactionInfo(){
        String[][] info;
        FileInteract file = new FileInteract(this);
        info = file.parseData(file.read());

        for (int i=0; i < info.length; i++){
            transactionInfo.add(new transactionInformation(info[i][0], info[i][1]));
        }
    }

    public void clearAll(View view){
        FileInteract file = new FileInteract(this);
        file.write(null);
    }

    public void returnToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void areYouSureDeleteAllDialog(View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup, null);

        dialogBuilder.setView(popupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        Button no_button = popupView.findViewById(R.id.noButton);
        Button yes_button = popupView.findViewById(R.id.yesButton);

        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAll(popupView);
                dialog.dismiss();
                returnToMain();
            }
        });
    }


    public void deleteItem(int position) {
        FileInteract file = new FileInteract(this);
        String[] data = file.read().split("\n");
        String new_data = "";
        for (int i=0; data.length>i; i++){
            if (i != position){
                new_data += data[i] + "\n";
            }
        }

        file.write(new_data);
        transactionInfo.remove(position);

        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void areYouSureDeleteDialog(int position){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup, null);

        dialogBuilder.setView(popupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        Button no_button = popupView.findViewById(R.id.noButton);
        Button yes_button = popupView.findViewById(R.id.yesButton);

        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(position);
                dialog.dismiss();
            }
        });
    }
}