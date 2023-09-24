package com.example.preuniversitario.services;

import com.example.preuniversitario.entities.StudentEntity;
import com.example.preuniversitario.entities.UploadDataEntity;
import com.example.preuniversitario.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public ArrayList<StudentEntity> getStudents(){
        return (ArrayList<StudentEntity>) studentRepository.findAll();
    }

    public void saveStudent(String rut, String names, String surnames, String birthday,
                            String school_type, String school_name, int senior_year){
        StudentEntity student = new StudentEntity();
        student.setRut(rut);
        student.setNames(names);
        student.setSurnames(surnames);
        student.setBirthday(birthday);
        student.setSchool_type(school_type);
        student.setSchool_name(school_name);
        student.setSenior_year(senior_year);
        studentRepository.save(student);
    }

    public Optional<StudentEntity> getByRut(String rut){
        return studentRepository.findById(rut);
    }

    public double getDiscountBySchoolType(StudentEntity student){

        double monthly_fee = 1500000;

        String school_type = student.getSchool_type();

        if(school_type.equals("Municipal")){
            monthly_fee = monthly_fee * 0.8;
        }else if(school_type.equals("Subvencionado")){
            monthly_fee = monthly_fee * 0.9;
        }

        return monthly_fee;
    }

    public double getDiscountBySeniorYear(StudentEntity student){
        double monthly_fee = 1500000;

        int senior_year = student.getSenior_year();
        Date current_time = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(current_time);
        int current_year = calendar.get(Calendar.YEAR);

        int years_since_promotion = current_year - senior_year;

        //HERE WE HAVE TO TAKE THE EXAMPLE OF WHEN IT'S FROM
        //DECEMBER TO THE OTHER YEAR
        if(years_since_promotion < 1){
            monthly_fee = monthly_fee * 0.85;
        }else if(years_since_promotion >= 1 && years_since_promotion <= 2){
            monthly_fee = monthly_fee * 0.92;
        }else if(years_since_promotion >= 3 && years_since_promotion <= 4){
            monthly_fee = monthly_fee * 0.96;
        }

        return monthly_fee;
    }

    public int getMaxFee(StudentEntity student){
        String school_type = student.getSchool_type();

        int max_fee = 4;

        if(school_type.equals("Municipal")){
            max_fee = 10;
        }else if(school_type.equals("Subvencionado")){
            max_fee = 7;
        }

        return max_fee;
    }

    public double getDiscountByAverageScore(int average_score){
        double new_fee_price = 0;
        if(average_score >= 950 && average_score <= 1000){
            new_fee_price = 0.9;
        }else if(average_score >= 900 && average_score <= 949){
            new_fee_price = 0.95;
        }else if(average_score >= 850 && average_score <= 899){
            new_fee_price = 0.98;
        }
        return new_fee_price;
    }

    public double getInterestByLateFee(int months_late){
        double interest = 0;
        if(months_late > 3){
            interest = 0.15;
        }else if(months_late == 3){
            interest = 0.09;
        }else if(months_late == 2){
            interest = 0.06;
        }else if(months_late == 1){
            interest = 0.03;
        }
        return interest;
    }

    public double getDiscountByCashPayment(int total_price){
        return total_price*0.5;
    }

    //Get number of exams by month
    //Get average score by month

}
