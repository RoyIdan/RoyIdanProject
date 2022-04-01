
package com.example.royidanproject.DatabaseFolder;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.royidanproject.CartActivity;

@Dao
public interface UsersDao {
    @Query("SELECT * FROM tblUsers")
    List<Users> getAll();

    @Query("SELECT * FROM tblUsers ORDER BY userName, userSurname")
    List<Users> getAll_sorted();

    @Query("SELECT * FROM tblUsers WHERE userId = :userId")
    Users getUserById(long userId);

    @Query("SELECT * FROM tblUsers WHERE userEmail = :userEmail")
    Users getUserByEmail(String userEmail);

    @Query("SELECT * FROM tblUser" +
            "s WHERE userEmail = :userEmail AND userPassword = :userPassword")
    Users getUserByLogin(String userEmail, String userPassword);

    @Query("SELECT userId FROM tblUsers WHERE userId = :id AND userPassword = :password")
    long validatePasswordById(long id, String password);

    @Delete
    void deleteUserByReference(Users user);

    @Query("SELECT * FROM tblUsers WHERE userName LIKE '%' || :input || '%' or userSurname like '%' || :input || '%'")
    List<Users> searchByNameOrSurname(String input);



    @Update
    void update(Users user);

    @Insert
    long insert(Users user);
}
