package io.sample.attendance.global.response;

import java.util.List;
import org.springframework.data.domain.Page;

public class PageResponse {
    private Integer totalPages;
    private Long totalElementCount;
    private Integer currentPage;
    private Integer currentElementCount;
    private Integer perPageNumber;

    private Boolean firstPage;
    private Boolean lastPage;
    private Boolean hasNextPage;
    private Boolean hasPrevious;

    private List<?> elements;

    private PageResponse(
        Integer totalPages,
        Long totalElementCount,
        Integer currentPage,
        Integer currentElementCount,
        Integer perPageNumber,
        Boolean firstPage,
        Boolean lastPage,
        Boolean hasNextPage,
        Boolean hasPrevious,
        List<?> elements
    ) {
        this.totalPages = totalPages;
        this.totalElementCount = totalElementCount;
        this.currentPage = currentPage;
        this.currentElementCount = currentElementCount;
        this.perPageNumber = perPageNumber;
        this.firstPage = firstPage;
        this.lastPage = lastPage;
        this.hasNextPage = hasNextPage;
        this.hasPrevious = hasPrevious;
        this.elements = elements;
    }

    public static PageResponse of(Page<?> page) {
        return getPageResponse(page, page.getContent());
    }

    public static PageResponse of(Page<?> page, List<?> elements) {
        return getPageResponse(page, elements);
    }

    private static PageResponse getPageResponse(Page<?> page, List<?> elements) {
        return new PageResponse(
            page.getTotalPages(),
            page.getTotalElements(),
            indexToNumber(page),
            page.getNumberOfElements(),
            page.getSize(),
            page.isFirst(),
            page.isLast(),
            page.hasNext(),
            page.hasPrevious(),
            elements
        );
    }

    private static <T> int indexToNumber(Page<T> page) {
        return page.getNumber() + 1;
    }
}
