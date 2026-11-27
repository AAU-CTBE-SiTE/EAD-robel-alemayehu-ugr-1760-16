package et.edu.aau.orderservice.dtos;

import java.math.BigDecimal;

public record OrderSummaryDto(
        Long id,
        String productName,
        int quantity,
        BigDecimal price
) {
}
