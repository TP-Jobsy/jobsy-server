package com.example.jobsyserver.features.freelancer.projection;

public interface FreelancerListItem {
    Long getId();

    String getFirstName();

    String getLastName();

    String getCountry();

    String getCity();

    String getAvatarUrl();

    Double getAverageRating();

    String getExperienceLevel();

    String getCategoryName();

    String getSpecializationName();

}
