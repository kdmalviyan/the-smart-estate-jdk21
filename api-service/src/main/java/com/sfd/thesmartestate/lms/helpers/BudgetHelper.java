package com.sfd.thesmartestate.lms.helpers;

import com.sfd.thesmartestate.lms.entities.Budget;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BudgetHelper {
    public Double convertToAbsoluteValue(String unit, Double amount) {
        if("lac".equalsIgnoreCase(unit)) {
            return amount * 100000;
        } else if("cr".equalsIgnoreCase(unit) || "crore".equalsIgnoreCase(unit)) {
            return amount * 10000000;
        }
        throw new LeadException("Budget's start unit not supported. Supported values are lac, cr, crore only.");
    }

    public Budget createDefaultBudget() {
        Budget budget = new Budget();
        budget.setEndUnit("LAC");
        budget.setStartUnit("LAC");
        budget.setStartAmount(0D);
        budget.setEndAmount(999999999D);
        return budget;
    }
}
