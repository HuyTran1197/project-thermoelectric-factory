package com.example.project_backend_thermoelectric.dto.materials_manager;

import lombok.Data;
import java.util.List;

@Data
public class StorekeeperApproveDto {
    private List<Long> consumableIds;
    private List<Long> replacementIds;
}
