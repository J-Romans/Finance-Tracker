package com.example.financetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder>{
    private ArrayList<transactionInformation> transactionInfo;
    private final RecyclerViewInterface recyclerViewInterface;

    public recyclerAdapter(ArrayList<transactionInformation> transactionInfo, RecyclerViewInterface recyclerViewInterface) {
        this.transactionInfo = transactionInfo;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView value;
        private TextView reason;
        private ImageButton delete_button;

        public MyViewHolder(final View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);
            value = view.findViewById(R.id.Money);
            reason = view.findViewById(R.id.ReasonView);
            delete_button = view.findViewById(R.id.deleteButton);

            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.areYouSureDeleteDialog(position);
                        }
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new recyclerAdapter.MyViewHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        //Gather info from file
        String _value = transactionInfo.get(position).getValue();
        String _reason = transactionInfo.get(position).getReason();

        //Change String
        holder.value.setText("£ " + formatValueDisplay(_value));
        holder.reason.setText(_reason);
    }
    public String formatValueDisplay(String value){
        if (value.contains(".")){
            value = value.replace(".", "!");
            String[] _value = value.split("!");
            if (_value[1].length()>2){
                _value[1] = _value[1].substring(0,2);
            }else if(_value[1].length() == 1) {
                _value[1] = _value[1] + "0";
            }else if (_value[1].length() == 0){
                _value[1] = _value[1] + "00";
            }
            String __value = _value[0] + "." + _value[1];
            return __value;
        }
        return value;
    }

    @Override
    public int getItemCount() {
        return transactionInfo.size();
    }
}