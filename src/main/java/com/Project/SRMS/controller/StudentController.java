package com.Project.SRMS.controller;

import com.Project.SRMS.dto.StudentDTO;
import com.Project.SRMS.dto.StudentRequest;
import com.Project.SRMS.dto.SubjectRequest;
import com.Project.SRMS.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public StudentDTO getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentRequest request) {
        StudentDTO created = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/subjects")
    public StudentDTO addSubject(@PathVariable("id") Long studentId, @RequestBody SubjectRequest request) {
        return studentService.addSubject(studentId, request);
    }

    @DeleteMapping("/{id}/subjects/{subjectId}")
    public StudentDTO removeSubject(@PathVariable("id") Long studentId, @PathVariable Long subjectId) {
        return studentService.removeSubject(studentId, subjectId);
    }
}
