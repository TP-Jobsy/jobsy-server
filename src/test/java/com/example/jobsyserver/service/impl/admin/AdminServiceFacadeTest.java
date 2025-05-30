package com.example.jobsyserver.service.impl.admin;

import com.example.jobsyserver.features.admin.service.*;
import com.example.jobsyserver.features.admin.service.impl.AdminServiceImpl;
import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.portfolio.projection.PortfolioAdminListItem;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.projection.ProjectAdminListItem;
import com.example.jobsyserver.features.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AdminServiceFacadeTest {

    @Mock
    private FreelancerAdminService freelancerSvc;
    @Mock
    private ClientAdminService clientSvc;
    @Mock
    private ProjectAdminService projectSvc;
    @Mock
    private PortfolioAdminService portfolioSvc;
    @Mock
    private UserAdminService userSvc;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Pageable pageable;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(1, 5, Sort.by("createdAt").ascending());
        now = LocalDateTime.now();
    }

    @Test
    void getAllFreelancers_delegates() {
        List<FreelancerProfileDto> expected = List.of(new FreelancerProfileDto());
        when(freelancerSvc.getAll()).thenReturn(expected);
        var actual = adminService.getAllFreelancers();
        assertThat(actual).isSameAs(expected);
        verify(freelancerSvc).getAll();
    }

    @Test
    void getFreelancerById_delegates() {
        FreelancerProfileDto dto = new FreelancerProfileDto();
        when(freelancerSvc.getById(42L)).thenReturn(dto);
        assertThat(adminService.getFreelancerById(42L)).isSameAs(dto);
        verify(freelancerSvc).getById(42L);
    }

    @Test
    void activateAndDeactivateAndDeleteFreelancer_delegates() {
        adminService.activateFreelancer(11L);
        adminService.deactivateFreelancer(12L);
        adminService.deleteFreelancer(13L);
        verify(freelancerSvc).activate(11L);
        verify(freelancerSvc).deactivate(12L);
        verify(freelancerSvc).delete(13L);
    }

    @Test
    void getAllClients_andClientById_delegates() {
        List<ClientProfileDto> clients = List.of(new ClientProfileDto());
        when(clientSvc.getAll()).thenReturn(clients);
        ClientProfileDto one = new ClientProfileDto();
        when(clientSvc.getById(7L)).thenReturn(one);
        assertThat(adminService.getAllClients()).isSameAs(clients);
        assertThat(adminService.getClientById(7L)).isSameAs(one);
        verify(clientSvc).getAll();
        verify(clientSvc).getById(7L);
    }

    @Test
    void activateAndDeactivateAndDeleteClient_delegates() {
        adminService.activateClient(21L);
        adminService.deactivateClient(22L);
        adminService.deleteClient(23L);
        verify(clientSvc).activate(21L);
        verify(clientSvc).deactivate(22L);
        verify(clientSvc).delete(23L);
    }

    @Test
    void getClientProjects_delegates() {
        List<ProjectDto> list = List.of(new ProjectDto());
        when(projectSvc.getByClient(5L)).thenReturn(list);
        assertThat(adminService.getClientProjects(5L)).isSameAs(list);
        verify(projectSvc).getByClient(5L);
    }

    @Test
    void project_lookup_and_paging_and_search_delegates() {
        ProjectDto dto = new ProjectDto();
        when(projectSvc.getById(2L)).thenReturn(dto);
        ProjectAdminListItem item = mock(ProjectAdminListItem.class);
        Page<ProjectAdminListItem> page = new PageImpl<>(List.of(item), pageable, 1);
        when(projectSvc.pageAll(pageable)).thenReturn(page);
        when(projectSvc.search("foo", "OPEN", now.minusDays(1), now, pageable))
                .thenReturn(page);
        assertThat(adminService.getProjectById(2L)).isSameAs(dto);
        assertThat(adminService.getAllProjectsPageable(pageable)).isSameAs(page);
        assertThat(adminService.searchProjects("foo", "OPEN", now.minusDays(1), now, pageable))
                .isSameAs(page);
        verify(projectSvc).getById(2L);
        verify(projectSvc).pageAll(pageable);
        verify(projectSvc).search("foo", "OPEN", now.minusDays(1), now, pageable);
    }

    @Test
    void deleteProject_delegates() {
        adminService.deleteProject(3L);
        verify(projectSvc).delete(3L);
    }

    @Test
    void portfolio_lookup_and_paging_and_search_delegates() {
        FreelancerPortfolioDto dto = new FreelancerPortfolioDto();
        when(portfolioSvc.getByFreelancer(8L)).thenReturn(List.of(dto));
        when(portfolioSvc.getById(9L)).thenReturn(dto);
        PortfolioAdminListItem pi = mock(PortfolioAdminListItem.class);
        Page<PortfolioAdminListItem> page = new PageImpl<>(List.of(pi), pageable, 1);
        when(portfolioSvc.pageAll(pageable)).thenReturn(page);
        when(portfolioSvc.search("t", "john", now.minusDays(2), now, pageable))
                .thenReturn(page);
        assertThat(adminService.getFreelancerPortfolio(8L)).containsExactly(dto);
        assertThat(adminService.getPortfolioById(9L)).isSameAs(dto);
        assertThat(adminService.getAllPortfoliosPageable(pageable)).isSameAs(page);
        assertThat(adminService.searchPortfolios("t", "john", now.minusDays(2), now, pageable))
                .isSameAs(page);
        verify(portfolioSvc).getByFreelancer(8L);
        verify(portfolioSvc).getById(9L);
        verify(portfolioSvc).pageAll(pageable);
        verify(portfolioSvc).search("t", "john", now.minusDays(2), now, pageable);
    }

    @Test
    void deletePortfolio_delegates() {
        adminService.deletePortfolio(8L, 99L);
        verify(portfolioSvc).delete(8L, 99L);
    }

    @Test
    void user_search_and_user_activation_delegates() {
        UserDto dto = new UserDto();
        Page<UserDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(userSvc.search("x", null, now.minusDays(5), now, pageable)).thenReturn(page);
        assertThat(adminService.searchUsers("x", null, now.minusDays(5), now, pageable))
                .isSameAs(page);
        adminService.activateFreelancer(15L);
        adminService.deactivateFreelancer(16L);
        adminService.activateClient(17L);
        adminService.deactivateClient(18L);
        verify(userSvc).search("x", null, now.minusDays(5), now, pageable);
        verify(freelancerSvc).activate(15L);
        verify(freelancerSvc).deactivate(16L);
        verify(clientSvc).activate(17L);
        verify(clientSvc).deactivate(18L);
    }

    @Test
    void getFreelancerProjects_delegates() {
        List<ProjectDto> list = List.of(new ProjectDto());
        when(projectSvc.getByFreelancer(42L)).thenReturn(list);
        assertThat(adminService.getFreelancerProjects(42L)).isSameAs(list);
        verify(projectSvc).getByFreelancer(42L);
    }
}