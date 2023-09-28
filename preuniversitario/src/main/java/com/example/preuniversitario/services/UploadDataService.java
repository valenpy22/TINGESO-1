package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.UploadDataEntity;
import com.example.preuniversitario.repositories.UploadDataRepository;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadDataService {
    @Autowired
    UploadDataRepository uploadDataRepository;

    private final Logger logg = LoggerFactory.getLogger(UploadDataService.class);

    public ArrayList<UploadDataEntity> getData(){
        return (ArrayList<UploadDataEntity>) uploadDataRepository.findAll();
    }

    @Generated
    public String save(MultipartFile file){
        String filename = file.getOriginalFilename();
        if(filename != null){
            if(!file.isEmpty()){
                try{
                    byte [] bytes = file.getBytes();
                    Path path = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    logg.info("File saved");
                }catch(IOException e){
                    logg.error("ERROR", e);
                }
            }
            return "File saved succesfully";
        }else{
            return "File not saved";
        }
    }

    @Generated
    public void readCSV(String address){
        String text = "";
        BufferedReader buffer = null;
        uploadDataRepository.deleteAll();
        try{
            buffer = new BufferedReader(new FileReader(address));
            String temp = "";
            String bufferRead;
            int count = 1;
            while((bufferRead = buffer.readLine()) != null){
                if(count == 1){
                    count = 0;
                }else{
                    saveDataDB(
                            bufferRead.split(";")[0],
                            bufferRead.split(";")[1],
                            bufferRead.split(";")[2]);
                    temp = temp + "\n" + bufferRead;
                }
            }
            text = temp;
            System.out.println("File read succesfully");
        }catch(Exception e){
            System.err.println("File not found");
        }finally{
            if(buffer != null){
                try{
                    buffer.close();
                }catch (IOException e){
                    logg.error("ERROR", e);
                }
            }
        }
    }

    public void saveData(UploadDataEntity data){
        uploadDataRepository.save(data);
    }

    public void deleteData(ArrayList<UploadDataEntity> datas){
        uploadDataRepository.deleteAll(datas);
    }

    public void saveDataDB(String rut, String exam_date, String score){
        UploadDataEntity new_data = new UploadDataEntity();
        new_data.setRut(rut);
        new_data.setExam_date(exam_date);
        new_data.setScore(score);

        saveData(new_data);
    }

    public List<String> getRuts(){
        return uploadDataRepository.findDistinctRut();
    }

    public double getAverageScoreByRutAndMonth(String rut, String exam_date){
        return uploadDataRepository.getAverageScoreByRutAndMonth(rut, exam_date);
    }

    public double getAverageScoreByRut(String rut){
        return uploadDataRepository.getAverageScoreByRut(rut);
    }

    public long getNumberOfExamsByRut(String rut){
        return uploadDataRepository.findExamsByRut(rut);
    }

}
