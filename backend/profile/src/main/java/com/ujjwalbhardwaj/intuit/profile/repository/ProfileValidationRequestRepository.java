package com.ujjwalbhardwaj.intuit.profile.repository;

import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface ProfileValidationRequestRepository extends CrudRepository<ProfileValidationRequest, String> {

    Optional<ProfileValidationRequest> findById(String id);

    List<ProfileValidationRequest> findAll();

    List<ProfileValidationRequest> findByBusinessId(String businessId);


}