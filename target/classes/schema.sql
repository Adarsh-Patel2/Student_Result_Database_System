CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    roll_number VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(255) NOT NULL,
    marks_obtained DOUBLE NOT NULL,
    max_marks DOUBLE NOT NULL,
    student_id BIGINT NOT NULL,
    CONSTRAINT fk_subjects_student
        FOREIGN KEY (student_id) REFERENCES students(id)
        ON DELETE CASCADE
);
