package io.sample.attendance.repo;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import io.sample.attendance.domain.Attendance;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepo extends JpaRepository<Attendance, Long> {
    int DAY_OF_FIRST = 1;

    @Query(value = "select attendance from Attendance as attendance" +
        " where attendance.timeTable.startAt >= :from and attendance.timeTable.startAt < :to",
        countQuery = "select count(attendance) from Attendance as attendance" +
            " where attendance.timeTable.startAt >= :from and attendance.timeTable.startAt < :to"
    )
    Page<Attendance> findAttendanceWithExtraWorksPageByMonthly(
        @Param(value = "from") LocalDateTime from,
        @Param(value = "to") LocalDateTime to,
        Pageable pageable
    );

    default Page<Attendance> findAttendanceWithExtraWorksPageByMonthly(YearMonth startAt, Pageable pageable) {
        LocalDate firstDayOfYearMonth = startAt.atDay(DAY_OF_FIRST);
        LocalDateTime from = firstDayOfYearMonth.atStartOfDay();
        LocalDateTime to = LocalDateTime.of(firstDayOfYearMonth.with(lastDayOfMonth()), LocalTime.MAX);
        return findAttendanceWithExtraWorksPageByMonthly(from, to, pageable);
    }

    @Override
    @EntityGraph(attributePaths = "extraWorks.elements")
    Optional<Attendance> findById(@Param(value = "id") Long id);

    Optional<Attendance> findAttendanceById(@Param(value = "id") Long id);
}
