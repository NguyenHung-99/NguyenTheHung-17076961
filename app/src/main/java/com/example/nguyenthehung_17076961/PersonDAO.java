package com.example.nguyenthehung_17076961;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PersonDAO {
    @Insert(onConflict = REPLACE)
    void insertPerson(Person p);

    @Delete
    void delete(Person p);

    @Delete
    void delAllPerson(List<Person> personList);

    @Query("Select * from person Where name = :name")
    List<Person> getByName(String name);

    //Updata query
    @Query("UPDATE person SET name = :name , tuoi = :tuoi , gioiTinh = :gioiTinh WHERE id = :id")
    void update(int id, String name, int tuoi, String gioiTinh);

    @Query("Select * from person")
    List<Person> getAll();
}
