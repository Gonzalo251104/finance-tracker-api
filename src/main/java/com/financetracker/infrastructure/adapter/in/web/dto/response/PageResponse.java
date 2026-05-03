package com.financetracker.infrastructure.adapter.in.web.dto.response;

import com.financetracker.domain.model.PageResult;

import java.util.List;
import java.util.function.Function;

/**
 * Generic paginated response DTO for REST API responses.
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <D, R> PageResponse<R> from(PageResult<D> pageResult, Function<D, R> mapper) {
        List<R> content = pageResult.content().stream().map(mapper).toList();
        return new PageResponse<>(
                content,
                pageResult.page(),
                pageResult.size(),
                pageResult.totalElements(),
                pageResult.totalPages(),
                pageResult.first(),
                pageResult.last()
        );
    }
}
