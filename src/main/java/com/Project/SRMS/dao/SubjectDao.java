package com.Project.SRMS.dao;

import com.Project.SRMS.model.Subject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SubjectDao {

    private final JdbcTemplate jdbcTemplate;

    public SubjectDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Subject mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new Subject(
                rs.getLong("id"),
                rs.getString("subject_name"),
                rs.getDouble("marks_obtained"),
                rs.getDouble("max_marks"),
                rs.getLong("student_id")
        );
    }

    public List<Subject> findByStudentId(Long studentId) {
        String sql = "SELECT id, subject_name, marks_obtained, max_marks, student_id " +
                "FROM subjects WHERE student_id = ? ORDER BY id";
        return jdbcTemplate.query(sql, SubjectDao::mapRow, studentId);
    }

    public Subject save(Subject subject) {
        String sql = "INSERT INTO subjects (subject_name, marks_obtained, max_marks, student_id) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, subject.getSubjectName());
            ps.setDouble(2, subject.getMarksObtained());
            ps.setDouble(3, subject.getMaxMarks());
            ps.setLong(4, subject.getStudentId());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        subject.setId(generatedId);
        return subject;
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM subjects WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public void deleteByStudentId(Long studentId) {
        String sql = "DELETE FROM subjects WHERE student_id = ?";
        jdbcTemplate.update(sql, studentId);
    }
}
