package com.lxp.sohee.course.infrastructure;

import com.lxp.sohee.course.model.Course;
import com.lxp.sohee.course.model.CourseRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class JdbcCourseRepository implements CourseRepository {
    private final Connection connection;

    public JdbcCourseRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Course save(Course course) {
        if (course.getId() == null) {
            return insert(course);
        } else {
            return update(course);
        }
    }

    // 강좌 생성
    private Course insert(Course course) {
        String sql = "INSERT INTO courses (title, description, instructor_id, status, level, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getDescription());
            pstmt.setLong(3, course.getInstructorId());
            pstmt.setString(4, course.getStatus().name());
            pstmt.setString(5, course.getLevel().name());
            pstmt.setObject(6, course.getCreatedAt());
            pstmt.setObject(7, course.getUpdatedAt());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("강좌 생성 실패: 저장된 행이 없습니다.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    // DB에서 받은 ID를 포함하여 객체를 복원해서 반환 (reconstruct 활용)
                    return Course.reconstruct(
                            id, course.getTitle(), course.getDescription(), course.getInstructorId(),
                            course.getStatus(), course.getLevel(), course.getPublishedAt(),
                            course.getCreatedAt(), course.getUpdatedAt()
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return course;
    }

    // 강좌 수정
    private Course update(Course course) {
        return course;
    }

    @Override
    public List<Course> findAll() {
        return List.of();
    }

    @Override
    public Optional<Course> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean existCourseId(Long id) {
        return false;
    }
}
