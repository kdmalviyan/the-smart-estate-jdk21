package com.sfd.thesmartestate.lms.services;

import com.sfd.thesmartestate.lms.entities.LeadInventorySize;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import com.sfd.thesmartestate.lms.repositories.LeadInventorySizeRepository;
import com.sfd.thesmartestate.users.services.UserService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP")

public class LeadInventorySizeServiceImpl implements LeadInventorySizeService {

    private final LeadInventorySizeRepository leadInventorySizeRepository;

    private final UserService userService;

    @Override
    public List<LeadInventorySize> findAll() {

        return leadInventorySizeRepository.findAll().stream().filter(LeadInventorySize::getActive).sorted(Comparator.comparing(LeadInventorySize::getLastUpdateAt).reversed()).collect(Collectors.toList());
    }

    @Override
    public LeadInventorySize create(LeadInventorySize leadInventorySize) {
        LeadInventorySize existingInventorySize=findBySize(createInventorySize(leadInventorySize.getSize()));
        if (Objects.isNull(existingInventorySize)) {
            leadInventorySize.setSize(createInventorySize(leadInventorySize.getSize()));
            leadInventorySize.setCreatedBy(userService.findLoggedInUser());
            leadInventorySize.setUpdatedBy(userService.findLoggedInUser());
            leadInventorySize.setCreatedAt(LocalDateTime.now());
            leadInventorySize.setLastUpdateAt(LocalDateTime.now());
            leadInventorySize.setActive(true);
            return   leadInventorySizeRepository.save(leadInventorySize);
        }
        else{
            throw new LeadException("Lead Inventory Already Exist");
        }

    }

    @Override
    public LeadInventorySize update(LeadInventorySize leadInventorySize) {
        LeadInventorySize existingLeadInventory=findById(leadInventorySize.getId());
        if (Objects.nonNull(existingLeadInventory)) {
            if(Objects.isNull(findBySize(createInventorySize(leadInventorySize.getSize())))){
                existingLeadInventory.setLastUpdateAt(LocalDateTime.now());
                existingLeadInventory.setUpdatedBy(userService.findLoggedInUser());
                existingLeadInventory.setDescription(leadInventorySize.getDescription());
                existingLeadInventory.setSize(leadInventorySize.getSize());
                return  leadInventorySizeRepository.save(existingLeadInventory);
            }
            else {
                throw new LeadException("Lead Inventory Already Exist With this Size");
            }

        }
       else{
           throw  new LeadException("Lead Inventory Not Found");
        }
    }

    @Override
    public LeadInventorySize findById(Long id) {
        return leadInventorySizeRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(LeadInventorySize leadType) {
        leadInventorySizeRepository.delete(leadType);
    }

    @Override
    public long count() {
        return leadInventorySizeRepository.count();
    }

    @Override
    public LeadInventorySize findBySize(String size) {
        return leadInventorySizeRepository.findBySize(size);
    }

    @Override
    public LeadInventorySize deactivateLeadInventory(Long id){
            LeadInventorySize leadInventory=findById(id);
            if(Objects.nonNull(leadInventory)){
                leadInventory.setLastUpdateAt(LocalDateTime.now());
                leadInventory.setActive(false);
                leadInventory.setUpdatedBy(userService.findLoggedInUser());
                return leadInventorySizeRepository.save(leadInventory);
            }
            else{
                throw new LeadException("Lead Inventory Not Found");
            }
    }
    @Override
    public String createInventorySize(String leadInventorySize) {
        String replaceText = leadInventorySize.stripLeading().stripTrailing();
        replaceText = replaceText.replace(' ', '_');
        return replaceText;
    }
}
