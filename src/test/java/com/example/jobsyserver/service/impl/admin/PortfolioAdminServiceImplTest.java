package com.example.jobsyserver.service.impl.admin;

import com.example.jobsyserver.features.admin.service.impl.PortfolioAdminServiceImpl;
import com.example.jobsyserver.features.portfolio.projection.PortfolioAdminListItem;
import com.example.jobsyserver.features.portfolio.repository.FreelancerPortfolioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioAdminServiceImplTest {

    @Mock private FreelancerPortfolioRepository repo;
    @InjectMocks private PortfolioAdminServiceImpl service;

    @Test
    void pageAll_ShouldReturnProjected() {
        PortfolioAdminListItem item = mock(PortfolioAdminListItem.class);
        Page<PortfolioAdminListItem> page = new PageImpl<>(List.of(item));
        when(repo.findAllProjected(any(), any(Pageable.class))).thenReturn(page);
        var out = service.pageAll(PageRequest.of(0, 3));
        assertThat(out.getContent()).containsExactly(item);
    }

    @Test
    void search_ShouldReturnProjected() {
        PortfolioAdminListItem item = mock(PortfolioAdminListItem.class);
        Page<PortfolioAdminListItem> page = new PageImpl<>(List.of(item));
        when(repo.findAllProjected(any(), any(Pageable.class))).thenReturn(page);
        var out = service.search("t", "john",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                PageRequest.of(0,3));
        assertThat(out.getContent()).containsExactly(item);
    }
}