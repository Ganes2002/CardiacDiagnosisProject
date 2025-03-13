package com.itc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
//import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class BookmarkModel {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long bookmarkId;
//@ManyToOne
//private User user;
@NotNull(message = "userId cannot be null")
private String userName;
@Min(value = 1, message = "Age must be at least 1")
@Max(value = 120, message = "Age must be at most 120")
private int age;
@Min(value = 50, message = " Blood Pressure(BP) must be at least 50")
@Max(value = 200, message = "Blood Pressure(BP) must be at most 200")
private int bp;
@Min(value = 100, message = "Cholesterol level must be at least 100")
@Max(value = 400, message = "Cholesterol level must be at most 400")
private int cholesterol;
@NotBlank(message = "diabetic cannot be blank")
private String diabetic;
@NotBlank(message = "Smoking status cannot be blank")
private String smoking_status;
@NotBlank(message = "Pain type cannot be blank")
private String pain_type;
@NotBlank(message = "Treatment cannot be blank")
private String treatment;
@NotBlank(message = "gender cannot be blank")
private String gender;

private String id;


public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public Long getBookmarkId() {
	return bookmarkId;
}
public void setBookmarkId(Long bookmarlId) {
	this.bookmarkId = bookmarlId;
}

public int getAge() {
	return age;
}
public void setAge(int age) {
	this.age = age;
}
public int getBp() {
	return bp;
}
public void setBp(int bp) {
	this.bp = bp;
}
public int getCholesterol() {
	return cholesterol;
}
public void setCholesterol(int cholesterol) {
	this.cholesterol = cholesterol;
}

public String getDiabetic() {
	return diabetic;
}
public void setDiabetic(String diabetic) {
	this.diabetic = diabetic;
}

public String getSmoking_status() {
	return smoking_status;
}
public void setSmoking_status(String smoking_status) {
	this.smoking_status = smoking_status;
}
public String getPain_type() {
	return pain_type;
}
public void setPain_type(String pain_type) {
	this.pain_type = pain_type;
}
public String getTreatment() {
	return treatment;
}
public void setTreatment(String treatment) {
	this.treatment = treatment;
}


public String getGender() {
	return gender;
}
public void setGender(String gender) {
	this.gender = gender;
}
public BookmarkModel() {
	super();
	// TODO Auto-generated constructor stub
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
@Override
public String toString() {
	return "BookmarkModel [bookmarkId=" + bookmarkId + ", userName=" + userName + ", age=" + age + ", bp=" + bp
			+ ", cholesterol=" + cholesterol + ", diabetic=" + diabetic + ", smoking_status=" + smoking_status
			+ ", pain_type=" + pain_type + ", treatment=" + treatment + ", gender=" + gender + ", id=" + id + "]";
}








}
