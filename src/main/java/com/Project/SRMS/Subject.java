package com.Project.SRMS;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subjectName;
    private Double marksObtained;
    private Double maxMarks;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}