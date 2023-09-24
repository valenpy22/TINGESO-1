package com.example.preuniversitario.services;

import com.example.preuniversitario.repositories.ReportSummaryRepository;
import com.example.preuniversitario.repositories.UploadDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class ReportSummaryService {
    @Autowired
    private ReportSummaryRepository reportSummaryRepository;

    @Autowired
    private UploadDataService uploadDataService;

    public void getReportSummary() throws ParseException {
        reportSummaryRepository.deleteAll();
        List<String> listRuts = uploadDataService.getRuts();
        for(String rut : listRuts){
            //calculate
        }
    }
}
