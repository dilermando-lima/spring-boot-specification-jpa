package demo.base;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import demo.model.Account;

@SuppressWarnings("unchecked")
@NoRepositoryBean
@Deprecated
public interface BaseContextRepository<E extends EntityBaseWithAccount> extends JpaSpecificationExecutor<E>, CrudRepository<E, String> {

    public static final Specification<EntityBaseWithAccount> INCLUDE_ACCOUNT_TO_SPECIFICATION = 
        (r, q, c) -> c.equal(r.get("account").get("id"), ContextAccount.accountId());

    public static final Function<String, Specification<EntityBaseWithAccount>> INCLUDE_ACCOUNT_AND_ID_TO_SPECIFICATION = 
        id -> INCLUDE_ACCOUNT_TO_SPECIFICATION.and((r, q, c) -> c.equal(r.get("id"), id));

    public default Page<E> listOnContext(Specification<E> spec, Pageable pageable) {
        if(spec == null){
            spec = (Specification<E>) INCLUDE_ACCOUNT_TO_SPECIFICATION;
        }else{
            spec.and((Specification<E>) INCLUDE_ACCOUNT_TO_SPECIFICATION);
        }
        if(pageable == null){
            pageable = PageRequest.ofSize(10);
        }
        return findAll(spec, pageable);
    }

    public default Page<E> listOnContext(Specification<E> spec) {
        return listOnContext(spec, null);
    }

    public default Page<E> listOnContext(Pageable pageable) {
        return listOnContext(null, pageable);
    }

    public default Page<E> listOnContext(){
        return listOnContext(null, null);
    }

    public default E saveOnContext(E entity) {
        entity.setAccount(new Account(ContextAccount.accountId()));
        return save(entity);
    }

    public default void deleteByIdOnContext(String id) {
        delete((Specification<E>) INCLUDE_ACCOUNT_AND_ID_TO_SPECIFICATION.apply(id));
    }

    public default void deleteAllOnContext(Specification<E> spec) {
        spec.and((Specification<E>) INCLUDE_ACCOUNT_TO_SPECIFICATION);
        delete(spec);
    }

    public default E findByIdOnContext(String id) {
        return findOne((Specification<E>) INCLUDE_ACCOUNT_AND_ID_TO_SPECIFICATION.apply(id)).orElse(null);
    }
    
}
