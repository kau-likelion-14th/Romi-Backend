package likelion14th.lte.Entity.todo.dto.response;

import likelion14th.lte.Entity.todo.entity.Todo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoListResponse {


    private Long todoId;
    private String description;
    private String categoryName;
    private boolean isCompleted;

    public static TodoListResponse from(Todo todo, boolean completed){
        return new TodoListResponse(
                todo.getId(),
                todo.getDescription(),
                todo.getCategory().getCategoryName(),
                completed
        );
    }
}

