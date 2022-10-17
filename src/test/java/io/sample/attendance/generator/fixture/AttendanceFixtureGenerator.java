package io.sample.attendance.generator.fixture;

import static org.hibernate.type.IntegerType.ZERO;

import io.sample.attendance.model.domain.Attendance;
import io.sample.attendance.model.dto.AttendanceDto.AttendanceRequest;
import io.sample.attendance.model.domain.repo.AttendanceRepo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class AttendanceFixtureGenerator {
    private static final LocalDate today = LocalDate.now();
    private static final LocalDate yesterday = today.minusDays(1);
    private static final LocalDate nextDay = today.plusDays(1);

    private final AttendanceRepo attendanceRepo;

    public AttendanceFixtureGenerator(AttendanceRepo attendanceRepo) {
        this.attendanceRepo = attendanceRepo;
    }

    public static Attendance 추가_근무가_없는_출결_생성() {
        final LocalDateTime startAt = LocalDateTime.of(yesterday, LocalTime.of(9, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(yesterday, LocalTime.of(18, ZERO));
        return Attendance.of(startAt, endAt);
    }

    public static Attendance 연장근무가_포함된_출결_생성() {
        final LocalDateTime startAt = LocalDateTime.of(yesterday, LocalTime.of(9, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(yesterday, LocalTime.of(22, ZERO));
        return Attendance.of(startAt, endAt);
    }

    public static Attendance 야간근무가_포함된_출결_생성() {
        final LocalDateTime startAt = LocalDateTime.of(yesterday, LocalTime.of(9, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(yesterday, LocalTime.of(23, 30));
        return Attendance.of(startAt, endAt);
    }

    public static Attendance 연장근무와_야간근무가_포함된_출결_생성() {
        final LocalDateTime startAt = LocalDateTime.of(yesterday, LocalTime.of(5, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(1, ZERO));
        return Attendance.of(startAt, endAt);
    }

    public static AttendanceRequest 추가_근무가_없는_출결_요청_생성() {
        Attendance 추가_근무가_없는_근무 = 추가_근무가_없는_출결_생성();
        return AttendanceRequest.of(추가_근무가_없는_근무.getStartAt(), 추가_근무가_없는_근무.getEndAt());
    }

    public static AttendanceRequest 연장근무가_포함된_출결_요청_생성() {
        Attendance 연장근무가_포함된_근무 = 연장근무가_포함된_출결_생성();
        return AttendanceRequest.of(연장근무가_포함된_근무.getStartAt(), 연장근무가_포함된_근무.getEndAt());
    }

    public static AttendanceRequest 야간근무가_포함된_출결_요청_생성() {
        Attendance 야간근무가_포함된_근무 = 야간근무가_포함된_출결_생성();
        return AttendanceRequest.of(야간근무가_포함된_근무.getStartAt(), 야간근무가_포함된_근무.getEndAt());
    }

    public static AttendanceRequest 연장근무와_야간근무가_포함된_출결_요청_생성() {
        Attendance 연장근무와_야간근무가_포함된_근무 = 연장근무와_야간근무가_포함된_출결_생성();
        return AttendanceRequest.of(연장근무와_야간근무가_포함된_근무.getStartAt(), 연장근무와_야간근무가_포함된_근무.getEndAt());
    }

    public Attendance 추가_근무가_없는_출결_등록() {
        return 출결_등록(추가_근무가_없는_출결_생성());
    }

    public Attendance 연장근무가_포함된_출결_등록() {
        return 출결_등록(연장근무가_포함된_출결_생성());
    }

    public Attendance 야간근무가_포함된_출결_등록() {
        return 출결_등록(야간근무가_포함된_출결_생성());
    }

    public Attendance 연장근무와_야간근무가_포함된_출결_등록() {
        return 출결_등록(연장근무와_야간근무가_포함된_출결_생성());
    }

    public void 출결_목록_생성(int size) {
        for (int i = 0; i < size; i++) {
            int remainder = i % 4;
            switch (remainder) {
                case 0:
                    추가_근무가_없는_출결_등록();
                    break;
                case 1:
                    연장근무가_포함된_출결_등록();
                    break;
                case 2:
                    야간근무가_포함된_출결_등록();
                    break;
                case 3:
                    연장근무와_야간근무가_포함된_출결_등록();
                    break;
            }
        }
    }

    private Attendance 출결_등록(Attendance attendance) {
        return attendanceRepo.saveAndFlush(attendance);
    }
}
