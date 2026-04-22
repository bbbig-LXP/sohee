package com.lxp.sohee.course.infrastructure;

import com.lxp.sohee.course.model.Course;
import com.lxp.sohee.course.model.CourseLevel;
import com.lxp.sohee.course.model.CourseRepository;
import com.lxp.sohee.course.model.CourseStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        String sql = "UPDATE courses SET title = ?, description = ?, status = ?, level = ?, updated_at = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getDescription());
            pstmt.setString(3, course.getStatus().name());
            pstmt.setString(4, course.getLevel().name());
            pstmt.setTimestamp(5, Timestamp.valueOf(course.getUpdatedAt()));
            pstmt.setLong(6, course.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("수정할 강의를 찾을 수 없습니다. ID: " + course.getId());
            }

            return course;
        } catch (SQLException e) {
            throw new RuntimeException("강의 수정 중 오류 발생", e);
        }
    }

    // 강좌 목록 전체 조회
    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();

        String sql = "SELECT id, title, description, instructor_id, status, level, published_at, created_at, updated_at FROM courses";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();) {

            while (rs.next()) {
                Course course = Course.reconstruct(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getLong("instructor_id"),
                        CourseStatus.valueOf(rs.getString("status")),
                        CourseLevel.valueOf(rs.getString("level")),
                        rs.getObject("published_at", LocalDateTime.class), // 타입을 명시해주면 더 안전해요
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class)
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("전체 강의 조회 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("DB 조회 오류", e);
        }

        return courses;
    }

    // 강좌 1개 조회
    @Override
    public Optional<Course> findById(Long id) {
        String sql = "SELECT id, title, description, instructor_id, status, level, published_at, created_at, updated_at FROM courses WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Course course = Course.reconstruct(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getLong("instructor_id"),
                            CourseStatus.valueOf(rs.getString("status")),
                            CourseLevel.valueOf(rs.getString("level")),
                            rs.getObject("published_at", LocalDateTime.class),
                            rs.getObject("created_at", LocalDateTime.class),
                            rs.getObject("updated_at", LocalDateTime.class)
                    );
                    return Optional.of(course);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("ID로 강의 조회 중 오류 발생", e);
        }

        return Optional.empty();
    }

    // 강의 존재 여부 확인
    @Override
    public boolean existCourseId(Long id) {
        String sql = "SELECT id FROM courses WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // id가 있으면 true, 없으면 false
            }
        }catch (SQLException e) {
            throw new RuntimeException("ID로 강의 조회 중 오류 발생", e);
        }
    }
}
