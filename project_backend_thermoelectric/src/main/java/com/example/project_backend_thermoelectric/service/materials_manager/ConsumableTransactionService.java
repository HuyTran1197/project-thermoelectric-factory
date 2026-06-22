package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.ConsumableInventoryDto;
import com.example.project_backend_thermoelectric.dto.materials_manager.ConsumableTransactionDto;
import com.example.project_backend_thermoelectric.entity.ConsumableTransaction;
import com.example.project_backend_thermoelectric.entity.User;
import com.example.project_backend_thermoelectric.repository.materials_manager.IConsumableMaterialRepository;
import com.example.project_backend_thermoelectric.repository.materials_manager.IConsumableTransactionRepository;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderConsumableRepository;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsumableTransactionService implements IConsumableTransactionService {
    @Autowired
    private IConsumableTransactionRepository  consumableTransactionRepository;
    @Autowired
    private IWorkOrderConsumableRepository workOrderConsumableRepository;

    @Autowired
    private IConsumableMaterialRepository materialRepository;
    @Autowired
    private IUserRepo                     userRepo;

    @Override
    public Page<ConsumableInventoryDto> getConsumableInventory(Pageable pageable, String code, String name) {
        return consumableTransactionRepository.getInventory(code, name, pageable);
    }

    @Override
    public Page<ConsumableTransactionDto> getConsumableTransactions(Pageable pageable, String keyword, String type, LocalDateTime from, LocalDateTime to) {
        String typeStr = (type == null || type.trim().isEmpty() || "null".equals(type)) ? null : type.trim();
        LocalDateTime filterTo = (to != null) ? to.withHour(23).withMinute(59).withSecond(59).withNano(999999999) : null;
        return consumableTransactionRepository.searchConsumableTransactions(keyword, typeStr, from, filterTo, pageable);
    }

    @Override
    public ConsumableTransaction importConsumable(ConsumableTransaction transaction) {
        // set time
        transaction.setCreatedAt(LocalDateTime.now());
        //Lấy thông tin phiên đăng nhập hiện tại từ Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //check
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Bạn chưa đăng nhập hoặc phiên làm việc đã hết hạn!");
        }

        // Lấy username của người đang đăng nhập
        String currentUsername = authentication.getName();

        //Truy vấn User từ database dựa vào username
        User currentUser = userRepo.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin tài khoản: " + currentUsername));
        //Gán user đang đăng nhập vào transaction
        transaction.setCreatedBy(currentUser);

        return consumableTransactionRepository.save(transaction);
    }

    @Override
    public ConsumableTransaction exportConsumable(ConsumableTransaction consumableTransaction) {
        return null;
    }

}
