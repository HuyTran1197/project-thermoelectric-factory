package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.entity.ConsumableTransaction;
import com.example.project_backend_thermoelectric.repository.materials_manager.IConsumableTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumableTransactionService implements IConsumableTransactionService {
    @Autowired
    private IConsumableTransactionRepository  consumableTransactionRepository;
    @Override
    public ConsumableTransaction importConsumable(ConsumableTransaction consumableTransaction) {
        return consumableTransactionRepository.save(consumableTransaction);
    }

    @Override
    public ConsumableTransaction exportConsumable(ConsumableTransaction consumableTransaction) {
        return null;
    }
}
