package com.example.studentdatabase.controllers;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.studentdatabase.models.Student;
import com.example.studentdatabase.models.StudentRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class StudentsController {

    @Autowired
    private StudentRepository studentRepo;
    @GetMapping("/students/view")
    public String getAllStudents(Model model) {
        System.out.println("Getting all students");
        List<Student> students = studentRepo.findAll();
        model.addAttribute("students", students);
        return "students/showAll";
    }

    @PostMapping("/students/view/{uid}")
    public String viewStudentInfo(Model model, @PathVariable int uid) {
        Student student = studentRepo.findById(uid).get();
        model.addAttribute("student",student);
        return "students/viewStudentInfo";
    }

    @PostMapping("students/add")
    public String addStudent(@RequestParam Map<String, String> newstudent, HttpServletResponse response) {
        System.out.println("Add Student");
        String newName = newstudent.get("name");
        float newWeight = Float.parseFloat(newstudent.get("weight"));
        float newHeight = Float.parseFloat(newstudent.get("height"));
        String newHaircolor = newstudent.get("haircolor");
        float newGpa = Float.parseFloat(newstudent.get("gpa"));
        studentRepo.save(new Student(newName, newWeight, newHeight, newHaircolor, newGpa));
        response.setStatus(201);
        return "redirect:/students/view";
    }

    @PostMapping("/students/delete/{uid}")
    public String deleteStudent(@PathVariable int uid) {
        studentRepo.deleteById(uid);
        return "redirect:/students/view";
    }

    @GetMapping("/students/edit/{uid}") 
    public String editStudentForm(@PathVariable int uid, Model model) {
        Student student = studentRepo.findById(uid).get();
        model.addAttribute("student", student);
        return "students/editStudentInfo";
    }

    @PostMapping("/students/{uid}")
    public String updateStudent(@PathVariable int uid, @ModelAttribute("student") Student student, Model model) {
        System.out.println("updating student");
        Student existingStudent = studentRepo.findById(uid).get();
        existingStudent.setUid(uid);
        existingStudent.setName(student.getName());
        existingStudent.setWeight(student.getWeight());
        existingStudent.setHeight(student.getHeight());
        existingStudent.setHaircolor(student.getHaircolor());
        existingStudent.setGpa(student.getGpa());
        studentRepo.save(existingStudent);
        return "redirect:/students/view";
    }

}
