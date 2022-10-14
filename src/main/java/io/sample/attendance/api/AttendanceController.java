package io.sample.attendance.api;

import io.sample.attendance.application.AttendanceService;
import io.sample.attendance.dto.AttendanceDto.AttendanceRequest;
import io.sample.attendance.dto.AttendanceDto.AttendanceResponse;
import io.sample.attendance.dto.AttendanceDto.MonthlyAttendanceRequest;
import io.sample.attendance.global.response.PageResponse;
import java.net.URI;
import java.time.YearMonth;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "attendance",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class AttendanceController {
    public static final String ATTENDANCE_BASE_URL = "/attendance";
    public static final String ATTENDANCE_BASE_URI_FORMAT = ATTENDANCE_BASE_URL.concat("/%s");
    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponse> registerAttendance(@Valid @RequestBody AttendanceRequest attendanceRequest) {
        AttendanceResponse attendanceResponse = attendanceService.saveAttendance(attendanceRequest);
        return ResponseEntity.created(uriFormatter(attendanceResponse.getId())).body(attendanceResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<AttendanceResponse> findAttendanceById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.findAttendanceResponseById(id));
    }

    @GetMapping("/monthly")
    public ResponseEntity<PageResponse<AttendanceResponse>> findAttendancesMonthly(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth,
        @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(attendanceService.findAttendanceResponsesByMonthly(MonthlyAttendanceRequest.of(yearMonth, pageable)));
    }

    private <T> URI uriFormatter(T path) {
        return URI.create(String.format(ATTENDANCE_BASE_URI_FORMAT, path));
    }
}
