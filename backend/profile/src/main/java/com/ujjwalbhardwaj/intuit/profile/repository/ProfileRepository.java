package com.ujjwalbhardwaj.intuit.profile.repository;

import com.ujjwalbhardwaj.intuit.profile.entity.Profile;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface ProfileRepository extends CrudRepository<Profile, String> {

    Optional<Profile> findById(String id);

    List<Profile> findAll();
}