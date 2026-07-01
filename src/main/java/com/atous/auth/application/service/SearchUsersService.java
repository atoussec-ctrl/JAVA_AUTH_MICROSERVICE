package com.atous.auth.application.service;

import com.atous.auth.application.dto.command.SearchUsersQuery; import com.atous.auth.application.dto.view.*; import com.atous.auth.application.mapper.UserApplicationMapper; import com.atous.auth.application.port.in.SearchUsersUseCase; import com.atous.auth.application.port.out.UserRepositoryPort;

public final class SearchUsersService implements SearchUsersUseCase { private final UserRepositoryPort users; private final UserApplicationMapper mapper; public SearchUsersService(UserRepositoryPort users, UserApplicationMapper mapper){this.users=users;this.mapper=mapper;} public PageView<UserView> execute(SearchUsersQuery q){var p=users.search(q.search(),q.enabled(),q.page(),q.size()); return new PageView<>(p.content().stream().map(mapper::toView).toList(),p.page(),p.size(),p.totalElements(),p.totalPages(),p.first(),p.last());} }
