package gray.task;

/**
 * Represents a todo task with a description and completion status.
 */
public class Todo extends Task {
    /**
     * Creates a new todo with the specified description.
     * The task is initialised to not done.
     *
     * @param description Description of todo.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toStorage() {
        return "T" + super.toStorage();
    }
}
