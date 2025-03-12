package io.github.drybalka.jooqexample;

import java.util.List;

public record InContract(String id, String billing, String customerId, List<InProduct> products) {
}
