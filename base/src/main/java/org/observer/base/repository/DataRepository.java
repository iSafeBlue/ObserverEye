package org.observer.base.repository;

import org.observer.base.dto.DataDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataRepository extends CrudRepository<DataDTO, Integer> {
    List<DataDTO> findAllByFunValAndGroupNameAndTid(String funVal , String group ,int tid);

    List<DataDTO> findAllByTid(int tid);
}