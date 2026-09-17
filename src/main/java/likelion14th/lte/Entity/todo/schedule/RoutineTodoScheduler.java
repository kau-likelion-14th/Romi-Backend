package likelion14th.lte.Entity.todo.schedule;

import likelion14th.lte.Entity.todo.entity.Todo;
import likelion14th.lte.Entity.todo.generator.RoutineTodoDateGenerator;
import likelion14th.lte.Entity.todo.repository.TodoDateRepository;
import likelion14th.lte.Entity.todo.repository.TodoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)

public class RoutineTodoScheduler {
    private final TodoDateRepository todoDateRepository;
    private final RoutineTodoDateGenerator routineTodoDateGenerator;
    private final TodoRepository todoRepository;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void generateRoutineTodoDates() {
        LocalDate today = LocalDate.now();

        List<Todo> routineTodos = todoRepository.findAllByRoutineEnabledTrue();
        for (Todo todo : routineTodos) {
            routineTodoDateGenerator.generate(
                    todo,
                    todo.getStartDate(),
                    todo.getEndDate(),
                    today
            );
        }
    }
}
