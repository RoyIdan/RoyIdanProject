package com.example.royidanproject.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Validator {

    public static String validateEmptyFields
            (String name, String surname, String date, String email, String address, String city, String password, String password2, String phone) {
        if (name.isEmpty() || surname.isEmpty() || date.isEmpty() || email.isEmpty() || address.isEmpty() || password.isEmpty() || password2.isEmpty() || phone.isEmpty()) {
            return "אנא מלא את כל השדות.";
        }
        return "";
    }

    public static boolean isEmpty(String str) {
        return str.isEmpty();
    }

    public static String validateName(String name) {
        //name
        if (name.length() < 2 || name.length() > 10) {
            return "השם הפרטי חייב להיות בין 2 ל10 תווים.";
        }

        if (!allCharacters(name)) {
            return "השם הפרטי יכול להכיל רק אותיות בעברית.";
        }

        return "";
    }

    public static String validateSurname(String surname) {
        //surname
        if (surname.length() < 2 || surname.length() > 16) {
            return "שם המשפחה צריך להיות בין 2 ל16 תווים.";
        }

        if (!allCharactersOrSpace(surname)) {
            return "שם המשפחה יכול להכיל רק אותיות בעברית ורווח.";
        }

        if (surname.indexOf(' ') != surname.lastIndexOf(' ')) {
            return "לא יכול להיות יותר מרווח אחד בשם המשפחה.";
        }
        return "";
    }

    public static String validateBirthdate(String date) {
        // birthdate
        try {
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            return "התאריך לא בפורמט הנכון. (dd/MM/yyyy)";
        }
        return "";
    }

    public static String validateEmail(String email) {
        // email
        if (!email.contains("@")) {
            return "האימייל צריך להכיל @.";
        }
        if (email.indexOf("@") == 0) {
            return "ה@ לא יכול להיות ראשון.";
        }
        if (email.indexOf("@") != email.lastIndexOf("@")) {
            return "לא יכול להיות יותר מ@ אחד.";
        }
        if (!email.contains(".")) {
            return "חייבת להיות נקודה אחת לפחות באימייל.";
        }
        if (email.indexOf(".") < email.indexOf("@")) {
            return "לא יכולה להיות נקודה לפני ה@.";
        }
        if (email.indexOf(".") - 1 == email.indexOf("@")) {
            return "הנקודה לא יכולה להיות מיד אחרי ה@.";
        }
        if (email.lastIndexOf(".") == email.length() - 1) {
            return "הנקודה לא יכולה להיות אחרונה.";
        }
        return "";
    }

    public static String validateCity(String city) {
        // city
        if (city.equals("select city")) {
            return "תבחר עיר בבקשה.";
        }
        return "";
    }

    public static String validatePassword(String password) {
        // password
        if (password.length() < 3 || password.length() > 16) {
            return "הסיסמא חייבת להיות בין 3 ל16 תווים.";
        }
        return "";
    }

    public static String validatePasswordValidation(String password, String password2) {
        if (!password.equals(password2)) {
            return "הסיסמאות לא תואמות.";
        }
        return "";
    }

    public static String validatePhone(String phone) {
        if (phone.length() != 10) {
            return "בטלפון צריך בדיוק 10 ספרות.";
        }
        return "";
    }

    public static boolean allCharacters(String input) {
        for (char c : input.toCharArray()) {
            if (c < 'א' || c > 'ת') {
                return false;
            }
        }

        return true;
    }

    public static boolean allCharactersOrSpace(String input) {
        for (char c : input.toCharArray()) {
            if ((c < 'א' || c > 'ת' ) && c != ' ' ) {
                return false;
            }
        }

        return true;
    }
}
