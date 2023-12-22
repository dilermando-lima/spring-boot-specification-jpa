package demo.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

@SuppressWarnings({"unchecked","java:S1612"})
public enum TypeFilter {

    EQUALS(             "eq",   (c,e,v) -> c.equal(e, v)),
    NOT_EQUALS(         "eqN",  (c,e,v) -> c.notEqual(e, v)),
    CONTAINS(           "ct",   (c,e,v) -> c.like((Expression<String>) e, "%" + v + "%" )),
    STARTS_WITH(        "sw",   (c,e,v) -> c.like((Expression<String>) e, "%" + v )),
    ENDS_WITH(          "ew",   (c,e,v) -> c.like((Expression<String>) e, v + "%" )),
    GREATER_THAN(       "gt",   (c,e,v) -> null ),
    GREATER_THAN_OR_EQUALS("gtE",(c,e,v) -> null),
    LESS_THAN(          "lt",   (c,e,v) -> null),
    LESS_THAN_OR_EQUALS("ltE",  (c,e,v) -> null),
    NULL(               "null", (c,e,v) -> Boolean.parseBoolean(String.valueOf(v)) ? c.isNull(e) : c.isNotNull(e)),
    IN(                 "in",   (c,e,v) -> c.in(e)),
    NOT_IN(             "inN",  (c,e,v) -> c.not(c.in(e))),
    ;
    
    public interface CriteriaFunction{
        public Predicate filter(CriteriaBuilder criteriaBuilder, Expression<?> expression, Object value);
    }

    private final String name;
    private final CriteriaFunction criteriaFunction;

    private TypeFilter(String name, CriteriaFunction criteriaFunction) {
        this.name = name;
        this.criteriaFunction = criteriaFunction;
    }

    public String getName() {
        return name;
    }

    public CriteriaFunction getCriteriaFunction() {
        return criteriaFunction;
    }

}
