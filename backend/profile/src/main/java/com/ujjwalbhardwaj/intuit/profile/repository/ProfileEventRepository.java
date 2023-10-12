package com.ujjwalbhardwaj.intuit.profile.repository;

import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ProfileEventRepository extends CrudRepository<ProfileEvent, String> {

    List<ProfileEvent> findAll();

    List<ProfileEvent> findByBusinessId(String businessId);
}

