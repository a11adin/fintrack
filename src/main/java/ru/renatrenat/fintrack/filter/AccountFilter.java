package ru.renatrenat.fintrack.filter;

import org.springframework.data.jpa.domain.Specification;
import ru.renatrenat.fintrack.model.Account;

public record AccountFilter(Float balanceGte) {
    public Specification<Account> toSpecification() {
        return Specification.where(balanceGteSpec());
    }

    private Specification<Account> balanceGteSpec() {
        return ((root, query, cb) -> balanceGte != null
                ? cb.greaterThanOrEqualTo(root.get("balance"), balanceGte)
                : null);
    }
}