
package com.example.royidanproject.DatabaseFolder;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UsersDao {
    @Query("SELECT * FROM tblUsers")
    List<Users> getAll();

    @Query("SELECT * FROM tblUsers WHERE userId = :userId")
    Users getUserById(long userId);

    @Query("SELECT * FROM tblUser" +
            "s WHERE userEmail = :userEmail AND userPassword = :userPassword")
    Users getUserByLogin(String userEmail, String userPassword);

    @Delete
    void deleteUserByReference(Users user);

    @Query("SELECT * FROM tblUsers WHERE userName LIKE '%' || :input || '%' or userSurname like '%' || :input || '%'")
    List<Users> searchByNameOrSurname(String input);

    @Update
    void update(Users user);

    @Insert
    long insert(Users user);
}
