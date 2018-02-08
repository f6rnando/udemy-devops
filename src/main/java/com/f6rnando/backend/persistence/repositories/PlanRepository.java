package com.f6rnando.backend.persistence.repositories;

import com.f6rnando.backend.persistence.domain.backend.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/************************************
 Created by f6rnando@gmail.com
 2018-02-07
 *************************************/

@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer> {
}
