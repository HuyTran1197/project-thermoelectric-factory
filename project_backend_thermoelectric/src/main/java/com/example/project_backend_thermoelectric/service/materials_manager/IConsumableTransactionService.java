package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.entity.ConsumableTransaction;

public interface IConsumableTransactionService {
    ConsumableTransaction importConsumable(ConsumableTransaction consumableTransaction);
    ConsumableTransaction exportConsumable(ConsumableTransaction consumableTransaction);
}
