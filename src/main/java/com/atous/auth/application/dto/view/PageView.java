package com.atous.auth.application.dto.view;

public record PageView<T>(java.util.List<T> content, int page, int size, long totalElements, int totalPages, boolean first, boolean last) {}
