package com.example.financetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set total on launch
        refresh(findViewById(0));



    }

    public void refresh(View view){
        FileInteract file = new FileInteract(this);
        String data = file.read();
        System.out.print(data);
        float total = getTotal(data);
        TextView total_display = findViewById(R.id.totalDisplay);
        String displayText = "£ "+formatValueDisplay(Float.toString(total));
        total_display.setText(displayText);
    }


    public void enter(View view) {
        //Check whether adding or taking away funds
        int multiplier;
        RadioGroup group = findViewById(R.id.radioButtonsGroup);
        try {
            multiplier = getMultiplier(group);
        }catch(Exception e){
            Toast.makeText(this, "Please select Add or Spent", Toast.LENGTH_SHORT).show();
            return;
        }
        //Get Value input
        EditText amount = findViewById(R.id.amountEntry);
        float value = 0;
        if (!amount.getText().toString().equals("")) {
            value = Float.parseFloat(amount.getText().toString());
        }

        //Get Reason
        EditText reason_entry = findViewById(R.id.reasonEntry);
        String reason = reason_entry.getText().toString();

        //Check Fields
        if ((value == 0) || (reason.length() == 0)){
            Toast.makeText(this, "Please fill in all inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        //Adding to File
        String content = formatValueDisplay(Float.toString(multiplier*value)) + "\u200E" + reason + "\n";
        FileInteract file = new FileInteract(this);
        String data = file.read();
        if (data != null){
            data = data + content;
        }
        else{
            data = content;
        }
        file.write(data);

        float total = getTotal(data);
        TextView total_display = findViewById(R.id.totalDisplay);
        String displayText = "£ "+formatValueDisplay(Float.toString(total));
        total_display.setText(displayText);
        amount.setText("");
        reason_entry.setText("");
        RadioButton spent = findViewById(R.id.radioSpent);
        spent.setChecked(true);
    }

    private int getMultiplier(RadioGroup group) {
        int checkedId = group.getCheckedRadioButtonId();
        RadioButton checked = findViewById(checkedId);
        int multiplier;
        if (checked.getText().toString().equals("Add")) {
            multiplier = 1;
        } else {
            multiplier = -1;
        }

        return multiplier;
    }



    private float getTotal(String data){
        if (data.length() == 0){
            return 0;
        }
        FileInteract file = new FileInteract(this);
        String[][] formatted_data = file.parseData(data);
        float total = 0;
        for(int i = 0; i<formatted_data.length; i++){
            total += Float.parseFloat(formatted_data[i][0]);
        }
        return total;
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
            //String __value = _value[0] + "." + _value[1];
            return _value[0] + "." + _value[1];
        }
        return value;
    }

    public void openSettings(View view){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
