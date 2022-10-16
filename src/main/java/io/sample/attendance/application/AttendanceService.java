package io.sample.attendance.application;

import io.sample.attendance.model.domain.Attendance;
import io.sample.attendance.model.dto.AttendanceDto.AttendanceRequest;
import io.sample.attendance.model.dto.AttendanceDto.AttendanceResponse;
import io.sample.attendance.model.dto.AttendanceDto.MonthlyAttendanceRequest;
import io.sample.global.exception.ResourceNotFoundException;
import io.sample.global.response.ErrorCode;
import io.sample.global.response.PageResponse;
import io.sample.attendance.model.domain.repo.AttendanceRepo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepo attendanceRepo;

    @Transactional
    public AttendanceResponse saveAttendance(AttendanceRequest attendanceRequest) {
        return AttendanceResponse.of(attendanceRepo.save(attendanceRequest.toEntity()));
    }

    public AttendanceResponse findAttendanceResponseById(Long id) {
        return AttendanceResponse.of(findAttendanceById(id));
    }

    private Attendance findAttendanceById(Long id) {
        return attendanceRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, id));
    }

    public PageResponse<AttendanceResponse> findAttendanceResponsesByMonthly(MonthlyAttendanceRequest monthlyAttendanceRequest) {
        Page<Attendance> attendances = attendanceRepo.findAttendanceWithExtraWorksPageByMonthly(
            monthlyAttendanceRequest.getStartAt(),
            monthlyAttendanceRequest.getPageable()
        );
        List<AttendanceResponse> elements = attendances.stream()
            .map(AttendanceResponse::of)
            .collect(Collectors.toList());
        return PageResponse.of(attendances, elements);
    }
}
