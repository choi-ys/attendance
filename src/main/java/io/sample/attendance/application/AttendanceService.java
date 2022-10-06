package io.sample.attendance.application;

import io.sample.attendance.domain.Attendance;
import io.sample.attendance.dto.AttendanceDto.AttendanceRequest;
import io.sample.attendance.dto.AttendanceDto.AttendanceResponse;
import io.sample.attendance.repo.AttendanceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepo attendanceRepo;

    @Transactional
    public AttendanceResponse saveAttendance(AttendanceRequest attendanceRequest) {
        return AttendanceResponse.toResponse(attendanceRepo.save(attendanceRequest.toEntity()));
    }

    public AttendanceResponse findAttendanceResponseById(Long id) {
        return AttendanceResponse.toResponse(findAttendanceById(id));
    }

    private Attendance findAttendanceById(Long id) {
        return attendanceRepo.findById(id).orElseThrow(() -> new IllegalArgumentException(""));
    }
}
