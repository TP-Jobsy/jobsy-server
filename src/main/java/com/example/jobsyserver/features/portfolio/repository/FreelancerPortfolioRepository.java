package com.example.jobsyserver.features.portfolio.repository;

import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.portfolio.projection.PortfolioAdminListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FreelancerPortfolioRepository extends JpaRepository<FreelancerPortfolio, Long> ,
        JpaSpecificationExecutor<FreelancerPortfolio> {
    @EntityGraph(attributePaths = {"freelancer", "skills"})
    List<FreelancerPortfolio> findByFreelancerId(Long freelancerId);

    @EntityGraph(attributePaths = {"freelancer", "skills"})
    Optional<FreelancerPortfolio> findByIdAndFreelancerId(Long portfolioId, Long freelancerId);

    default Page<PortfolioAdminListItem> findAllProjected(
            Specification<FreelancerPortfolio> spec,
            Pageable pageable
    ) {
        return findAll(spec, pageable)
                .map(p -> new PortfolioAdminListItem() {
                    @Override
                    public Long getId() {
                        return p.getId();
                    }

                    @Override
                    public String getTitle() {
                        return p.getTitle();
                    }

                    @Override
                    public LocalDateTime getCreatedAt() {
                        return p.getCreatedAt();
                    }

                    @Override
                    public String getFirstName() {
                        return p.getFreelancer().getUser().getFirstName();
                    }

                    @Override
                    public String getLastName() {
                        return p.getFreelancer().getUser().getLastName();
                    }
                });
    }
}