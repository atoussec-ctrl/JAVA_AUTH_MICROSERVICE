package com.atous.auth.presentation.dto.response;

public record PageResponse<T>(java.util.List<T> content, int page, int size, long totalElements, int totalPages, boolean first, boolean last) {}
