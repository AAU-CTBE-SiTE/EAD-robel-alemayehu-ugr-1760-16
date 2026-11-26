package et.edu.aau.customerservice.service;


import java.util.List;

import org.springframework.stereotype.Service;
import et.edu.aau.customerservice.exceptions.CustomerNotFoundException;
import et.edu.aau.customerservice.model.Customer;
import et.edu.aau.customerservice.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer saveCustomer(Customer customer) {
        customer.setId(null); // let MongoDB generate the id on create
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public Customer updateCustomer(String id, Customer updated) {
        Customer existing = getCustomerById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        return customerRepository.save(existing);
    }

    public void deleteCustomer(String id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }
}
