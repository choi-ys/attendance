package io.sample.attendance.generator.fixture;

import static org.hibernate.type.IntegerType.ZERO;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.dto.AttendanceDto.AttendanceRequest;
import io.sample.attendance.repo.AttendanceRepo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class AttendanceFixtureGenerator {
    private static final LocalDate today = LocalDate.now();
    private static final LocalDate nextDay = today.plusDays(1);

    private final AttendanceRepo attendanceRepo;

    public AttendanceFixtureGenerator(AttendanceRepo attendanceRepo) {
        this.attendanceRepo = attendanceRepo;
    }

    public static Attendance 추가_근무가_없는_근무() {
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(18, ZERO));
        return Attendance.of(startAt, endAt);
    }

    public static Attendance 연장근무가_포함된_근무() {
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(22, ZERO));
        return Attendance.of(startAt, endAt);
    }

    public static Attendance 야간근무가_포함된_근무() {
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(23, 30));
        return Attendance.of(startAt, endAt);
    }

    public static Attendance 연장근무와_야간근무가_포함된_근무() {
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(5, ZERO));
        final LocalDateTime endAt = LocalDateTime.of(nextDay, LocalTime.of(1, ZERO));
        return Attendance.of(startAt, endAt);
    }

    public static AttendanceRequest 추가_근무가_없는_근무_생성_요청() {
        Attendance 추가_근무가_없는_근무 = 추가_근무가_없는_근무();
        return AttendanceRequest.of(추가_근무가_없는_근무.getStartAt(), 추가_근무가_없는_근무.getEndAt());
    }

    public static AttendanceRequest 연장근무가_포함된_근무_생성_요청() {
        Attendance 연장근무가_포함된_근무 = 연장근무가_포함된_근무();
        return AttendanceRequest.of(연장근무가_포함된_근무.getStartAt(), 연장근무가_포함된_근무.getEndAt());
    }

    public static AttendanceRequest 야간근무가_포함된_근무_생성_요청() {
        Attendance 야간근무가_포함된_근무 = 야간근무가_포함된_근무();
        return AttendanceRequest.of(야간근무가_포함된_근무.getStartAt(), 야간근무가_포함된_근무.getEndAt());
    }

    public static AttendanceRequest 연장근무와_야간근무가_포함된_근무_생성_요청() {
        Attendance 연장근무와_야간근무가_포함된_근무 = 연장근무와_야간근무가_포함된_근무();
        return AttendanceRequest.of(연장근무와_야간근무가_포함된_근무.getStartAt(), 연장근무와_야간근무가_포함된_근무.getEndAt());
    }

    public Attendance 추가_근무가_없는_근무_등록() {
        return 근무_저장(추가_근무가_없는_근무());
    }

    public Attendance 연장근무가_포함된_근무_등록() {
        return 근무_저장(연장근무가_포함된_근무());
    }

    public Attendance 야간근무가_포함된_근무_등록() {
        return 근무_저장(야간근무가_포함된_근무());
    }

    public Attendance 연장근무와_야간근무가_포함된_근무_등록() {
        return 근무_저장(연장근무와_야간근무가_포함된_근무());
    }

    public void 근무_목록_생성(int size) {
        for (int i = 0; i < size; i++) {
            int remainder = i % 4;
            switch (remainder) {
                case 0:
                    추가_근무가_없는_근무_등록();
                    break;
                case 1:
                    연장근무가_포함된_근무_등록();
                    break;
                case 2:
                    야간근무가_포함된_근무_등록();
                    break;
                case 3:
                    연장근무와_야간근무가_포함된_근무_등록();
                    break;
            }
        }
    }

    private Attendance 근무_저장(Attendance attendance) {
        return attendanceRepo.saveAndFlush(attendance);
    }
}
