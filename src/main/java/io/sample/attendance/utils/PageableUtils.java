package io.sample.attendance.utils;

import java.util.Objects;
import org.springframework.data.domain.Pageable;

public class PageableUtils {
    public static Pageable pageNumberToIndex(Pageable pageable) {
        if (isNullPageable(pageable)) {
            throw new IllegalArgumentException();
        }

        if (isNotZeroPageNumber(pageable.getPageNumber())) {
            return withMinusPageNumber(pageable);
        }
        return pageable;
    }

    private static boolean isNullPageable(Pageable pageable) {
        return Objects.isNull(pageable);
    }

    private static boolean isZeroPageNumber(int pageNumber) {
        return pageNumber == 0;
    }

    private static boolean isNotZeroPageNumber(int pageNumber) {
        return !isZeroPageNumber(pageNumber);
    }

    private static Pageable withMinusPageNumber(Pageable pageable) {
        return pageable.withPage(pageable.getPageNumber() - 1);
    }
}
