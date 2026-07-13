package com.Project.SRMS.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Plain POJO -- no JPA annotations. Rows are mapped manually via JdbcTemplate
 * RowMapper in StudentDao, and persisted with hand-written SQL.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private Long id;
    private String name;
    private String rollNumber;
    private List<Subject> subjects = new ArrayList<>();

    public Student(Long id, String name, String rollNumber) {
        this.id = id;
        this.name = name;
        this.rollNumber = rollNumber;
        this.subjects = new ArrayList<>();
    }
}
