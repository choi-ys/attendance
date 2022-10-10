package io.sample.attendance.repo;

import static org.assertj.core.api.Assertions.assertThat;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.domain.ExtraWorkType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@DataJpaTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@DisplayName("Repo:Attendance")
class AttendanceRepoTest {
    private final AttendanceRepo attendanceRepo;
    private final EntityManager entityManager;

    public AttendanceRepoTest(AttendanceRepo attendanceRepo, EntityManager entityManager) {
        this.attendanceRepo = attendanceRepo;
        this.entityManager = entityManager;
    }

    private Attendance given;

    @BeforeEach
    void setUp() {
        final LocalDate today = LocalDate.now();
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(9, 0));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(18, 0));

        given = Attendance.of(startAt, endAt);
    }

    @Test
    @DisplayName("추가 근무가 없는 일일 근태 기록 저장")
    public void save() {
        // When
        Attendance actual = attendanceRepo.save(given);

        // Then
        assertThat(actual).isSameAs(given);
    }

    @Test
    @DisplayName("추가 근무가 있는 일일 근태 기록 저장")
    public void save_existExtraWorks() {
        // When
        final LocalDate today = LocalDate.now();
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(5, 0));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(23, 0));
        Attendance given = Attendance.of(startAt, endAt);
        Attendance actual = attendanceRepo.save(given);

        // Then
        assertThat(actual).isSameAs(given);
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("일일 근태 기록 조회")
    public void findById() {
        // Given
        attendanceRepo.saveAndFlush(given);
        entityManager.clear();

        // When
        Attendance actual = attendanceRepo.findById(given.getId()).orElseThrow();

        // Then
        assertThat(actual).isEqualTo(given);
    }

    @Test
    @DisplayName("추가 근무가 있는 일일 근태 기록 조회 시, 일일 근태 기록만 조회")
    public void findById_existExtraWorks() {
        // When
        final LocalDate today = LocalDate.now();
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(5, 0));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(23, 0));
        Attendance given = attendanceRepo.saveAndFlush(Attendance.of(startAt, endAt));
        entityManager.clear();

        Attendance actual = attendanceRepo.findById(given.getId()).orElseThrow();

        // Then
        assertThat(actual).isEqualTo(given);
    }

    @Test
    @DisplayName("일일 근태 기록 조회 시, Lazy laoding을 이용한 추가 근무 조회")
    public void findByIdWithExtraWorks() {
        // Given
        final LocalDate today = LocalDate.now();
        final LocalDateTime startAt = LocalDateTime.of(today, LocalTime.of(5, 0));
        final LocalDateTime endAt = LocalDateTime.of(today, LocalTime.of(23, 0));
        Attendance given = attendanceRepo.saveAndFlush(Attendance.of(startAt, endAt));
        entityManager.clear();

        // When
        Attendance actual = attendanceRepo.findById(given.getId()).orElseThrow();

        // Then
        assertThat(actual.getExtraWorks())
            .satisfies(extraWorks -> {
                assertThat(extraWorks.getExtraWorkTypes()).containsOnly(ExtraWorkType.NIGHT_SHIFT, ExtraWorkType.OVERTIME);
                assertThat(extraWorks.getSize()).isEqualTo(3);
            });
    }
}
