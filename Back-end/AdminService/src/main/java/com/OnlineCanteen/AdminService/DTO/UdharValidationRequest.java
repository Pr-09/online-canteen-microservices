package com.OnlineCanteen.AdminService.DTO;

import lombok.Data;

@Data
public class UdharValidationRequest {
    private Long userId;

    private String code;

    private Double amount;
}
