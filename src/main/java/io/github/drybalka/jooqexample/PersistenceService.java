package io.github.drybalka.jooqexample;

import static org.jooq.generated.Tables.PRODUCT;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.generated.tables.daos.*;
import org.jooq.generated.tables.pojos.*;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PersistenceService {

  private final DSLContext dsl;

  private final DtoMapper mapper;

  private final CustomerDao customerDao;
  private final ContractDao contractDao;
  private final ProductDao productDao;

  public void insertCustomer(InCustomer inCustomer) {
    Customer customer = mapper.toDto(inCustomer);
    customerDao.insert(customer);
  }

  public void insertContract(InContract inContract) {
    Contract contract = mapper.toDto(inContract);
    contractDao.insert(contract);

    List<Product> products = mapper.extractProducts(inContract);
    productDao.insert(products);
  }

  public void updateContract(InContract inContract) {
    Contract contract = mapper.toDto(inContract);
    contractDao.update(contract);

    List<Product> products = mapper.extractProducts(inContract);
    List<String> productIds = products.stream().map(Product::id).toList();
    dsl.deleteFrom(PRODUCT)
        .where(PRODUCT.CONTRACT_ID.eq(contract.id()))
        .and(PRODUCT.ID.notIn(productIds))
        .execute();
    productDao.merge(products);
  }

}
