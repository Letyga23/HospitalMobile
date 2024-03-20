package com.example.hospitalmobile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Patient implements Parcelable {
    private int Id_Patient;
    private String LastName;
    private String FirstName;
    private String Patronymic;
    private String PassportData;
    private String DateOfBirth;
    private String Gender;
    private String Address;
    private String PhoneNumber;
    private String Email;
    private String PatientPhoto;

    public Patient(int id_Patient, String lastName, String firstName, String patronymic, String passportData, String dateOfBirth, String gender, String address, String phoneNumber, String email, String photo) {
        Id_Patient = id_Patient;
        LastName = lastName;
        FirstName = firstName;
        Patronymic = patronymic;
        PassportData = passportData;
        DateOfBirth = dateOfBirth;
        Gender = gender;
        Address = address;
        PhoneNumber = phoneNumber;
        Email = email;
        PatientPhoto = WorkingWithImage.resizeBase64Image(photo);
    }

    protected Patient(Parcel in) {
        Id_Patient = in.readInt();
        LastName = in.readString();
        FirstName = in.readString();
        Patronymic = in.readString();
        PassportData = in.readString();
        DateOfBirth = in.readString();
        Gender = in.readString();
        Address = in.readString();
        PhoneNumber = in.readString();
        Email = in.readString();
        PatientPhoto = in.readString();
    }



    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public int getId_Patient() {
        return Id_Patient;
    }

    public void setId_Patient(int id_Patient) {
        Id_Patient = id_Patient;
    }

    public String getLastName() { return LastName; }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getPatronymic() {
        return Patronymic;
    }

    public void setPatronymic(String patronymic) {
        Patronymic = patronymic;
    }

    public String getPassportData() {
        return PassportData;
    }

    public void setPassportData(String passportData) {
        PassportData = passportData;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPatientPhoto() {
        return PatientPhoto;
    }

    public void setPatientPhoto(String patientPhoto) {
        PatientPhoto = patientPhoto;
    }

    public Bitmap getBitmapSource()
    {
        String patientPhoto = getPatientPhoto();
        if (patientPhoto != null) {
            byte[] array = android.util.Base64.decode(patientPhoto, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(array, 0, array.length);
        } else {
            // Обработка случая, когда строка Base64 пуста или null
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(Id_Patient);
        dest.writeString(LastName);
        dest.writeString(FirstName);
        dest.writeString(Patronymic);
        dest.writeString(PassportData);
        dest.writeString(DateOfBirth);
        dest.writeString(Gender);
        dest.writeString(Address);
        dest.writeString(PhoneNumber);
        dest.writeString(Email);
        dest.writeString(PatientPhoto);
    }

    public JSONObject convertToJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Id_Patient", this.Id_Patient);
        jsonObject.put("LastName", this.LastName);
        jsonObject.put("FirstName", this.FirstName);
        jsonObject.put("Patronymic", this.Patronymic);
        jsonObject.put("PassportData", this.PassportData);
        jsonObject.put("DateOfBirth", this.DateOfBirth);
        jsonObject.put("Gender", this.Gender);
        jsonObject.put("Address", this.Address);
        jsonObject.put("PhoneNumber", this.PhoneNumber);
        jsonObject.put("Email", this.Email);
        jsonObject.put("PatientPhoto", this.PatientPhoto);
        return jsonObject;
    }
}
