package ssa.tools;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterSpecificationBuilder {

    private Map<String, Object> statements;

    public FilterSpecificationBuilder() {
        this.statements = new HashMap<>();
    }

    public FilterSpecificationBuilder addFilter(String name, Object value) {
        this.statements.put(name, value);
        return this;
    }

    public Specification build() {
        return (Specification) (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.select(root);
            List<Predicate> predicates = new ArrayList<>();
            statements.forEach((name, value) -> {
                if (null != name && null != value) {
                    predicates.add(criteriaBuilder.equal(root.get(name), value));
                }
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
