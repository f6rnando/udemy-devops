package com.f6rnando.backend.service;

import com.f6rnando.backend.persistence.domain.backend.Plan;
import com.f6rnando.backend.persistence.repositories.PlanRepository;
import com.f6rnando.enums.PlansEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/************************************
 Created by f6rnando@gmail.com
 2018-02-19
 *************************************/

@Service
@Transactional(readOnly = true)
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public Plan findPlanById(int planID) {
        return planRepository.findOne(planID);
    }

    @Transactional
    public Plan createPlan(int planID) {

        Plan plan = null;

        if (planID == 1) {
            plan = planRepository.save(new Plan(PlansEnum.BASIC));
        } else if (planID == 2) {
            plan = planRepository.save(new Plan(PlansEnum.PRO));
        } else {
            throw new IllegalArgumentException("Plan ID " + planID + " not recognised.");
        }

        return plan;
    }
}
