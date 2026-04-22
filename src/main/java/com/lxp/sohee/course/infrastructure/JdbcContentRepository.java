package com.lxp.sohee.course.infrastructure;

import com.lxp.sohee.course.model.Content;
import com.lxp.sohee.course.model.ContentRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcContentRepository implements ContentRepository {
    private  final Connection connection;

    public JdbcContentRepository(Connection connection) {
        this.connection = connection;
    }

    // 컨텐츠 생성
    @Override
    public Content save(Content content) {
        String sql = "INSERT INTO contents (section_id, title, content_type, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, content.getSection().getId());
            pstmt.setString(2, content.getTitle());
            pstmt.setString(3, content.getType().name());
            pstmt.setString(4, content.getStatus().name());
            pstmt.setObject(5, content.getCreatedAt());
            pstmt.setObject(6, content.getUpdatedAt());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("컨텐츠 생성 실패: 저장된 행이 없습니다.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    // DB에서 받은 ID를 포함하여 객체를 복원해서 반환 (reconstruct 활용)
                    return Content.reconstruct(id, content.getSection(), content.getTitle(), content.getType(), content.getStatus(), content.getCreatedAt(), content.getUpdatedAt());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
