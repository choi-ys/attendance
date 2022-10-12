package io.sample.attendance.repo;

import io.sample.attendance.domain.Attendance;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepo extends JpaRepository<Attendance, Long> {
    @Query(value = "select attendance from Attendance as attendance" +
        " left join fetch attendance.extraWorks as extraWorks" +
        " where attendance.timeTable.startAt >= :from and attendance.timeTable.startAt < :to",
        countQuery = "select count(attendance) from Attendance as attendance" +
            " where attendance.timeTable.startAt >= :from and attendance.timeTable.startAt < :to"
    )
    Page<Attendance> findAttendanceWithExtraWorksPageByMonthly(
        @Param(value = "from") LocalDateTime from,
        @Param(value = "to") LocalDateTime to,
        Pageable pageable
    );

    default Page<Attendance> findAttendanceWithExtraWorksPageByMonthly(LocalDate startAt, Pageable pageable) {
        LocalDateTime from = startAt.atStartOfDay();
        LocalDateTime to = startAt.plusDays(1).atStartOfDay();
        return findAttendanceWithExtraWorksPageByMonthly(from, to, pageable);
    }
}
