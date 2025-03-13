package com.itc.project.service;
import com.itc.project.model.Model;

import java.util.List;

public interface ServiceInterface 
{
	  List<Model> getAllDiagnoses();
	  List<Model> getDiagnosesByGender(String gender);
	  List<Model> getDiagnosesByDiabeticStatus(String diabetic);
	  List<Model> getDiagnosesBySmokingStatus(String smokingStatus);
	  List<Model> getDiagnosesByPainType(String painType);
	  List<Model> getDiagnosesByAgeAbove(int age);
	  List<Model> getDiagnosesByAgeBelow(int age);
	  List<Model> getDiagnosesBySmokingAndDiabetic(String smokingStatus, String diabetic);

}
