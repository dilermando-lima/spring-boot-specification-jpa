package demo.repository;

import org.springframework.stereotype.Repository;

import demo.base.BaseContextRepository;
import demo.model.Employee;

@Repository
public interface EmployeeRepository extends BaseContextRepository<Employee> {
}