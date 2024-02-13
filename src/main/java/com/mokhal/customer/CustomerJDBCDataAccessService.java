package com.mokhal.customer;

import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT * FROM customer
                """;

        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper);

        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                SELECT * FROM customer WHERE id=?
                """;

        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper, id);

        return customers.stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer (name, email, age) VALUES (?, ?, ?)
                """;
        int result = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
        System.out.println("LOG INSERT STATEMENT" + result);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT COUNT(id) FROM customer WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);

        return count != null && count > 0 ? true : false;
    }

    @Override
    public void deleteCustomerById(Long id) {
        var sql = """
                DELETE FROM customer WHERE id = ?
                """;

        int result = jdbcTemplate.update(sql, id);
        System.out.println("deleteCustomerById= " + result);
    }

    @Override
    public void updateCustomer(Customer customerUpdate) {

        if (customerUpdate.getName() != null) {
            var sql = """
                UPDATE customer SET name = ? WHERE id = ?
                """;

            int result = jdbcTemplate.update(sql, customerUpdate.getName(), customerUpdate.getId());
            System.out.println("update customer name = " + result);
        }

        if (customerUpdate.getEmail() != null) {
            var sql = """
                    UPDATE customer SET email = ? WHERE id = ?
                    """;

            int result = jdbcTemplate.update(sql, customerUpdate.getEmail(), customerUpdate.getId());
            System.out.println("update customer email = " + result);
        }

        if (customerUpdate.getAge() != null) {
            var sql = """
                    UPDATE customer SET age = ? WHERE id = ?
                    """;

            int result = jdbcTemplate.update(sql, customerUpdate.getAge(), customerUpdate.getId());
            System.out.println("update customer age = " + result);
        }

    }
}
