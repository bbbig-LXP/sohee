package com.lxp.sohee.course.infrastructure;

import com.lxp.sohee.course.model.Course;
import com.lxp.sohee.course.model.CourseSection;
import com.lxp.sohee.course.model.CourseSectionRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcCourseSectionRepository implements CourseSectionRepository {
    private final Connection connection;

    public JdbcCourseSectionRepository(Connection connection) {
        this.connection = connection;
    }

    // 강좌 섹션 생성
    @Override
    public CourseSection save(CourseSection section) {
        String sql = "INSERT INTO course_sections (course_id, title, created_at, updated_at) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, section.getCourse().getId());
            pstmt.setString(2, section.getTitle());
            pstmt.setObject(3, section.getCreatedAt());
            pstmt.setObject(4, section.getUpdatedAt());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("강좌 섹션 생성 실패: 저장된 행이 없습니다.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    // DB에서 받은 ID를 포함하여 객체를 복원해서 반환 (reconstruct 활용)
                    return CourseSection.reconstruct(id, section.getCourse(), section.getTitle(), section.getCreatedAt(), section.getUpdatedAt());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
