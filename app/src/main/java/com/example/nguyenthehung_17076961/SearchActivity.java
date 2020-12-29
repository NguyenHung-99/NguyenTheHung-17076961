package com.example.nguyenthehung_17076961;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    EditText txtSearch;
    Button btnSearch, btnGetAll, btnHome;
    RecyclerView recyclerView;
    List<Person> personList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;

    MainAdapter adapter;
    RoomConfigDB database;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        txtSearch = findViewById(R.id.search_name);
        btnSearch = findViewById(R.id.bt_search);
        btnGetAll = findViewById(R.id.btn_getAll);
        btnHome = findViewById(R.id.btn_goHome);
        recyclerView = findViewById(R.id.recycler_view);

        database = RoomConfigDB.getInstance(this);
        personList = database.personDAO().getAll();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MainAdapter(personList,this);

        recyclerView.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtxt = txtSearch.getText().toString().trim();

                if(searchtxt.equals("")){
                    Toast.makeText(SearchActivity.this, "Vui Lòng nhập tên cần search", Toast.LENGTH_LONG).show();
                }else {
                    if(database.personDAO().getByName(searchtxt).size() == 0){
                        Toast.makeText(SearchActivity.this, "Không tìm thấy", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(SearchActivity.this, "Tìm thấy " + database.personDAO().getByName(searchtxt).size() + " person", Toast.LENGTH_LONG).show();
                        personList.clear();
                        personList.addAll(database.personDAO().getByName(searchtxt));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchActivity.this, MainActivity.class);
                SearchActivity.this.startActivity(myIntent);
            }
        });
        btnGetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personList.clear();
                personList.addAll(database.personDAO().getAll());
                adapter.notifyDataSetChanged();
            }
        });
    }
}
