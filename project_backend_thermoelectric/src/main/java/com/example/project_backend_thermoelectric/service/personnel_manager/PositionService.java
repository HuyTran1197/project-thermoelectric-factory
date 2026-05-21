package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Position;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IPositionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService implements IPositionService {
    @Autowired
    private IPositionRepo positionRepo;

    @Override
    public Position createPosition(Position position) {
        if (positionRepo.existsByName(position.getName())) {
            throw new RuntimeException("Chức vụ đã tồn tại!");
        }
        return positionRepo.save(position);
    }

    @Override
    public Page<Position> searchPositions(String keyword, int page, int size) {
        return positionRepo.findByNameContainingIgnoreCase(
                keyword != null ? keyword : "",
                PageRequest.of(page, size)
        );
    }

    @Override
    public List<Position> getAllPositions() {
        return positionRepo.findAll();
    }

    @Override
    public List<Position> searchPositions(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return positionRepo.findAll();
        }
        return positionRepo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Position getPositionById(Long id) {
        return positionRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Không tìm thấy chức vụ!"));
    }

    @Override
    public Position updatePosition(Long id, Position request) {
        Position position = getPositionById(id);
        position.setName(request.getName());
        return positionRepo.save(position);
    }

    @Override
    public void deletePosition(Long id) {
        if (!positionRepo.existsById(id)) {
            throw new RuntimeException("Không tìm thấy chức vụ!");
        }
        try {
            positionRepo.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Không thể xóa chức vụ vì đang có nhân viên sử dụng");
        }
    }
}
