package com.example.royidanproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.royidanproject.DatabaseFolder.AppDatabase;
import com.example.royidanproject.DatabaseFolder.Users;
import com.example.royidanproject.Utility.CommonMethods;
import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.Validator;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;
import static com.example.royidanproject.MainActivity.ADMIN_PHONE;
import static com.example.royidanproject.MainActivity.SP_NAME;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etSurname, etBirthdate, etEmail, etAddress, etPassword, etPasswordValidation, etPhone;
    private Spinner spiCity, spiPhone;
    private ImageView ivImage;
    private ImageButton ibCamera, ibGallery;
    private RadioGroup rgGender;
    private Button btnReturn, btnRegister, btnLogin;
    private Bitmap bmUser;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private AppDatabase db;

    private void setViewPointers() {
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etBirthdate = findViewById(R.id.etBirthdate);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        spiCity = findViewById(R.id.spiCity);
        etPassword = findViewById(R.id.etPassword);
        etPasswordValidation = findViewById(R.id.etPasswordValidation);
        etPhone = findViewById(R.id.etPhone);
        spiPhone = findViewById(R.id.spi_Phone);
        ivImage = findViewById(R.id.ivImage);
        ibCamera = findViewById(R.id.ibCamera);
        ibGallery = findViewById(R.id.ibGallery);
        rgGender = findViewById(R.id.rgGender);
        btnReturn = findViewById(R.id.btnReturn);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setViewPointers();
        sp = getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();
        db = AppDatabase.getInstance(RegisterActivity.this);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.createLoginDialog(RegisterActivity.this);
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("user")) {
            updateMode();
        }
        else {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rgGender.getCheckedRadioButtonId() == -1) {
                        ((TextView) findViewById(R.id.tvGenderError)).setError("Please select gender.");
                        return;
                    }

                    String email = etEmail.getText().toString().trim();
                    String phone = spiPhone.getSelectedItem().toString() + etPhone.getText().toString().trim();
                    String name = etName.getText().toString().trim();
                    String surname = etSurname.getText().toString().trim();
                    String gender = ((RadioButton) findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
                    String strBirthdate = etBirthdate.getText().toString().trim();
                    String address = etAddress.getText().toString().trim();
                    String city = spiCity.getSelectedItem().toString();
                    String password = etPassword.getText().toString().trim();
                    String passwordValidation = etPasswordValidation.getText().toString().trim();

                    if (!validateForm(name, surname, strBirthdate, email, address, city, password, passwordValidation, phone)) {
                        return;
                    }

                    if (!checkEmailAndPhone(email, phone)) {
                        return;
                    }

                    Date date;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        date = sdf.parse(strBirthdate);
                    } catch (ParseException e) {
                        toast("invalid date format");
                        return;
                    }

                    String photo = CommonMethods.savePhoto(bmUser);
                    if (photo == null) {
                        toast("Failed to save the photo");
                        return;
                    }

                    Users user = new Users();
                    user.setUserName(name);
                    user.setUserSurname(surname);
                    user.setUserGender(gender);
                    user.setUserBirthdate(date);
                    user.setUserEmail(email);
                    user.setUserAddress(address);
                    user.setUserCity(city);
                    user.setUserPassword(password);
                    user.setUserPhone(phone);
                    user.setUserPhoto(photo);
                    long id = db.usersDao().insert(user);

                    editor.putString("name", name + " " + surname);
                    editor.putString("image", photo);
                    editor.putLong("id", id);
                    editor.putBoolean("admin", phone.equals(ADMIN_PHONE));
                    editor.commit();

                    bmUser = null;
                    toast("Successfully registered");

                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            });
        }

        ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission(RegisterActivity.this)) {
                    getPermission(RegisterActivity.this);
                }
                else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
            }
        });

        ibGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission(RegisterActivity.this)) {
                    getPermission(RegisterActivity.this);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);
                }
            }
        });

        etBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDateDialog();
            }
        });
    }

    private void updateMode() {
        ((TextView)findViewById(R.id.tvTitle)).setText("Update form");
        btnRegister.setText("Update");
        btnLogin.setVisibility(View.GONE);

        Users user = (Users) getIntent().getExtras().getSerializable("user");

        etEmail.setText(user.getUserEmail());
        String phone = user.getUserPhone();
        etName.setText(user.getUserName());
        etSurname.setText(user.getUserSurname());
        String gender = user.getUserGender();
        etBirthdate.setText(new SimpleDateFormat("dd/MM/yyyy").format(user.getUserBirthdate()));
        etAddress.setText(user.getUserAddress());
        String city = user.getUserCity();
        etPassword.setText(user.getUserPassword());
        rgGender.check(gender.equals("male") ? R.id.rdMale : R.id.rdFemale);
        String photo = user.getUserPhoto();

        spiCity.setSelection(Arrays.asList(getResources().getStringArray(R.array.spinner_city)).indexOf(city));
        spiPhone.setSelection(Integer.parseInt(String.valueOf(phone.charAt(2))));
        etPhone.setText(phone.substring(3));
        bmUser = CommonMethods.getImage(photo);
        ivImage.setImageBitmap(bmUser);

        Bitmap originPhoto = bmUser;

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String phone = spiPhone.getSelectedItem().toString() + etPhone.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String surname = etSurname.getText().toString().trim();
                String gender = ((RadioButton)findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
                String strBirthdate = etBirthdate.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String city = spiCity.getSelectedItem().toString();
                String password = etPassword.getText().toString().trim();
                String passwordValidation = etPasswordValidation.getText().toString().trim();

                if (!validateForm(name, surname, strBirthdate, email, address, city, password, passwordValidation, phone)) {
                    return;
                }

                if (!checkEmailAndPhone(email, phone)) {
                    return;
                }

                Date date;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date = sdf.parse(strBirthdate);
                } catch (ParseException e) {
                    toast("invalid date format");
                    return;
                }

                if (!(bmUser == originPhoto)) {
                    String photo = CommonMethods.savePhoto(bmUser);
                    if (photo == null) {
                        toast("Failed to save the photo");
                        return;
                    }
                    CommonMethods.deletePhoto(user.getUserPhoto());
                    user.setUserPhoto(photo);
                }

                user.setUserName(name);
                user.setUserSurname(surname);
                user.setUserGender(gender);
                user.setUserBirthdate(date);
                user.setUserEmail(email);
                user.setUserAddress(address);
                user.setUserCity(city);
                user.setUserPassword(password);
                user.setUserPhone(phone);
                db.usersDao().update(user);

                // Shared Preferences
                if (sp.getLong("id", 0l) == user.getUserId()) {
                    editor.putString("name", name + " " + surname);
                    editor.putString("image", user.getUserPhoto());
                    editor.commit();
                }

                toast("Successfully updated");
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }

            private boolean checkEmailAndPhone(String email, String phone) {
                List<Users> users = db.usersDao().getAll();

                for (Users u : users) {
                    if (u.getUserId() != user.getUserId() && email.equals(u.getUserEmail())) {
                        toast("email is already in use.");
                        return false;
                    }
                }

                for (Users u : users) {
                    if (u.getUserId() != user.getUserId() && phone.equals(u.getUserPhone())) {
                        toast("phone is already in use.");
                        return false;
                    }
                }

                return true;
            }
        });
    }

    private boolean checkEmailAndPhone(String email, String phone) {
        List<Users> users = db.usersDao().getAll();

        for (Users u : users) {
            if (email.equals(u.getUserEmail())) {
                toast("email is already in use.");
                return false;
            }
            if (phone.equals(u.getUserPhone())) {
                toast("phone is already in use.");
                return false;
            }
        }

        return true;
    }

    private boolean validateForm(String name, String surname, String date, String email, String address, String city, String password, String password2, String phone) {
        String error = Validator.validateEmptyFields(name, surname, date, email, address, city, password, password2, phone);
        if (!error.isEmpty()) {
            toast(error);
            return false;
        }

        boolean valid = true;

        error = "";
        error = Validator.validateName(name);
        if (!error.isEmpty()) {
            etName.setError(error);
            valid = false;
        }

        error = "";
        error = Validator.validateSurname(surname);
        if (!error.isEmpty()) {
            etSurname.setError(error);
            valid = false;
        }

        error = "";
        error = Validator.validateBirthdate(date);
        if (!error.isEmpty()) {
            etBirthdate.setError(error);
            valid = false;
        }

        error = "";
        error = Validator.validateEmail(email);
        if (!error.isEmpty()) {
            etEmail.setError(error);
            valid = false;
        }

        error = "";
        error = Validator.validateCity(city);
        if (!error.isEmpty()) {
            TextView errorText = (TextView)spiCity.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Please select city.");
            valid = false;
        }

        error = "";
        error = Validator.validatePassword(password);
        if (!error.isEmpty()) {
            etPassword.setError(error);
            valid = false;
        }

        error = "";
        error = Validator.validatePasswordValidation(password, password2);
        if (!error.isEmpty()) {
            etPasswordValidation.setError(error);
            valid = false;
        }

        error = "";
        error = Validator.validatePhone(phone);
        if (!error.isEmpty()) {
            etPhone.setError(error);
            valid = false;
        }

        if (!valid) {
            return false;
        }

        if (bmUser == null) {
            toast("Please select an image.");
            return false;
        }
        return true;
    }

    private void getPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                CAMERA,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
        }, 1);
    }

    private boolean checkPermission(Context context) {
        int cam = ContextCompat.checkSelfPermission(context, CAMERA);
        int write = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);

        return cam == PERMISSION_GRANTED &&
                write == PERMISSION_GRANTED &&
                read == PERMISSION_GRANTED;
    }

    private void buildDateDialog() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();

        if(etBirthdate.getText().toString().trim().length() != 0)
        {
            try
            {
                cal.setTime(sdf.parse(etBirthdate.getText().toString()));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog picker = new DatePickerDialog(RegisterActivity.this, new setDate(), year, month, day);

        Calendar maxCalender = Calendar.getInstance();

        maxCalender.set(Calendar.YEAR, maxCalender.get(Calendar.YEAR));

        picker.getDatePicker().setMaxDate(maxCalender.getTimeInMillis());

        picker.show();
    }

    private class setDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day)
        {
            month++;

            String str = day + "/" + month + "/" + year;

            etBirthdate.setText(str);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                bmUser = (Bitmap) data.getExtras().get("data");
                ivImage.setImageBitmap(bmUser);
            }
        }

        else if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    bmUser = selectedImage;
                    ivImage.setImageBitmap(bmUser);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void toast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}