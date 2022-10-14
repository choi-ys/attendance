package io.sample.attendance.repo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration;
import io.sample.attendance.config.p6spy.P6spyLogMessageFormatConfiguration;
import io.sample.attendance.domain.Attendance;
import io.sample.attendance.generator.fixture.AttendanceFixtureGenerator;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.persistence.EntityManager;
import org.hibernate.Hibernate;
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

    @Test
    @DisplayName("추가 근무가 없는 일일 근태 기록 저장")
    public void save() {
        // Given
        Attendance given = AttendanceFixtureGenerator.추가_근무가_없는_근무();

        // When
        Attendance actual = attendanceRepo.save(given);

        // Then
        assertAll(
            () -> assertThat(actual).isSameAs(given),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("추가 근무가 있는 일일 근태 기록 저장")
    public void save_existExtraWorks() {
        // When
        Attendance given = attendanceFixtureGenerator.연장근무와_야간근무가_포함된_근무_등록();
        Attendance actual = attendanceRepo.save(given);

        // Then
        assertAll(
            () -> assertThat(actual).isSameAs(given),
            () -> assertThat(actual.getExtraWorks()).satisfies(extraWorks -> {
                assertThat(extraWorks.getSize()).isEqualTo(given.getExtraWorks().getSize());
                assertThat(extraWorks.getExtraWorkTypes()).containsExactlyElementsOf(given.getExtraWorks().getExtraWorkTypes());
            })
        );
    }

    @Test
    @DisplayName("일일 근태 기록 조회 시 추가 근무와 함께 조회")
    public void findById() {
        // Given
        Attendance given = attendanceFixtureGenerator.연장근무와_야간근무가_포함된_근무_등록();
        entityManager.clear();

        // When
        Attendance actual = attendanceRepo.findById(given.getId()).orElseThrow();

        // Then
        assertAll(
            () -> assertThat(actual).isEqualTo(given),
            () -> assertThat(Hibernate.isInitialized(actual.getExtraWorks().getElements()))
                .as("@EntityGraph를 통해 LAZY로 설정된 연관객체의 즉시 조회 여부")
                .isTrue(),
            () -> assertThat(actual.getExtraWorks()).satisfies(extraWorks -> {
                assertThat(extraWorks.getSize()).isEqualTo(given.getExtraWorks().getSize());
                assertThat(extraWorks.getExtraWorkTypes()).containsExactlyElementsOf(given.getExtraWorks().getExtraWorkTypes());
            })
        );
    }

    @Test
    @DisplayName("추가 근무가 있는 일일 근태 기록 조회 시, 일일 근태 기록만 조회")
    public void findById_existExtraWorks() {
        // When
        Attendance given = attendanceFixtureGenerator.연장근무와_야간근무가_포함된_근무_등록();
        entityManager.clear();

        Attendance actual = attendanceRepo.findAttendanceById(given.getId()).orElseThrow();

        // Then
        assertAll(
            () -> assertThat(actual).isEqualTo(given),
            () -> assertThat(Hibernate.isInitialized(actual.getExtraWorks().getElements()))
                .as("LAZY로 설정된 연관객체의 Proxy 객체 여부")
                .isFalse()
        );
    }

    @Test
    @DisplayName("월별 근태 목록 조회")
    public void findByAttendanceAndExtraWorksPageByCreatedAt() {
        // Given
        final int requestSize = 5;
        attendanceFixtureGenerator.근무_목록_생성(requestSize);
        entityManager.clear();

        final YearMonth yearMonth = YearMonth.from(LocalDate.now());
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
