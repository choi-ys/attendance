package io.sample.attendance.global.response;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

@Getter
public class PageResponse<E> {
    private Integer totalPages;
    private Long totalElementCount;
    private Integer currentPage;
    private Integer currentElementCount;
    private Integer perPageNumber;

    private Boolean firstPage;
    private Boolean lastPage;
    private Boolean hasNextPage;
    private Boolean hasPrevious;
    private Sort sort;

    private List<E> elements;

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
        Sort sort,
        List<E> elements
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
        this.sort = sort;
        this.elements = elements;
    }

    public static <T> PageResponse<T> of(Page<T> page) {
        return getPageResponse(page, page.getContent());
    }

    public static <T, E> PageResponse<E> of(Page<T> page, List<E> elements) {
        return getPageResponse(page, elements);
    }

    private static <T, E> PageResponse<E> getPageResponse(Page<T> page, List<E> elements) {
        return new PageResponse<>(
            page.getTotalPages(),
            page.getTotalElements(),
            indexToNumber(page),
            page.getNumberOfElements(),
            page.getSize(),
            page.isFirst(),
            page.isLast(),
            page.hasNext(),
            page.hasPrevious(),
            page.getSort(),
            elements
        );
    }

    private static <T> int indexToNumber(Page<T> page) {
        return page.getNumber() + 1;
    }
}
