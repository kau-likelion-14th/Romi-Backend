package likelion14th.lte.Entity.todo.repository;

import likelion14th.lte.Entity.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {


    List<Todo> findAllByRoutineEnabledTrue();
}