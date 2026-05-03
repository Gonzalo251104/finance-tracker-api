package com.financetracker.domain.model;

import java.util.List;

/**
 * Domain-level page result. Framework-agnostic representation of a paginated result set.
 *
 * @param <T> the type of elements in the page
 */
public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public PageResult(List<T> content, int page, int size, long totalElements) {
        this(content, page, size, totalElements,
                size > 0 ? (int) Math.ceil((double) totalElements / size) : 0,
                page == 0,
                size > 0 ? page >= (int) Math.ceil((double) totalElements / size) - 1 : true);
    }
}
