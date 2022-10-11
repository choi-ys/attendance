package io.sample.attendance.repo;

import io.sample.attendance.domain.Attendance;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepo extends JpaRepository<Attendance, Long> {

    @Query(value = "select attendance from Attendance as attendance" +
        " left join fetch attendance.extraWorks as extraWorks" +
        " where attendance.createdAt >= :from and attendance.createdAt < :to",
        countQuery = "select count(attendance) from Attendance as attendance" +
            " where attendance.createdAt >= :from and attendance.createdAt < :to"
    )
    Page<Attendance> findAttendanceWithExtraWorksPageList(
        @Param(value = "from") LocalDateTime from,
        @Param(value = "to") LocalDateTime to,
        PageRequest pageRequest
    );

    default Page<Attendance> findAttendanceWithExtraWorksPageList(LocalDate createdAt, PageRequest pageRequest) {
        return findAttendanceWithExtraWorksPageList(createdAt.atStartOfDay(), createdAt.plusDays(1).atStartOfDay(), pageRequest);
    }
}
