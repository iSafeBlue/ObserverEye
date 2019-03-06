package org.observer.base.repository;

import org.observer.base.dto.TaskDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<TaskDTO, Integer> {

    public List<TaskDTO> findAllByUid(int uid);

}