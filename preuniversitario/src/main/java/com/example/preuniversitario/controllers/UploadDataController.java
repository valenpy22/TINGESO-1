package com.example.preuniversitario.controllers;

import com.example.preuniversitario.entities.UploadDataEntity;
import com.example.preuniversitario.services.UploadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping
public class UploadDataController {
    @Autowired
    private UploadDataService uploadDataService;

    @GetMapping("/fileUpload")
    public String main(){
        return "fileUpload";
    }

    @PostMapping("/fileUpload")
    public String upload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes){
        uploadDataService.save(file);

        redirectAttributes.addFlashAttribute("messageFUS", "File uploaded succesfully");
        uploadDataService.readCSV("data");
        return "redirect:/fileUpload";
    }

    @GetMapping("/fileInformation")
    public String listing(Model model){
        ArrayList<UploadDataEntity> datas = uploadDataService.getData();
        model.addAttribute("datas", datas);
        return "fileInformation";
    }
}
