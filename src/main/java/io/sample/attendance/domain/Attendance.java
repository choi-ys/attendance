package io.sample.attendance.domain;

import static io.sample.attendance.validator.TimeValidator.validateStartAndEndTime;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {
    public static final int DAILY_STATUTORY_ACTUAL_WORKING_HOUR = 8;
    public static final int MAX_BREAK_TIME_HOUR = 1;
    public static final int DAILY_STATUTORY_WORKING_HOUR = DAILY_STATUTORY_ACTUAL_WORKING_HOUR + MAX_BREAK_TIME_HOUR;
    public static final int DAILY_STATUTORY_WORKING_MINUTE = 540;
    public static final int BASIC_PAY_PER_HOUR = 10000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TimeTable timeTable;

    @Embedded
    private ExtraWorks extraWorks;
    private int basicPay;
    private int totalPay;

    private Attendance(LocalDateTime startAt, LocalDateTime endAt) {
        this.timeTable = TimeTable.of(startAt, endAt);
        this.extraWorks = ExtraWorks.from(this);
        this.basicPay = calculateBasicPay();
        this.totalPay = calculateTotalPay();
    }

    public static Attendance of(LocalDateTime startAt, LocalDateTime endAt) {
        validateStartAndEndTime(startAt, endAt);
        return new Attendance(startAt, endAt);
    }

    private int calculateBasicPay() {
        return timeTable.getWorkDuration().getHour() * BASIC_PAY_PER_HOUR;
    }

    private int calculateTotalPay() {
        return basicPay + extraWorks.getTotalExtraPay();
    }

    public WorkDuration getWorkDuration() {
        return timeTable.getWorkDuration();
    }

    public LocalDateTime getStartAt() {
        return timeTable.getStartAt();
    }

    public LocalDateTime getEndAt() {
        return timeTable.getEndAt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attendance that = (Attendance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
