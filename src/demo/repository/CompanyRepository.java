package demo.repository;

import org.springframework.stereotype.Repository;

import demo.base.BaseContextRepository;
import demo.model.Company;

@Repository
public interface CompanyRepository extends BaseContextRepository<Company> {
}