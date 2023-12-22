package demo.repository;

import org.springframework.stereotype.Repository;

import demo.base.ContextAccountRepository;
import demo.model.Employee;

@Repository
public interface EmployeeRepository extends ContextAccountRepository<Employee> {
}