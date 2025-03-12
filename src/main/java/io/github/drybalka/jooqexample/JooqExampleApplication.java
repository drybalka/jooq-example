package io.github.drybalka.jooqexample;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(scanBasePackages = { "org.jooq.generated", "io.github.drybalka.jooqexample" })
@RequiredArgsConstructor
@Slf4j
public class JooqExampleApplication implements CommandLineRunner {

  private final PersistenceService persistenceService;

  private final SearchService searchService;

  public static void main(String[] args) {
    SpringApplication.run(JooqExampleApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    log.info("XXXXXXXXXX Application starting");

    InCustomer customer = new InCustomer("cust_id", "name", "occupation", "age");
    persistenceService.insertCustomer(customer);

    InProduct product1 = new InProduct("prod_id1", "VF");
    InProduct product2 = new InProduct("prod_id2", "VF");
    InContract contract = new InContract("contract_id1", "monthly", "cust_id", List.of(product1, product2));
    persistenceService.insertContract(contract);

    List<OutCustomer> allCustomers = searchService.searchCustomers(null, null, null);
    log.info("All customers: {}", allCustomers);

    List<OutCustomer> vfCustomers = searchService.searchCustomers(null, null, "VF");
    log.info("VF customers: {}", vfCustomers);

    InProduct upd_product1 = new InProduct("prod_id1", "Telekom");
    InProduct upd_product2 = new InProduct("upd_prod_id", "Telekom");
    InContract upd_contract = new InContract("contract_id1", "daily", "cust_id",
        List.of(upd_product1, upd_product2));
    persistenceService.updateContract(upd_contract);

    List<OutCustomer> tCustomers = searchService.searchCustomers(null, null, "Telekom");
    log.info("Telekom customers: {}", tCustomers);
  }
}
