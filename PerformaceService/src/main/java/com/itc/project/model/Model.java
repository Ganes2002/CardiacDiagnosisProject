package com.itc.project.model;


public class Model 
{
	private String id;
	private String gender;
	private int age;
	private int bp;                
	private int cholesterol;
	private String diabetic;
	private String smoking_status;
	private String pain_type;
	private String treatment;
	@Override
	public String toString() {
		return "Model [id=" + id + ", gender=" + gender + ", age=" + age + ", bp=" + bp + ", cholesterol=" + cholesterol
				+ ", diabetic=" + diabetic + ", smoking_status=" + smoking_status + ", pain_type=" + pain_type
				+ ", treatment=" + treatment + "]";
	}
	public Model() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Model(String id, String gender, int age, int bp, int cholesterol, String diabetic, String smoking_status,
			String pain_type, String treatment) {
		super();
		this.id = id;
		this.gender = gender;
		this.age = age;
		this.bp = bp;
		this.cholesterol = cholesterol;
		this.diabetic = diabetic;
		this.smoking_status = smoking_status;
		this.pain_type = pain_type;
		this.treatment = treatment;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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

}
