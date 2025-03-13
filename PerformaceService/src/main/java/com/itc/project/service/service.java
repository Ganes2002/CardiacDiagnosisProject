package com.itc.project.service;

import com.itc.project.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class service implements ServiceInterface 
{
	private final String API_URL = "http://localhost:3232/diagnosis";
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public List<Model> getAllDiagnoses() {
        Model[] response = restTemplate.getForObject(API_URL, Model[].class);
        return Arrays.asList(response);
    }
    @Override
    public List<Model> getDiagnosesByGender(String gender) {
        return getAllDiagnoses().stream()
                .filter(diagnosis -> diagnosis.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
    }
    @Override
    public List<Model> getDiagnosesByDiabeticStatus(String diabetic) {
        return getAllDiagnoses().stream()
                .filter(diagnosis -> diagnosis.getDiabetic().equalsIgnoreCase(diabetic))
                .collect(Collectors.toList());
    }
    @Override
    public List<Model> getDiagnosesBySmokingStatus(String smokingStatus) {
        return getAllDiagnoses().stream()
                .filter(diagnosis -> diagnosis.getSmoking_status().equalsIgnoreCase(smokingStatus))
                .collect(Collectors.toList());
    }
    @Override
    public List<Model> getDiagnosesByPainType(String painType) {
        return getAllDiagnoses().stream()
                .filter(diagnosis -> diagnosis.getPain_type().equalsIgnoreCase(painType))
                .collect(Collectors.toList());
    }
    @Override
    public List<Model> getDiagnosesByAgeAbove(int age) {
        return getAllDiagnoses().stream()
                .filter(diagnosis -> diagnosis.getAge() > age)
                .collect(Collectors.toList());
    }
    @Override
    public List<Model> getDiagnosesByAgeBelow(int age) {
        return getAllDiagnoses().stream()
                .filter(diagnosis -> diagnosis.getAge() < age)
                .collect(Collectors.toList());
    }
    @Override
    public List<Model> getDiagnosesBySmokingAndDiabetic(String smokingStatus, String diabetic) {
        return getAllDiagnoses().stream()
                .filter(diagnosis -> diagnosis.getSmoking_status().equalsIgnoreCase(smokingStatus) &&
                        diagnosis.getDiabetic().equalsIgnoreCase(diabetic))
                .collect(Collectors.toList());
    }
}
