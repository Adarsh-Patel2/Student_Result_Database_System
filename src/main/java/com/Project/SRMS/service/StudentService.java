package com.Project.SRMS.service;

import com.Project.SRMS.dao.StudentDao;
import com.Project.SRMS.dao.SubjectDao;
import com.Project.SRMS.dto.StudentDTO;
import com.Project.SRMS.dto.StudentRequest;
import com.Project.SRMS.dto.SubjectDTO;
import com.Project.SRMS.dto.SubjectRequest;
import com.Project.SRMS.exception.DuplicateRollNumberException;
import com.Project.SRMS.exception.ResourceNotFoundException;
import com.Project.SRMS.model.Student;
import com.Project.SRMS.model.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentDao studentDao;
    private final SubjectDao subjectDao;

    public StudentService(StudentDao studentDao, SubjectDao subjectDao) {
        this.studentDao = studentDao;
        this.subjectDao = subjectDao;
    }

    public List<StudentDTO> getAllStudents() {
        return studentDao.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public StudentDTO getStudentById(Long id) {
        Student student = studentDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return toDto(student);
    }

    public StudentDTO createStudent(StudentRequest request) {
        String name = request.getName() == null ? "" : request.getName().trim();
        String roll = request.getRollNumber() == null ? "" : request.getRollNumber().trim();

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (roll.isEmpty()) {
            throw new IllegalArgumentException("Roll number is required.");
        }
        if (studentDao.existsByRollNumber(roll)) {
            throw new DuplicateRollNumberException("This roll number already exists.");
        }

        Student student = new Student(null, name, roll);
        Student saved = studentDao.save(student);
        return toDto(saved);
    }

    public void deleteStudent(Long id) {
        studentDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        subjectDao.deleteByStudentId(id);
        studentDao.deleteById(id);
    }

    public StudentDTO addSubject(Long studentId, SubjectRequest request) {
        studentDao.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        String subjectName = request.getSubjectName() == null ? "" : request.getSubjectName().trim();
        Double obtained = request.getMarksObtained();
        Double max = request.getMaxMarks();

        if (subjectName.isEmpty()) {
            throw new IllegalArgumentException("Subject name is required.");
        }
        if (obtained == null || obtained < 0) {
            throw new IllegalArgumentException("Enter valid marks.");
        }
        if (max == null || max < 1) {
            throw new IllegalArgumentException("Max marks must be at least 1.");
        }
        if (obtained > max) {
            throw new IllegalArgumentException("Cannot exceed max marks.");
        }

        Subject subject = new Subject(null, subjectName, obtained, max, studentId);
        subjectDao.save(subject);

        return getStudentById(studentId);
    }

    public StudentDTO removeSubject(Long studentId, Long subjectId) {
        studentDao.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        subjectDao.deleteById(subjectId);
        return getStudentById(studentId);
    }

    private StudentDTO toDto(Student student) {
        List<Subject> subjects = subjectDao.findByStudentId(student.getId());

        double totalObtained = subjects.stream().mapToDouble(Subject::getMarksObtained).sum();
        double totalMax = subjects.stream().mapToDouble(Subject::getMaxMarks).sum();
        String percentage = totalMax > 0
                ? String.format("%.2f", (totalObtained / totalMax) * 100)
                : "0.00";
        String grade = computeGrade(Double.parseDouble(percentage));

        List<SubjectDTO> subjectDtos = subjects.stream()
                .map(s -> new SubjectDTO(s.getId(), s.getSubjectName(), s.getMarksObtained(), s.getMaxMarks()))
                .toList();

        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getRollNumber(),
                subjectDtos,
                totalObtained,
                totalMax,
                percentage,
                grade
        );
    }

    private String computeGrade(double pct) {
        if (pct >= 90) return "A+";
        if (pct >= 75) return "A";
        if (pct >= 60) return "B";
        if (pct >= 40) return "C";
        return "Fail";
    }
}
