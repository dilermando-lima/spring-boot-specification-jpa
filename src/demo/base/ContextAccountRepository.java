package demo.base;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import demo.model.Account;

@SuppressWarnings("unchecked")
@NoRepositoryBean
public interface ContextAccountRepository<E extends EntityBaseWithAccount> extends JpaSpecificationExecutor<E>, CrudRepository<E, String> {
    
    public static final Function<String, Specification<EntityBaseWithAccount>> INCLUDE_ACCOUNT_TO_SPECIFICATION = 
        accountId -> (r, q, c) ->   c.equal(r.get("account").get("id"), accountId);

    public static final BiFunction<String, String, Specification<EntityBaseWithAccount>> INCLUDE_ACCOUNT_AND_ID_TO_SPECIFICATION = 
        (accountId, entityId) -> INCLUDE_ACCOUNT_TO_SPECIFICATION.apply(accountId).and((r, q, c) -> c.equal(r.get("id"), entityId));

    public default List<E> listOnAccountContext(Specification<E> spec, String accountId) {
        if(spec == null){
            spec = (Specification<E>) INCLUDE_ACCOUNT_TO_SPECIFICATION.apply(accountId);
        }else{
            spec.and((Specification<E>) INCLUDE_ACCOUNT_TO_SPECIFICATION.apply(accountId));
        }
 
        return findAll(spec);
    }

    public default E saveOnAccountContext(E entity, String accountId) {
        entity.setAccount(new Account(accountId));
        entity.setDateInsert(ZonedDateTime.now());
        entity.setDateLastUpdate(ZonedDateTime.now());
        return save(entity);
    }

    public default void deleteByIdOnAccountContext(String accountId , String entityId) {
        delete((Specification<E>) INCLUDE_ACCOUNT_AND_ID_TO_SPECIFICATION.apply(accountId, entityId));
    }

    public default boolean existsByIdOnAccountContext(String accountId , String entityId) {
        return exists((Specification<E>) INCLUDE_ACCOUNT_AND_ID_TO_SPECIFICATION.apply(accountId, entityId));
    }

    public default void deleteAllOnAccountContext(Specification<E> spec) {
        spec.and((Specification<E>) INCLUDE_ACCOUNT_TO_SPECIFICATION);
        delete(spec);
    }

    public default E findByIdOnAccountContext(String accountId , String entityId) {
        return findOne((Specification<E>) INCLUDE_ACCOUNT_AND_ID_TO_SPECIFICATION.apply(accountId, entityId)).orElse(null);
    }

}
