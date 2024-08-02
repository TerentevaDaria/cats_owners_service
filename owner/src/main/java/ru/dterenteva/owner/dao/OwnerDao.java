package ru.dterenteva.owner.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dterenteva.owner.dao.Owner;

@Repository
public interface OwnerDao extends JpaRepository<Owner, Integer> {
}
