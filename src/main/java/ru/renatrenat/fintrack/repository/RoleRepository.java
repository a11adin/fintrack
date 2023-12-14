package ru.renatrenat.fintrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.renatrenat.fintrack.model.ERole;
import ru.renatrenat.fintrack.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
        Optional<Role> findByName(ERole name);

}
