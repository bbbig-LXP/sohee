package com.lxp.sohee;

import com.lxp.sohee.config.JDBCConnection;
import com.lxp.sohee.course.controller.CourseController;
import com.lxp.sohee.course.infrastructure.JdbcCourseRepository;
import com.lxp.sohee.course.model.CourseLevel;
import com.lxp.sohee.course.model.CourseRepository;
import com.lxp.sohee.course.service.CourseService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        try (Connection connection = JDBCConnection.getConnection();
                Scanner sc = new Scanner(System.in);) {
            System.out.println("연결 성공: " + connection);

            CourseRepository repository = new JdbcCourseRepository(connection);
            CourseService service = new CourseService(repository);
            CourseController controller = new CourseController(service);

            System.out.println("===== 강좌 관리 시스템 =====");

            while(true) {
                System.out.println("1. 강좌 등록 | 2. 강좌 목록 조회 | 3. 강좌 상세 조회 | exit. 종료");
                System.out.print("입력: ");
                String menu = sc.nextLine();

                if (menu.equals("1")) {
                    try {
                        System.out.print("강좌 제목: ");
                        String title = sc.nextLine();

                        System.out.print("강좌 설명: ");
                        String description = sc.nextLine();

                        System.out.print("강사 ID: ");
                        Long instructorId = Long.parseLong(sc.nextLine());

                        System.out.print("난이도 (BEGINNER, INTERMEDIATE, ADVANCED): ");
                        CourseLevel level = CourseLevel.valueOf(sc.nextLine().toUpperCase().trim());

                        controller.addCourse(title, description, instructorId, level);
                    } catch (Exception e) {
                        System.out.println("잘못된 입력입니다.");
                    }
                } else if (menu.equals("2")) {
                    controller.list();
                } else if (menu.equals("3")) {
                    try {
                        System.out.print("조회할 강의 ID를 입력하세요: ");
                        Long id = Long.parseLong(sc.nextLine());
                        controller.detail(id);
                    } catch (NumberFormatException e) {
                        System.out.println("ID는 숫자만 입력 가능합니다.");
                    }
                } else if (menu.equals("exit")) {
                    System.out.println("프로그램을 종료합니다.");
                    break;
                } else {
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
