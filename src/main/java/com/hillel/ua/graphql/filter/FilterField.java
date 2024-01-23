package com.hillel.ua.graphql.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class FilterField {

    private final static Logger LOGGER = LoggerFactory.getLogger(FilterField.class);

    private String operator;
    private String value;

    public Predicate generateCriteria(CriteriaBuilder builder, Path<Integer> field) {
        try {
            int numericValue = Integer.parseInt(value);
            switch (operator) {
                case "lt": return builder.lt(field, numericValue);
                case "le": return builder.le(field, numericValue);
                case "gt": return builder.gt(field, numericValue);
                case "ge": return builder.ge(field, numericValue);
                case "eq": return builder.equal(field, numericValue);
            }
        } catch (NumberFormatException ignored) {
            LOGGER.error("Failed to parse numeric value: {}", value);
        }

        return switch (operator) {
            case "endsWith" -> builder.like(field.as(String.class), "%" + value);
            case "startsWith" -> builder.like(field.as(String.class), value + "%");
            case "contains" -> builder.like(field.as(String.class), "%" + value + "%");
            case "eq" -> builder.equal(field.as(String.class), value);
            default -> null;  // TODO: handle unknown operators
        };
    }
}
