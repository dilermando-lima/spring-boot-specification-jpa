package demo.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

import demo.filter.ValueFilterParamSupport.ValueFilterParam;
import jakarta.persistence.criteria.Path;



public class FilterBuilder<E> {

    private final Object filterRequest;
    private final Object initialFilter;

    private FilterBuilder(Object initialFilter, Object filterRequest){
        Objects.requireNonNull(filterRequest);
        this.filterRequest = filterRequest;
        this.initialFilter = initialFilter;
    }

    public static <E> FilterBuilder<E> init(Object initialFilter, Object filterRequest){
        return new FilterBuilder<>(initialFilter, filterRequest);
    }

    /**
     * @param <E>
     * @param filterRequest
     * @apiNote call as FilterBuilder.< YourEntity >init( filterRequest ).build()
     * @return
     */
    public static <E> FilterBuilder<E> init(Object filterRequest){
        return new FilterBuilder<>(null, filterRequest);
    }

    public Specification<E> build(){

        List<Specification<E>> listSpecification =  new ArrayList<>();
        if(initialFilter != null){
            listSpecification.addAll(
                ValueFilterParamSupport
                .collectFilterParamToBuildQuery(initialFilter)
                .get()
                .map(this::convertFilterParamToSpecification)
                .toList()
            );
        }

        listSpecification.addAll(
            ValueFilterParamSupport
            .collectFilterParamToBuildQuery(filterRequest)
            .get()
            .map(this::convertFilterParamToSpecification)
            .toList()
        );

       return Specification.allOf(listSpecification);
    }




    private Specification<E> convertFilterParamToSpecification(ValueFilterParam valueFilterParam){
        return (root, query, criteriaBuilder) -> {
            Path<?> path = root;
            for (String paramPath : valueFilterParam.filterParam().param().split("\\.")) {
                path = path.get(paramPath);
            }
            return valueFilterParam
                        .filterParam()
                        .type()
                        .getCriteriaFunction()
                        .filter(criteriaBuilder, path, valueFilterParam.value());
        };
    }

  
    
}
