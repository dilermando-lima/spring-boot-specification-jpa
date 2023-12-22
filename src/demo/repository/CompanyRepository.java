package demo.repository;

import org.springframework.stereotype.Repository;

import demo.base.ContextAccountRepository;
import demo.model.Company;

@Repository
public interface CompanyRepository extends ContextAccountRepository<Company> {
}