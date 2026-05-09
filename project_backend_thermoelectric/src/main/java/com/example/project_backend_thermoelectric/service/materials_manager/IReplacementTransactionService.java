package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.entity.ReplacementTransaction;

public interface IReplacementTransactionService {
    ReplacementTransaction importReplacement(ReplacementTransaction replacementTransaction);
    ReplacementTransaction exportReplacement(ReplacementTransaction replacementTransaction);
}
