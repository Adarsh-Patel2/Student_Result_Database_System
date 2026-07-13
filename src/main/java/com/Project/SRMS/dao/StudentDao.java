package com.Project.SRMS.dao;

import com.Project.SRMS.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentDao {

    private final JdbcTemplate jdbcTemplate;

    public StudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Student mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new Student(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("roll_number")
        );
    }

    public List<Student> findAll() {
        String sql = "SELECT id, name, roll_number FROM students ORDER BY id";
        return jdbcTemplate.query(sql, StudentDao::mapRow);
    }

    public Optional<Student> findById(Long id) {
        String sql = "SELECT id, name, roll_number FROM students WHERE id = ?";
        List<Student> results = jdbcTemplate.query(sql, StudentDao::mapRow, id);
        return results.stream().findFirst();
    }

    public boolean existsByRollNumber(String rollNumber) {
        String sql = "SELECT COUNT(*) FROM students WHERE roll_number = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, rollNumber);
        return count != null && count > 0;
    }

    public Student save(Student student) {
        String sql = "INSERT INTO students (name, roll_number) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getName());
            ps.setString(2, student.getRollNumber());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        student.setId(generatedId);
        return student;
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM students WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
