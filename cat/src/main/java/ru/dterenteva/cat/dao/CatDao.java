package ru.dterenteva.cat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatDao extends JpaRepository<Cat, Integer> {
    public List<Cat> findCatsByBreed(String breed);
    public List<Cat> findCatsByOwnerId(Integer id);
}
