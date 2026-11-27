package et.edu.aau.orderservice.services;

import et.edu.aau.orderservice.dtos.CustomerDto;
import et.edu.aau.orderservice.dtos.CustomerOrdersResponse;
import et.edu.aau.orderservice.dtos.OrderSummaryDto;
import et.edu.aau.orderservice.entities.Order;
import et.edu.aau.orderservice.exceptions.CustomerNotFoundException;
import et.edu.aau.orderservice.exceptions.CustomerServiceUnavailableException;
import et.edu.aau.orderservice.exceptions.OrderNotFoundException;
import et.edu.aau.orderservice.repositories.OrderRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final String customerServiceUrl;

    public OrderService(
            OrderRepository orderRepository,
            RestTemplate restTemplate,
            @Value("${customer.service.url}") String customerServiceUrl
    ) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.customerServiceUrl = customerServiceUrl;
    }

    public Order save(Order order) {
        order.setId(null);
        return orderRepository.save(order);
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    public Order update(Long id, Order updatedOrder) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        existingOrder.setProductName(updatedOrder.getProductName());
        existingOrder.setQuantity(updatedOrder.getQuantity());
        existingOrder.setPrice(updatedOrder.getPrice());
        existingOrder.setCustomerId(updatedOrder.getCustomerId());
        return orderRepository.save(existingOrder);
    }

    public void deleteById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    public List<Order> getOrdersByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public CustomerOrdersResponse getCustomerWithOrders(String customerId) {
        String baseUrl = customerServiceUrl.endsWith("/")
                ? customerServiceUrl.substring(0, customerServiceUrl.length() - 1)
                : customerServiceUrl;
        String url = baseUrl + "/api/customers/" + customerId;

        CustomerDto customer;
        try {
            customer = restTemplate.getForObject(url, CustomerDto.class);
            if (customer == null) {
                throw new CustomerNotFoundException("Customer not found with id: " + customerId);
            }
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new CustomerNotFoundException("Customer not found with id: " + customerId);
            }
            throw new CustomerServiceUnavailableException(
                    "Customer service returned error: " + ex.getStatusCode(), ex);
        } catch (RestClientException ex) {
            throw new CustomerServiceUnavailableException(
                    "Customer service is unavailable or unreachable: " + ex.getMessage(), ex);
        }

        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<OrderSummaryDto> orderSummaries = orders.stream()
                .map(order -> new OrderSummaryDto(
                        order.getId(),
                        order.getProductName(),
                        order.getQuantity(),
                        order.getPrice()
                ))
                .toList();

        return new CustomerOrdersResponse(customer, orderSummaries);
    }
}
