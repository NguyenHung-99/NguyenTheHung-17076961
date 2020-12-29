package com.example.nguyenthehung_17076961;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    EditText edt_name, edt_tuoi;
    Button btAdd, btReset, btSearch;
    RecyclerView recyclerView;
    List<Person> personList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;

    MainAdapter adapter;
    RoomConfigDB database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_name = findViewById(R.id.search_name);
        edt_tuoi = findViewById(R.id.edt_tuoi);
        btAdd = findViewById(R.id.bt_search);
        btReset = findViewById(R.id.btn_reset);
        btSearch = findViewById(R.id.btn_getAll);
        recyclerView = findViewById(R.id.recycler_view);

        //set value spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> arrayListGioiTinh = new ArrayList<>();
        arrayListGioiTinh.add("Nam");
        arrayListGioiTinh.add("Nữ");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,arrayListGioiTinh);
        spinner.setAdapter(arrayAdapter);
        //

        database = RoomConfigDB.getInstance(this);

        personList = database.personDAO().getAll();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MainAdapter(personList,this);

        recyclerView.setAdapter(adapter);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get string from edit text
                String sName = edt_name.getText().toString().trim();
                String sGioiTinh = spinner.getSelectedItem().toString();
                // int sTuoi = Integer.parseInt(edt_tuoi.getText().toString().trim());
                Toast.makeText(MainActivity.this, sGioiTinh, Toast.LENGTH_LONG).show();
                if(sName.equals("") || edt_tuoi.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Name or Tuổi incorrect", Toast.LENGTH_LONG).show();
                }else if(Integer.parseInt(edt_tuoi.getText().toString().trim()) <= 0){
                    Toast.makeText(MainActivity.this, "Tuổi Phải lớn hơn 0", Toast.LENGTH_LONG).show();
                }else if(!sName.equals("")){
                    int sTuoi = Integer.parseInt(edt_tuoi.getText().toString().trim());
                    Person data = new Person();
                    data.setName(sName);
                    data.setTuoi(sTuoi);
                    data.setGioiTinh(sGioiTinh);
                    database.personDAO().insertPerson(data);
                    Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_LONG).show();
                    edt_tuoi.setText("");
                    edt_name.setText("");
                    spinner.setSelection(0);
                    //Notify
                    personList.clear();
                    personList.addAll(database.personDAO().getAll());
                    adapter.notifyDataSetChanged();
                }

            }
        });
        //delete all person
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.personDAO().delAllPerson(personList);
                Toast.makeText(MainActivity.this, "Reset List User Thành công", Toast.LENGTH_LONG).show();
                //Notify
                personList.clear();
                personList.addAll(database.personDAO().getAll());
                adapter.notifyDataSetChanged();

            }
        });
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
}