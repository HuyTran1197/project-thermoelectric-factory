package com.example.project_backend_thermoelectric.service;

import com.example.project_backend_thermoelectric.entity.ReplacementTransaction;
import com.example.project_backend_thermoelectric.repositiory.IReplacementTransactionRepository;
import com.example.project_backend_thermoelectric.service.impl.IReplacementTransactionService;
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
