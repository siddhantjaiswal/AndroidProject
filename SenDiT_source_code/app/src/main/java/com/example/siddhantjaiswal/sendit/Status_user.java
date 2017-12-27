package com.example.siddhantjaiswal.sendit;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Status_user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_user);
    }
    public void onClick(View view){
        TextView status= (TextView)findViewById(R.id.status);
        TextView textView9 = (TextView) findViewById(R.id.textView9);
       String s= textView9.getText().toString();
        EditText edt = (EditText)findViewById(R.id.edt);
        edt.setText(s);
        status.setText(s);
    }

    public void onClick1(View view){
        TextView status= (TextView)findViewById(R.id.status);
        TextView textView9 = (TextView) findViewById(R.id.textView10);
        String s= textView9.getText().toString();
        EditText edt = (EditText)findViewById(R.id.edt);
        edt.setText(s);
        status.setText(s);
    }
    public void onClick2(View view){
        TextView status= (TextView)findViewById(R.id.status);
        TextView textView9 = (TextView) findViewById(R.id.textView11);
        String s= textView9.getText().toString();
        EditText edt = (EditText)findViewById(R.id.edt);
        edt.setText(s);
        status.setText(s);
    }
    public void onClick3(View view){
        TextView status= (TextView)findViewById(R.id.status);
        TextView textView9 = (TextView) findViewById(R.id.textView12);
        String s= textView9.getText().toString();
        EditText edt = (EditText)findViewById(R.id.edt);
        edt.setText(s);
        status.setText(s);
    }
    public void onClick4(View view){
        TextView status= (TextView)findViewById(R.id.status);
        TextView textView9 = (TextView) findViewById(R.id.textView13);
        String s= textView9.getText().toString();
        EditText edt = (EditText)findViewById(R.id.edt);
        edt.setText(s);
        status.setText(s);
    }
    public void onClick5(View view){
        TextView status= (TextView)findViewById(R.id.status);
        TextView textView9 = (TextView) findViewById(R.id.textView14);
        String s= textView9.getText().toString();
        EditText edt = (EditText)findViewById(R.id.edt);
        edt.setText(s);
        status.setText(s);
    }
    public void onClick6(View view){
        TextView status= (TextView)findViewById(R.id.status);
        TextView textView9 = (TextView) findViewById(R.id.textView15);
        String s= textView9.getText().toString();
        EditText edt = (EditText)findViewById(R.id.edt);
        edt.setText(s);
        status.setText(s);
    }
    public void onClick7(View view){
        TextView status= (TextView)findViewById(R.id.status);
        TextView textView9 = (TextView) findViewById(R.id.textView16);
        String s= textView9.getText().toString();
        EditText edt = (EditText)findViewById(R.id.edt);
        edt.setText(s);
        status.setText(s);
    }
    public void onClick8(View view){
        TextView status= (TextView)findViewById(R.id.status);
        TextView textView9 = (TextView) findViewById(R.id.textView17);
        String s= textView9.getText().toString();
        EditText edt = (EditText)findViewById(R.id.edt);
        edt.setText(s);
        status.setText(s);
    }


    public void save_status(View view) {
        TextView status= (TextView)findViewById(R.id.status);
        EditText edt = (EditText)findViewById(R.id.edt);
        String s= edt.getText().toString();
        if (edt.getText().toString().trim().length()!=0)
        {
            status.setText(s);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        TextView status= (TextView)findViewById(R.id.status);
        String s=status.getText().toString();
        status.setText(s);
    }
}
