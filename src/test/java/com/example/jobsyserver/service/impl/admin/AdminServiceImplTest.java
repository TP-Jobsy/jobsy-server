package com.example.jobsyserver.service.impl.admin;

import com.example.jobsyserver.features.admin.service.impl.AdminServiceImpl;
import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.client.mapper.ClientProfileMapper;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.portfolio.mapper.FreelancerPortfolioMapper;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.portfolio.repository.FreelancerPortfolioRepository;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.mapper.ProjectMapper;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private FreelancerPortfolioRepository portfolioRepository;
    @Mock
    private FreelancerProfileRepository freelancerProfileRepository;
    @Mock
    private FreelancerProfileMapper freelancerProfileMapper;
    @Mock
    private ClientProfileRepository clientProfileRepository;
    @Mock
    private ClientProfileMapper clientProfileMapper;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private FreelancerPortfolioMapper portfolioMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Long testUserId;
    private User testFreelancer;
    private User testClient;
    private FreelancerProfile testFreelancerProfile;
    private ClientProfile testClientProfile;
    private Project testProject;
    private FreelancerPortfolio testPortfolio;

    @BeforeEach
    void setup(){
        testUserId = 1L;
        testFreelancer = User.builder()
                .id(testUserId)
                .role(UserRole.FREELANCER)
                .isActive(true)
                .build();
        testClient = User.builder()
                .id(testUserId)
                .role(UserRole.CLIENT)
                .isActive(true)
                .build();
        testFreelancerProfile = FreelancerProfile.builder()
                .id(testUserId)
                .user(testFreelancer)
                .build();
        testClientProfile = ClientProfile.builder()
                .id(testUserId)
                .user(testClient)
                .build();
        testProject = Project.builder().id(1L).build();
        testPortfolio = FreelancerPortfolio.builder().id(1L).build();
    }

    @Test
    void getAllFreelancers_ShouldReturnListOfFreelancers() {
        when(userRepository.findByRole(UserRole.FREELANCER)).thenReturn(List.of(testFreelancer));
        when(freelancerProfileRepository.findByUser(testFreelancer)).thenReturn(Optional.of(testFreelancerProfile));
        when(freelancerProfileMapper.toDto(testFreelancerProfile)).thenReturn(new FreelancerProfileDto());

        List<FreelancerProfileDto> result = adminService.getAllFreelancers();

        assertFalse(result.isEmpty());
        verify(freelancerProfileRepository).findByUser(testFreelancer);
    }

    @Test
    void getFreelancerById_WhenExists_ShouldReturnFreelancer() {
        when(freelancerProfileRepository.findByUserId(testUserId)).thenReturn(Optional.of(testFreelancerProfile));
        when(freelancerProfileMapper.toDto(testFreelancerProfile)).thenReturn(new FreelancerProfileDto());

        FreelancerProfileDto result = adminService.getFreelancerById(testUserId);

        assertNotNull(result);
        verify(freelancerProfileRepository).findByUserId(testUserId);
    }

    @Test
    void getFreelancerById_WhenNotExists_ShouldThrowException() {
        when(freelancerProfileRepository.findByUserId(testUserId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.getFreelancerById(testUserId));
    }

    @Test
    void deactivateFreelancer_ShouldSetActiveFalse() {
        when(userRepository.findByIdAndRole(testUserId, UserRole.FREELANCER)).thenReturn(Optional.of(testFreelancer));

        adminService.deactivateFreelancer(testUserId);

        assertFalse(testFreelancer.getIsActive());
        verify(userRepository).save(testFreelancer);
    }

    @Test
    void deleteFreelancer_ShouldCallRepositoryDelete() {
        when(userRepository.findByIdAndRole(testUserId, UserRole.FREELANCER)).thenReturn(Optional.of(testFreelancer));

        adminService.deleteFreelancer(testUserId);

        verify(userRepository).delete(testFreelancer);
    }

    @Test
    void getAllClients_ShouldReturnListOfClients() {
        when(userRepository.findByRole(UserRole.CLIENT)).thenReturn(List.of(testClient));
        when(clientProfileRepository.findByUser(testClient)).thenReturn(Optional.of(testClientProfile));
        when(clientProfileMapper.toDto(testClientProfile)).thenReturn(new ClientProfileDto());

        List<ClientProfileDto> result = adminService.getAllClients();

        assertFalse(result.isEmpty());
        verify(clientProfileRepository).findByUser(testClient);
    }

    @Test
    void getClientById_WhenExists_ShouldReturnClient() {
        when(clientProfileRepository.findByUserId(testUserId)).thenReturn(Optional.of(testClientProfile));
        when(clientProfileMapper.toDto(testClientProfile)).thenReturn(new ClientProfileDto());

        ClientProfileDto result = adminService.getClientById(testUserId);

        assertNotNull(result);
        verify(clientProfileRepository).findByUserId(testUserId);
    }

    @Test
    void deactivateClient_ShouldSetActiveFalse() {
        when(userRepository.findByIdAndRole(testUserId, UserRole.CLIENT)).thenReturn(Optional.of(testClient));

        adminService.deactivateClient(testUserId);

        assertFalse(testClient.getIsActive());
        verify(userRepository).save(testClient);
    }

    @Test
    void deleteClient_ShouldCallRepositoryDelete() {
        when(userRepository.findByIdAndRole(testUserId, UserRole.CLIENT)).thenReturn(Optional.of(testClient));

        adminService.deleteClient(testUserId);

        verify(userRepository).delete(testClient);
    }

    @Test
    void getClientProjects_ShouldReturnProjects() {
        when(projectRepository.findByClientId(testUserId))
                .thenReturn(List.of(testProject));
        when(projectMapper.toDto(testProject))
                .thenReturn(new ProjectDto());

        List<ProjectDto> result = adminService.getClientProjects(testUserId);

        assertFalse(result.isEmpty());
        verify(projectRepository).findByClientId(testUserId);
        verify(projectMapper).toDto(testProject);
    }


    @Test
    void getProjectById_WhenExists_ShouldReturnProject() {
        when(projectRepository.findById(testUserId))
                .thenReturn(Optional.of(testProject));
        when(projectMapper.toDto(testProject))
                .thenReturn(new ProjectDto());

        ProjectDto result = adminService.getProjectById(testUserId);

        assertNotNull(result);
        verify(projectRepository).findById(testUserId);
        verify(projectMapper).toDto(testProject);
    }

    @Test
    void deleteProject_ShouldCallRepositoryDelete() {
        when(projectRepository.findById(testUserId)).thenReturn(Optional.of(testProject));

        adminService.deleteProject(testUserId);

        verify(projectRepository).delete(testProject);
    }

    @Test
    void getFreelancerPortfolio_ShouldReturnPortfolioItems() {
        when(portfolioRepository.findByFreelancerId(testUserId))
                .thenReturn(List.of(testPortfolio));
        when(portfolioMapper.toDto(testPortfolio))
                .thenReturn(new FreelancerPortfolioDto());

        List<FreelancerPortfolioDto> result = adminService.getFreelancerPortfolio(testUserId);

        assertFalse(result.isEmpty());
        verify(portfolioRepository).findByFreelancerId(testUserId);
        verify(portfolioMapper).toDto(testPortfolio);
    }

    @Test
    void deletePortfolio_ShouldCallRepositoryDelete() {
        when(portfolioRepository.findByIdAndFreelancerId(testUserId, testUserId))
                .thenReturn(Optional.of(testPortfolio));

        adminService.deletePortfolio(testUserId, testUserId);

        verify(portfolioRepository).delete(testPortfolio);
    }

    @Test
    void getAllFreelancers_WhenNoFreelancers_ShouldReturnEmptyList() {
        when(userRepository.findByRole(UserRole.FREELANCER)).thenReturn(Collections.emptyList());

        List<FreelancerProfileDto> result = adminService.getAllFreelancers();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllClients_WhenNoClients_ShouldReturnEmptyList() {
        when(userRepository.findByRole(UserRole.CLIENT)).thenReturn(Collections.emptyList());

        List<ClientProfileDto> result = adminService.getAllClients();

        assertTrue(result.isEmpty());
    }

    @Test
    void getClientProjects_WhenNoProjects_ShouldReturnEmptyList() {
        when(projectRepository.findByClientId(testUserId))
                .thenReturn(Collections.emptyList());

        List<ProjectDto> result = adminService.getClientProjects(testUserId);

        assertTrue(result.isEmpty());
        verify(projectRepository).findByClientId(testUserId);
        verifyNoInteractions(projectMapper);
    }

    @Test
    void getFreelancerPortfolio_WhenNoItems_ShouldReturnEmptyList() {
        when(portfolioRepository.findByFreelancerId(testUserId))
                .thenReturn(Collections.emptyList());

        List<FreelancerPortfolioDto> result = adminService.getFreelancerPortfolio(testUserId);

        assertTrue(result.isEmpty());
        verify(portfolioRepository).findByFreelancerId(testUserId);
        verifyNoInteractions(portfolioMapper);
    }
}