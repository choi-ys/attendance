package io.sample.attendance.repo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration;
import io.sample.attendance.config.p6spy.P6spyLogMessageFormatConfiguration;
import io.sample.attendance.domain.Attendance;
import io.sample.attendance.domain.ExtraWorkType;
import io.sample.attendance.fixture.AttendanceFixtureGenerator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@DataJpaTest(showSql = false)
@TestConstructor(autowireMode = AutowireMode.ALL)
@ImportAutoConfiguration(DataSourceDecoratorAutoConfiguration.class)
@Import({AttendanceFixtureGenerator.class, P6spyLogMessageFormatConfiguration.class})
@DisplayName("Repo:Attendance")
class AttendanceRepoTest {
    private final AttendanceRepo attendanceRepo;
    private final EntityManager entityManager;
    private final AttendanceFixtureGenerator attendanceFixtureGenerator;

    public AttendanceRepoTest(
        AttendanceRepo attendanceRepo,
        EntityManager entityManager,
        AttendanceFixtureGenerator attendanceFixtureGenerator
    ) {
        this.attendanceRepo = attendanceRepo;
        this.entityManager = entityManager;
        this.attendanceFixtureGenerator = attendanceFixtureGenerator;
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

    @Test
    @DisplayName("월별 근태 목록 조회")
    public void findByAttendanceAndExtraWorksPageByCreatedAt() {
        // Given
        final int requestSize = 5;
        attendanceFixtureGenerator.근무_목록_생성(requestSize);
        entityManager.clear();

        final YearMonth yearMonth = YearMonth.from(LocalDate.now()) ;
        final int requestPage = 0;
        final int perPageNum = 10;
        final String sortProperties = "createdAt";
        final Sort orderBy = Sort.by(Sort.Direction.DESC, sortProperties);
        final PageRequest pageRequest = PageRequest.of(requestPage, perPageNum, orderBy);

        // When
        Page<Attendance> actual = attendanceRepo.findAttendanceWithExtraWorksPageByMonthly(yearMonth, pageRequest);

        // Then
        assertAll(
            () -> assertThat(actual.getTotalPages()).as("전체 페이지 수").isEqualTo(1),
            () -> assertThat(actual.getContent()).as("조회된 페이지의 컨텐츠 수").hasSize(requestSize),
            () -> assertThat(actual.getPageable())
                .satisfies(pageable -> {
                    assertThat(pageable.getPageNumber()).as("현재 페이지 번호").isEqualTo(requestPage);
                    assertThat(pageable.getPageSize()).as("페이지당 컨텐츠 수").isEqualTo(perPageNum);
                    assertThat(pageable.getSort()).as("정렬 기준").isEqualTo(orderBy);
                })
        );
    }
}
