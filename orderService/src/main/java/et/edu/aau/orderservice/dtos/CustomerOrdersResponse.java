package et.edu.aau.orderservice.dtos;

import java.util.List;

public record CustomerOrdersResponse(
        CustomerDto customer,
        List<OrderSummaryDto> orders
) {
}
