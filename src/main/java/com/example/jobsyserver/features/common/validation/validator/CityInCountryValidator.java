package com.example.jobsyserver.features.common.validation.validator;

import com.example.jobsyserver.features.common.validation.service.LocationValidationService;
import com.example.jobsyserver.features.common.validation.annotation.CityInCountry;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

@Component
public class CityInCountryValidator implements ConstraintValidator<CityInCountry, Object> {

    private String countryField;
    private String cityField;
    private final LocationValidationService locationService;

    public CityInCountryValidator(LocationValidationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public void initialize(CityInCountry annotation) {
        this.countryField = annotation.countryField();
        this.cityField    = annotation.cityField();
    }

    @Override
    public boolean isValid(Object bean, ConstraintValidatorContext context) {
        BeanWrapper wrapper = new BeanWrapperImpl(bean);
        String country = (String) wrapper.getPropertyValue(countryField);
        String city    = (String) wrapper.getPropertyValue(cityField);

        if (country == null || city == null) {
            return true;
        }
        if (!locationService.countryExists(country)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Страна '" + country + "' не найдена"
                    )
                    .addPropertyNode(countryField)
                    .addConstraintViolation();
            return false;
        }
        if (!locationService.cityExistsInCountry(country, city)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Город '" + city + "' не найден в стране '" + country + "'"
                    )
                    .addPropertyNode(cityField)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
