package com.example.financetracker;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileInteract {
    private Context mContext;
    private String filename = "Finances.txt";
    public FileInteract(Context mContext){
        this.mContext = mContext;
    }


    public String read() {
        try {
            File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(storageDir, filename);

            FileInputStream fis = new FileInputStream(file);
            byte[] content = new byte[(int) file.length()];
            fis.read(content);
            fis.close();
            return new String(content);

        } catch (FileNotFoundException e){
            write("");
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void write(String content){
        FileOutputStream fos;
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(storageDir,filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        try{
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String[][] parseData(String data){
        String[] _data = data.split("\n");
        String[][] __data = new String[_data.length][2];
        for (int i = 0; i < _data.length; i++){
            __data[i] = _data[i].split("\u200E");
        }
        return __data;
    }

    public void Delete(){
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(storageDir,filename);
        file.delete();
    }

}
