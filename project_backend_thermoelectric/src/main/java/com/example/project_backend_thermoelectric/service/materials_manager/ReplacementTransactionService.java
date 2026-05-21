package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.ReplacementInventoryDto;
import com.example.project_backend_thermoelectric.dto.materials_manager.ReplacementTransactionDto;
import com.example.project_backend_thermoelectric.entity.ReplacementTransaction;
import com.example.project_backend_thermoelectric.entity.User;
import com.example.project_backend_thermoelectric.repository.materials_manager.IReplacementTransactionRepository;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReplacementTransactionService implements IReplacementTransactionService {
    @Autowired
    private IReplacementTransactionRepository replacementTransactionRepository;
    @Autowired
    private IUserRepo  userRepo;

    @Override
    public Page<ReplacementInventoryDto> getReplacementInventory(Pageable pageable, String code, String name) {
        return replacementTransactionRepository.getInventory(code, name, pageable);
    }

    @Override
    public Page<ReplacementTransactionDto> getReplacementTransactions(Pageable pageable, String keyword, String type, LocalDateTime from, LocalDateTime to) {
        String typeStr = (type == null || type.trim().isEmpty() || "null".equals(type)) ? null : type.trim();
        LocalDateTime filterTo = (to != null) ? to.withHour(23).withMinute(59).withSecond(59).withNano(999999999) : null;
        return replacementTransactionRepository.searchReplacementTransactions(keyword, typeStr, from, filterTo, pageable);
    }

    @Override
    public ReplacementTransaction importReplacement(ReplacementTransaction replacementTransaction) {
        replacementTransaction.setCreatedAt(LocalDateTime.now());

        User user = userRepo.findById(1L).orElseThrow();

        replacementTransaction.setCreatedBy(user);
        return replacementTransactionRepository.save(replacementTransaction);
    }

    @Override
    public ReplacementTransaction exportReplacement(ReplacementTransaction replacementTransaction) {
        return null;
    }
}
