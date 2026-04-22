package com.lxp.sohee.course.infrastructure;

import com.lxp.sohee.course.model.Course;
import com.lxp.sohee.course.model.CourseLevel;
import com.lxp.sohee.course.model.CourseSection;
import com.lxp.sohee.course.model.CourseSectionRepository;
import com.lxp.sohee.course.model.CourseStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

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

    // 섹션 1개 조회
    @Override
    public Optional<CourseSection> findById(Long id) {
        String sql = """
            SELECT s.id AS s_id, s.title AS s_title, s.created_at AS s_at, s.updated_at AS s_up,
                   c.id AS c_id, c.title AS c_title, c.description, c.instructor_id, 
                   c.status, c.level, c.published_at, c.created_at, c.updated_at
            FROM course_sections s
            JOIN courses c ON s.course_id = c.id
            WHERE s.id = ?
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Course course = Course.reconstruct(
                            rs.getLong("c_id"),
                            rs.getString("c_title"),
                            rs.getString("description"),
                            rs.getLong("instructor_id"),
                            CourseStatus.valueOf(rs.getString("status")),
                            CourseLevel.valueOf(rs.getString("level")),
                            rs.getObject("published_at", LocalDateTime.class),
                            rs.getObject("created_at", LocalDateTime.class),
                            rs.getObject("updated_at", LocalDateTime.class)
                    );

                    CourseSection section = CourseSection.reconstruct(
                            rs.getLong("s_id"),
                            course,
                            rs.getString("s_title"),
                            rs.getObject("s_at", LocalDateTime.class),
                            rs.getObject("s_up", LocalDateTime.class)
                    );
                    return Optional.of(section);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("ID로 섹션 정보를 조회하는 중 DB 오류가 발생했습니다.", e);
        }

        return Optional.empty();
    }
}
