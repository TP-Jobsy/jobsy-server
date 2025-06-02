package com.example.jobsyserver.service.impl.admin;

import com.example.jobsyserver.features.admin.service.impl.ProjectAdminServiceImpl;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.mapper.ProjectMapper;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.projection.ProjectAdminListItem;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectAdminServiceImplTest {

    @Mock private ProjectRepository repo;
    @Mock private ProjectMapper mapper;
    @Mock
    private FreelancerProfileRepository freelancerProfileRepo;
    @InjectMocks private ProjectAdminServiceImpl service;

    @Test
    void getByClient_ShouldMapAll() {
        Project p = new Project(); p.setId(5L);
        ProjectDto dto = new ProjectDto();
        when(repo.findByClientId(10L)).thenReturn(List.of(p));
        when(mapper.toDto(p)).thenReturn(dto);
        var list = service.getByClient(10L);
        assertThat(list).containsExactly(dto);
        verify(repo).findByClientId(10L);
        verify(mapper).toDto(p);
    }

    @Test
    void getByFreelancer_ShouldMapAll() {
        FreelancerProfile profile = new FreelancerProfile();
        profile.setId(20L);
        when(freelancerProfileRepo.findByUserId(20L))
                .thenReturn(Optional.of(profile));
        Project p = new Project();
        p.setId(7L);
        when(repo.findByAssignedFreelancerId(20L))
                .thenReturn(List.of(p));
        ProjectDto dto = new ProjectDto();
        when(mapper.toDto(p)).thenReturn(dto);
        var list = service.getByFreelancer(20L);
        assertThat(list).containsExactly(dto);
        InOrder inOrder = inOrder(freelancerProfileRepo, repo, mapper);
        inOrder.verify(freelancerProfileRepo).findByUserId(20L);
        inOrder.verify(repo).findByAssignedFreelancerId(20L);
        inOrder.verify(mapper).toDto(p);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void getById_WhenFound() {
        Project p = new Project(); p.setId(3L);
        ProjectDto dto = new ProjectDto();
        when(repo.findById(3L)).thenReturn(Optional.of(p));
        when(mapper.toDto(p)).thenReturn(dto);
        var result = service.getById(3L);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getById_WhenMissing_ShouldThrow() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_ShouldRemoveEntity() {
        Project p = new Project(); p.setId(4L);
        when(repo.findById(4L)).thenReturn(Optional.of(p));
        service.delete(4L);
        verify(repo).delete(p);
    }

    @Test
    void delete_WhenMissing_NoOpOrThrow() {
        when(repo.findById(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.delete(5L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void pageAll_ShouldReturnProjectedPage() {
        ProjectAdminListItem item = mock(ProjectAdminListItem.class);
        Page<ProjectAdminListItem> page = new PageImpl<>(List.of(item));
        when(repo.findAllProjectedByAdmin(any(Pageable.class))).thenReturn(page);
        var out = service.pageAll(PageRequest.of(0,5));
        assertThat(out.getContent()).containsExactly(item);
    }

    @Test
    void search_ShouldReturnProjected() {
        ProjectAdminListItem item = mock(ProjectAdminListItem.class);
        Page<ProjectAdminListItem> page = new PageImpl<>(List.of(item));
        when(repo.findAllProjected(any(), any(Pageable.class))).thenReturn(page);
        var out = service.search("x", "OPEN", LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), PageRequest.of(0,5));
        assertThat(out.getContent()).containsExactly(item);
        verify(repo).findAllProjected(any(), any(Pageable.class));
    }
}