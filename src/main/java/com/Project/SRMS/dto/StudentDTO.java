package com.Project.SRMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String name;
    private String rollNumber;
    private List<SubjectDTO> subjects;

    // Computed result fields, so the frontend doesn't need to recompute them
    private double totalObtained;
    private double totalMax;
    private String percentage;
    private String grade;
}
