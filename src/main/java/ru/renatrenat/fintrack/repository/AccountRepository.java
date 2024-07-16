package ru.renatrenat.fintrack.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.renatrenat.fintrack.model.Account;
import ru.renatrenat.fintrack.model.User;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByUser_Id(long id);



    Optional<Account> findByTransactions_Id(Long id);

    Optional<Account> findByName(String name);

    boolean existsByUser_Id(long id);

    boolean existsByName(String name);

    long deleteByUser(User user);

    List<Account> findAllByUser(User user, Sort sort);

    
}