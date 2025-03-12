package io.github.drybalka.jooqexample;

import java.util.List;

import org.jooq.generated.tables.pojos.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoMapper {
  Customer toDto(InCustomer customer);

  Contract toDto(InContract contract);

  Product toDto(InProduct product, String contractId);

  default List<Product> extractProducts(InContract contract) {
    var products = contract.products();
    var id = contract.id();

    return products.stream().map(p -> toDto(p, id)).toList();
  }

  OutProduct fromDto(Product product);

  OutCustomer fromDto(Customer customer, List<Product> products);
}
