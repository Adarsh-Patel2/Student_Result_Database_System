package com.Project.SRMS.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Plain POJO -- no JPA annotations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    private Long id;
    private String subjectName;
    private Double marksObtained;
    private Double maxMarks;
    private Long studentId;
}
