package demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import demo.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account,String> {
}