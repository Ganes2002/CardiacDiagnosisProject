package com.itc.project.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itc.project.model.Model;
import com.itc.project.service.ServiceInterface;

@RestController
@RequestMapping("/diagnosis")
@CrossOrigin(origins = "*")
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private ServiceInterface serviceInterface;

    // GET /diagnosis - Retrieve all diagnosis records
    @GetMapping
    public ResponseEntity<List<Model>> getAllDiagnoses() {
        logger.info("Request to fetch all diagnosis records.");
        List<Model> diagnoses = serviceInterface.getAllDiagnoses();
        if (diagnoses.isEmpty()) {
            logger.warn("No diagnosis records found.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("Returning {} diagnosis records.", diagnoses.size());
        return new ResponseEntity<>(diagnoses, HttpStatus.OK);
    }

    // GET /diagnosis/gender?value=Male
    @GetMapping("/gender")
    public ResponseEntity<List<Model>> getDiagnosesByGender(@RequestParam String value) {
        logger.info("Request to fetch diagnosis records by gender: {}", value);
        List<Model> diagnoses = serviceInterface.getDiagnosesByGender(value);
        if (diagnoses.isEmpty()) {
            logger.warn("No diagnosis records found for gender: {}", value);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("Returning {} diagnosis records for gender: {}", diagnoses.size(), value);
        return new ResponseEntity<>(diagnoses, HttpStatus.OK);
    }

    // GET /diagnosis/diabetic?value=Yes
    @GetMapping("/diabetic")
    public ResponseEntity<List<Model>> getDiagnosesByDiabeticStatus(@RequestParam String value) {
        logger.info("Request to fetch diagnosis records by diabetic status: {}", value);
        List<Model> diagnoses = serviceInterface.getDiagnosesByDiabeticStatus(value);
        if (diagnoses.isEmpty()) {
            logger.warn("No diagnosis records found for diabetic status: {}", value);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("Returning {} diagnosis records for diabetic status: {}", diagnoses.size(), value);
        return new ResponseEntity<>(diagnoses, HttpStatus.OK);
    }

    // GET /diagnosis/smoking-status?value=Never
    @GetMapping("/smoking-status")
    public ResponseEntity<List<Model>> getDiagnosesBySmokingStatus(@RequestParam String value) {
        logger.info("Request to fetch diagnosis records by smoking status: {}", value);
        List<Model> diagnoses = serviceInterface.getDiagnosesBySmokingStatus(value);
        if (diagnoses.isEmpty()) {
            logger.warn("No diagnosis records found for smoking status: {}", value);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("Returning {} diagnosis records for smoking status: {}", diagnoses.size(), value);
        return new ResponseEntity<>(diagnoses, HttpStatus.OK);
    }

    // GET /diagnosis/pain-type?value=Chest
    @GetMapping("/pain-type")
    public ResponseEntity<List<Model>> getDiagnosesByPainType(@RequestParam String value) {
        logger.info("Request to fetch diagnosis records by pain type: {}", value);
        List<Model> diagnoses = serviceInterface.getDiagnosesByPainType(value);
        if (diagnoses.isEmpty()) {
            logger.warn("No diagnosis records found for pain type: {}", value);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("Returning {} diagnosis records for pain type: {}", diagnoses.size(), value);
        return new ResponseEntity<>(diagnoses, HttpStatus.OK);
    }

    // GET /diagnosis/age-above?value=60
    @GetMapping("/age-above")
    public ResponseEntity<List<Model>> getDiagnosesByAgeAbove(@RequestParam int value) {
        logger.info("Request to fetch diagnosis records for age above: {}", value);
        List<Model> diagnoses = serviceInterface.getDiagnosesByAgeAbove(value);
        if (diagnoses.isEmpty()) {
            logger.warn("No diagnosis records found for age above: {}", value);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("Returning {} diagnosis records for age above: {}", diagnoses.size(), value);
        return new ResponseEntity<>(diagnoses, HttpStatus.OK);
    }

    // GET /diagnosis/age-below?value=60
    @GetMapping("/age-below")
    public ResponseEntity<List<Model>> getDiagnosesByAgeBelow(@RequestParam int value) {
        logger.info("Request to fetch diagnosis records for age below: {}", value);
        List<Model> diagnoses = serviceInterface.getDiagnosesByAgeBelow(value);
        if (diagnoses.isEmpty()) {
            logger.warn("No diagnosis records found for age below: {}", value);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("Returning {} diagnosis records for age below: {}", diagnoses.size(), value);
        return new ResponseEntity<>(diagnoses, HttpStatus.OK);
    }

    // GET /diagnosis/filter?smokingStatus=Never&diabetic=Yes
    @GetMapping("/filter")
    public ResponseEntity<List<Model>> getDiagnosesBySmokingAndDiabetic(
            @RequestParam String smokingStatus,
            @RequestParam String diabetic) {
        logger.info("Request to fetch diagnosis records with smokingStatus: {} and diabetic: {}", smokingStatus, diabetic);
        List<Model> diagnoses = serviceInterface.getDiagnosesBySmokingAndDiabetic(smokingStatus, diabetic);
        if (diagnoses.isEmpty()) {
            logger.warn("No diagnosis records found for smokingStatus: {} and diabetic: {}", smokingStatus, diabetic);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("Returning {} diagnosis records for smokingStatus: {} and diabetic: {}", diagnoses.size(), smokingStatus, diabetic);
        return new ResponseEntity<>(diagnoses, HttpStatus.OK);
    }
}
