package ssa.tools;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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

    /**
     * Adds a {@link Map.Entry} to the filter's statements.
     *
     * @param name  name of filter's statement
     * @param value value of filter's statement
     *
     * @return current {@link FilterSpecificationBuilder}
     */
    public FilterSpecificationBuilder addFilter(String name, Object value) {
        this.statements.put(name, value);
        return this;
    }

    /**
     * Builds and returns a {@link Specification} which can be used by {@link JpaSpecificationExecutor}.
     *
     * @return {@link FilterSpecificationBuilder}
     *
     * @see Specification
     * @see JpaSpecificationExecutor
     */

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
