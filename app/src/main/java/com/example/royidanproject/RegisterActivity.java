package com.example.royidanproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.royidanproject.Utility.CitiesIsrael;
import com.example.royidanproject.Utility.ToolbarManager;
import com.example.royidanproject.Utility.UserImages;
import com.example.royidanproject.Utility.Dialogs;
import com.example.royidanproject.Utility.Validator;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private ToolbarManager toolbarManager;

    @Override
    protected void onResume() {
        super.onResume();

        toolbarManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        toolbarManager.onDestroy();
    }

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
        setSpinnerData();

        sp = getSharedPreferences(SP_NAME, 0);
        editor = sp.edit();
        db = AppDatabase.getInstance(RegisterActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarManager = new ToolbarManager(RegisterActivity.this, toolbar);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.createLoginDialog(RegisterActivity.this);
            }
        });

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ((TextView) findViewById(R.id.tvGenderError)).setError(null);
            }
        });

        addErrorListeners();

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("user")) {
            updateMode();
        }
        else {
            ((ArrayAdapter) spiCity.getAdapter()).insert("?????? ??????", 0);
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = etEmail.getText().toString().trim();
                    String phone = spiPhone.getSelectedItem().toString() + etPhone.getText().toString().trim();
                    String name = etName.getText().toString().trim();
                    String surname = etSurname.getText().toString().trim();
                    String gender = "";
                    if (rgGender.getCheckedRadioButtonId() != -1) {
                        gender = ((RadioButton) findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
                    }
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

                    String photo = UserImages.savePhoto(bmUser, RegisterActivity.this);
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
                    toast("?????????? ????????????!");

                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
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

    private void addErrorListeners() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = Validator.validateName(s.toString().trim());
                if (!error.isEmpty()) {
                    etName.setError(error);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = Validator.validateSurname(s.toString().trim());
                if (!error.isEmpty()) {
                    etSurname.setError(error);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = Validator.validateEmail(s.toString().trim());
                if (!error.isEmpty()) {
                    etEmail.setError(error);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = Validator.validateAddress(s.toString().trim());
                if (!error.isEmpty()) {
                    etAddress.setError(error);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = Validator.validatePassword(s.toString().trim());
                if (!error.isEmpty()) {
                    etPassword.setError(error);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String error = Validator.validatePhone(spiPhone.getSelectedItem() + s.toString().trim());
                if (!error.isEmpty()) {
                    etPhone.setError(error);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setSpinnerData() {
        Spinner spinner = spiCity;
        List<String> list = CitiesIsrael.GetListCitiesInIsrael();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void updateMode() {
        ((TextView)findViewById(R.id.tvTitle)).setText("???????? ??????????");
        btnRegister.setText("????????");
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
        rgGender.check(gender.equals("??????") ? R.id.rdMale : R.id.rdFemale);
        String photo = user.getUserPhoto();

        spiCity.setSelection(CitiesIsrael.GetListCitiesInIsrael().indexOf(city));
        spiPhone.setSelection(Integer.parseInt(String.valueOf(phone.charAt(2))));
        etPhone.setText(phone.substring(3));
        bmUser = UserImages.getImageBitmap(photo, RegisterActivity.this);
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
                    String photo = UserImages.savePhoto(bmUser, RegisterActivity.this);
                    if (photo == null) {
                        toast("?????????? ???????????? ??????????");
                        return;
                    }
                    UserImages.deletePhoto(user.getUserPhoto(), RegisterActivity.this);
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
                if (sp.getLong("id", 0L) == user.getUserId()) {
                    editor.putString("name", name + " " + surname);
                    editor.putString("image", user.getUserPhoto());
                    editor.commit();
                }

                toast("?????????? ?????????? ????????????");
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }

            private boolean checkEmailAndPhone(String email, String phone) {
                List<Users> users = db.usersDao().getAll_sorted();

                for (Users u : users) {
                    if (u.getUserId() != user.getUserId() && email.equals(u.getUserEmail())) {
                        toast("?????????????? ?????? ???????? ????????????");
                        return false;
                    }
                }

                for (Users u : users) {
                    if (u.getUserId() != user.getUserId() && phone.equals(u.getUserPhone())) {
                        toast("???????????? ?????? ???????? ????????????");
                        return false;
                    }
                }

                return true;
            }
        });
    }

    private boolean checkEmailAndPhone(String email, String phone) {
        List<Users> users = db.usersDao().getAll_sorted();

        for (Users u : users) {
            if (email.equals(u.getUserEmail())) {
                toast("?????????????? ?????? ???????? ????????????");
                return false;
            }
            if (phone.equals(u.getUserPhone())) {
                toast("???????????? ?????? ????????  ????????????");
                return false;
            }
        }

        return true;
    }

    private boolean validateForm(String name, String surname, String date, String email, String address, String city, String password, String password2, String phone) {
//        String error = Validator.validateEmptyFields(name, surname, date, email, address, city, password, password2, phone);
//        if (!error.isEmpty()) {
//            toast(error);
//            return false;
//        }

        if (hasErrors()) {
            return false;
        }

        if (bmUser == null) {
            toast("?????? ??????????");
            return false;
        }

        if (name.isEmpty()) {
            etName.setError("?????? ?????? ?????? ????");
            return false;
        }
        if (surname.isEmpty()) {
            etSurname.setError("?????? ?????? ?????? ????");
            return false;
        }
        if (rgGender.getCheckedRadioButtonId() == -1) {
            ((TextView)findViewById(R.id.tvGenderError)).setError("?????? ?????? ????????");
            return false;
        }
        if (date.isEmpty()) {
            etBirthdate.setError("?????? ?????? ?????? ????");
            return false;
        }
        if (email.isEmpty()) {
            etEmail.setError("?????? ?????? ?????? ????");
            return false;
        }
        if (address.isEmpty()) {
            etAddress.setError("?????? ?????? ?????? ????");
            return false;
        }
        if (spiCity.getSelectedItemPosition() == 0) {
            ((TextView)spiCity.getSelectedView()).setError("?????? ?????? ??????");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("?????? ???? ?????? ????");
            return false;
        }
        if (password2.isEmpty()) {
            etPasswordValidation.setError("?????? ?????? ?????? ????");
            return false;
        }
        if (!Validator.validatePasswordValidation(password, password2).isEmpty()) {
            etPasswordValidation.setError("???????????????? ???? ????????????");
            return false;
        }
        if (phone.length() == 3) {
            etPhone.setError("?????? ?????? ?????? ????");
            return false;
        }
//        error = "";
//        error = Validator.validateName(name);
//        if (!error.isEmpty()) {
//            etName.setError(error);
//            valid = false;
//        }
//
//        error = "";
//        error = Validator.validateSurname(surname);
//        if (!error.isEmpty()) {
//            etSurname.setError(error);
//            valid = false;
//        }
//
//        error = "";
//        error = Validator.validateBirthdate(date);
//        if (!error.isEmpty()) {
//            etBirthdate.setError(error);
//            valid = false;
//        }
//
//        error = "";
//        error = Validator.validateEmail(email);
//        if (!error.isEmpty()) {
//            etEmail.setError(error);
//            valid = false;
//        }
//
//        error = "";
//        error = Validator.validateCity(city);
//        if (!error.isEmpty()) {
//            TextView errorText = (TextView)spiCity.getSelectedView();
//            errorText.setError("");
//            errorText.setTextColor(Color.RED);
//            errorText.setText("Please select city.");
//            valid = false;
//        }
//
//        error = "";
//        error = Validator.validatePassword(password);
//        if (!error.isEmpty()) {
//            etPassword.setError(error);
//            valid = false;
//        }
//
//        error = "";
//        error = Validator.validatePasswordValidation(password, password2);
//        if (!error.isEmpty()) {
//            etPasswordValidation.setError(error);
//            valid = false;
//        }
//
//        error = "";
//        error = Validator.validatePhone(phone);
//        if (!error.isEmpty()) {
//            etPhone.setError(error);
//            valid = false;
//        }

        return true;
    }

    private boolean hasErrors() {
        if (etName.getError() != null) {
            return true;
        }
        if (etSurname.getError() != null) {
            return true;
        }
        if (((TextView) findViewById(R.id.tvGenderError)).getError() != null) {
            return true;
        }
        if (etBirthdate.getError() != null) {
            return true;
        }
        if (etEmail.getError() != null) {
            return true;
        }
        if (etAddress.getError() != null) {
            return true;
        }
        if (((TextView) spiCity.getSelectedView()).getError() != null) {
            return true;
        }
        if (etPassword.getError() != null) {
            return true;
        }
        if (etPasswordValidation.getError() != null) {
            return true;
        }
        if (etPhone.getError() != null) {
            return true;
        }

        return false;
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

        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.set(Calendar.YEAR, maxCalendar.get(Calendar.YEAR) - 14);

        picker.getDatePicker().setMaxDate(maxCalendar.getTimeInMillis());

        picker.show();
    }

    private class setDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day)
        {
            month++;

            String str = day + "/" + month + "/" + year;

            etBirthdate.setText(str);

            etBirthdate.setError(null);
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