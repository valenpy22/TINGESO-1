package com.example.preuniversitario;

import com.example.preuniversitario.entities.UploadDataEntity;
import com.example.preuniversitario.repositories.UploadDataRepository;
import com.example.preuniversitario.services.UploadDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class UploadDataTest {

    @Autowired
    UploadDataRepository uploadDataRepository;

    @Autowired
    UploadDataService uploadDataService;

    @Test
    void getDataTest(){
        UploadDataEntity uploadData = new UploadDataEntity();

        uploadData.setRut("21305689-1");
        uploadData.setScore("890");
        uploadData.setExam_date("02/10/2023");
        uploadDataRepository.save(uploadData);
        assertNotEquals(new ArrayList<>(), uploadDataService.getData());
        uploadDataRepository.deleteAll();
    }

    @Test
    void saveDataTest(){
        UploadDataEntity uploadData = new UploadDataEntity();

        uploadData.setRut("21305689-1");
        uploadData.setScore("890");
        uploadData.setExam_date("02/10/2023");
        uploadDataService.saveData(uploadData);
        assertNotEquals(new ArrayList<>(), uploadDataService.getData());
        uploadDataRepository.deleteAll();
    }

    /*
    @Test
    void deleteDataTest(){

    }

     */

    @Test
    void saveDataDBTest(){
        uploadDataService.saveDataDB("21305689-1", "01/01/2023", "900");
        assertNotEquals(new ArrayList<>(), uploadDataRepository.findAll());
        uploadDataRepository.deleteAll();
    }

    @Test
    void getRutsTest(){
        UploadDataEntity uploadData1 = new UploadDataEntity();
        UploadDataEntity uploadData2 = new UploadDataEntity();
        uploadData1.setRut("21305689-1");
        uploadData2.setRut("20708642-0");
        uploadDataService.saveData(uploadData1);
        uploadDataService.saveData(uploadData2);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("21305689-1");
        arrayList.add("20708642-0");

        assertEquals(arrayList, uploadDataService.getRuts());
        uploadDataRepository.deleteAll();
    }

    @Test
    void getAverageScoreByRutAndMonthTest(){
        UploadDataEntity u1 = new UploadDataEntity();
        UploadDataEntity u2 = new UploadDataEntity();
        u1.setRut("21305689-1");
        u1.setScore("800");
        u1.setExam_date("07/10/2023");
        u2.setRut("21305689-1");
        u2.setScore("1000");
        u2.setExam_date("07/10/2023");
        uploadDataRepository.save(u1);
        uploadDataRepository.save(u2);
        assertEquals(900, uploadDataService.getAverageScoreByRutAndMonth(u1.getRut(), u1.getExam_date()));
        uploadDataRepository.deleteAll();
    }

    @Test
    void getAverageScoreByRutTest(){
        UploadDataEntity u1 = new UploadDataEntity();
        UploadDataEntity u2 = new UploadDataEntity();
        u1.setRut("21305689-1");
        u1.setScore("800");
        u1.setExam_date("07/10/2023");
        u2.setRut("21305689-1");
        u2.setScore("1000");
        u2.setExam_date("07/10/2023");
        uploadDataRepository.save(u1);
        uploadDataRepository.save(u2);
        assertEquals(900, uploadDataService.getAverageScoreByRut(u1.getRut()));
        uploadDataRepository.deleteAll();
    }

    @Test
    void getNumberOfExamsByRutTest(){
        UploadDataEntity u1 = new UploadDataEntity();
        UploadDataEntity u2 = new UploadDataEntity();
        u1.setRut("21305689-1");
        u1.setScore("800");
        u1.setExam_date("07/10/2023");
        u2.setRut("21305689-1");
        u2.setScore("1000");
        u2.setExam_date("07/10/2023");
        uploadDataRepository.save(u1);
        uploadDataRepository.save(u2);
        assertEquals(2, uploadDataService.getNumberOfExamsByRut(u1.getRut()));
        uploadDataRepository.deleteAll();
    }

    @Test
    void getLastExamDateTest(){
        UploadDataEntity u1 = new UploadDataEntity();
        UploadDataEntity u2 = new UploadDataEntity();
        u1.setRut("21305689-1");
        u1.setScore("800");
        u1.setExam_date("07/10/2023");
        u2.setRut("21305689-1");
        u2.setScore("1000");
        u2.setExam_date("07/11/2023");
        uploadDataRepository.save(u1);
        uploadDataRepository.save(u2);
        assertEquals("07/11/2023", uploadDataService.getLastExamDate(u1.getRut()));
        uploadDataRepository.deleteAll();
    }

    @Test
    void deleteAllUploadDataTest(){
        UploadDataEntity uploadData = new UploadDataEntity();
        uploadData.setRut("1");
        uploadDataRepository.save(uploadData);
        uploadDataService.deleteAll();
        assertEquals(new ArrayList<>(), uploadDataRepository.findAll());
    }
}
