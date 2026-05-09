package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.entity.ReplacementTransaction;
import com.example.project_backend_thermoelectric.repository.materials_manager.IReplacementTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplacementTransactionService implements IReplacementTransactionService {
    @Autowired
    private IReplacementTransactionRepository replacementTransactionRepository;
    @Override
    public ReplacementTransaction importReplacement(ReplacementTransaction replacementTransaction) {
        return replacementTransactionRepository.save(replacementTransaction);
    }

    @Override
    public ReplacementTransaction exportReplacement(ReplacementTransaction replacementTransaction) {
        return null;
    }
}
