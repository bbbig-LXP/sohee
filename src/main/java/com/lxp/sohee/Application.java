package com.lxp.sohee;

import com.lxp.sohee.config.JDBCConnection;
import com.lxp.sohee.course.controller.CourseController;
import com.lxp.sohee.course.infrastructure.JdbcContentRepository;
import com.lxp.sohee.course.infrastructure.JdbcCourseRepository;
import com.lxp.sohee.course.infrastructure.JdbcCourseSectionRepository;
import com.lxp.sohee.course.model.ContentRepository;
import com.lxp.sohee.course.model.ContentStatus;
import com.lxp.sohee.course.model.ContentType;
import com.lxp.sohee.course.model.CourseLevel;
import com.lxp.sohee.course.model.CourseRepository;
import com.lxp.sohee.course.model.CourseSectionRepository;
import com.lxp.sohee.course.service.CourseService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Application {
    private static final Scanner sc = new Scanner(System.in);
    private static CourseController controller;

    public static void main(String[] args) {
        try (Connection connection = JDBCConnection.getConnection()) {
            System.out.println("연결 성공: " + connection);
            initDependencies(connection);
            runMenuLoop();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            sc.close();
        }
    }

    private static void initDependencies(Connection connection) {
        CourseRepository repository = new JdbcCourseRepository(connection);
        ContentRepository contentRepository = new JdbcContentRepository(connection);
        CourseSectionRepository sectionRepository = new JdbcCourseSectionRepository(connection);
        CourseService service = new CourseService(repository, sectionRepository, contentRepository);
        controller = new CourseController(service);
    }

    private static void runMenuLoop() {
        System.out.println("===== 강좌 관리 시스템 =====");
        while (true) {
            System.out.println("\n1. 강좌 등록 | 2. 강좌 목록 조회 | 3. 강좌 상세 조회 | 4. 강좌 수정 | 5. 강좌 숨김 | exit. 종료");
            System.out.print("선택: ");
            String menu = sc.nextLine();

            if (menu.equalsIgnoreCase("exit")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            try {
                switch (menu) {
                    case "1" -> handleAddCourse();
                    case "2" -> controller.getCourses();
                    case "3" -> handleGetCourse();
                    case "4" -> handleUpdateCourse();
                    case "5" -> handleDeleteCourse();
                    default -> System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                }
            } catch (Exception e) {
                System.out.println("오류 발생: " + e.getMessage());
            }
        }
    }

    // 기능별 상세 메서드
    private static void handleAddCourse() {
        System.out.print("강좌 제목: ");
        String title = sc.nextLine();

        System.out.print("강좌 설명: ");
        String description = sc.nextLine();

        System.out.print("강사 ID: ");
        Long instructorId = Long.parseLong(sc.nextLine());

        System.out.print("난이도 (BEGINNER, INTERMEDIATE, ADVANCED): ");
        CourseLevel level = CourseLevel.valueOf(sc.nextLine().toUpperCase().trim());

        controller.addCourse(title, description, instructorId, level);
    }

    private static void handleGetCourse() {
        System.out.print("조회할 강의 ID를 입력하세요: ");
        Long id = Long.parseLong(sc.nextLine());
        controller.getCourse(id);

        System.out.println("\n[ 추가 작업: 1. 섹션 등록 | 2. 콘텐츠 등록 | Enter. 메뉴로 돌아가기 ]");
        System.out.print("선택: ");
        String subMenu = sc.nextLine();

        switch (subMenu) {
            case "1" -> handleAddSection(id);
            case "2" -> handleAddContent();
        }
    }

    private static void handleUpdateCourse() {
        System.out.print("수정할 강의 ID를 입력하세요: ");
        Long id = Long.parseLong(sc.nextLine());

        controller.getCourse(id);

        System.out.println("새로운 제목 (변경 없으면 엔터): ");
        String newTitle = sc.nextLine();

        System.out.println("새로운 설명 (변경 없으면 엔터): ");
        String newDescription = sc.nextLine();

        controller.update(id, newTitle, newDescription);
    }

    private static void handleDeleteCourse() {
        System.out.print("보관(삭제)할 강의 ID를 입력하세요: ");
        Long id = Long.parseLong(sc.nextLine());

        System.out.print("정말로 삭제하시겠습니까? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            controller.delete(id);
        } else {
            System.out.println("삭제가 취소되었습니다.");
        }
    }

    private static void handleAddSection(Long courseId) {
        System.out.print("새로운 섹션 제목 (2~50자): ");
        String title = sc.nextLine();

        controller.addSection(courseId, title);
    }

    private static void handleAddContent() {
        System.out.print("콘텐츠를 추가할 섹션 ID: ");
        Long sectionId = Long.parseLong(sc.nextLine());

        System.out.print("콘텐츠 제목 (2~50자): ");
        String title = sc.nextLine();

        System.out.print("콘텐츠 타입 (VIDEO, DOCUMENT): ");
        ContentType type = ContentType.valueOf(sc.nextLine().toUpperCase().trim());

        System.out.print("초기 상태 (NORMAL, HIDDEN): ");
        ContentStatus status = ContentStatus.valueOf(sc.nextLine().toUpperCase().trim());

        controller.addContent(sectionId, title, type, status);
    }
}
