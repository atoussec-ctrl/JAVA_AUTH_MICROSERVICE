package com.atous.auth.application.port.in;

import com.atous.auth.application.dto.command.SearchUsersQuery;
import com.atous.auth.application.dto.view.PageView;
import com.atous.auth.application.dto.view.UserView;

public interface SearchUsersUseCase { PageView<UserView> execute(SearchUsersQuery query); }
