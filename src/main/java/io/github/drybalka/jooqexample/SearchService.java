package io.github.drybalka.jooqexample;

import static org.jooq.generated.Tables.*;

import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.generated.tables.pojos.*;
import static org.jooq.impl.DSL.*;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SearchService {

  private final DSLContext dsl;

  private final DtoMapper mapper;

  public List<OutCustomer> searchCustomers(String name, String billing, String brand) {
    List<Customer> customers = dsl.selectFrom(CUSTOMER)
        .where(name == null ? noCondition() : CUSTOMER.NAME.likeIgnoreCase("%" + name + "%"))
        .and(billing == null ? noCondition()
            : exists(
                selectOne().from(CONTRACT)
                    .where(CONTRACT.CUSTOMER_ID.eq(CUSTOMER.ID))
                    .and(CONTRACT.BILLING.eq(billing))))
        .and(brand == null ? noCondition()
            : exists(
                selectOne().from(PRODUCT)
                    .where(productInCustomer(CUSTOMER.ID))
                    .and(PRODUCT.BRAND.eq(brand))))
        .limit(10)
        .fetchInto(Customer.class);

    return customers.stream().map(customer -> {
      var products = dsl.selectFrom(PRODUCT)
          .where(productInCustomer(val(customer.id())))
          .fetchInto(Product.class);
      return mapper.fromDto(customer, products);
    }).toList();
  }

  private Condition productInCustomer(Field<String> customerId) {
    return PRODUCT.CONTRACT_ID.in(
        select(CONTRACT.ID).from(CONTRACT)
            .where(CONTRACT.CUSTOMER_ID.eq(customerId)));
  }
}
