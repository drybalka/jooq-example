package io.github.drybalka.jooqexample;

import java.util.List;

public record OutCustomer(String id, String name, List<OutProduct> products) {
}
