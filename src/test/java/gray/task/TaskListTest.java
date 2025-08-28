package gray.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TaskListTest {
    private final Todo todo = new Todo("borrow book");
    private final Deadline deadline = new Deadline("return book",
            LocalDateTime.of(2025, 8, 26, 23, 59));
    private final Event event = new Event("CTF", LocalDateTime.of(2025, 8, 26, 9, 0),
            LocalDateTime.of(2025, 8, 27, 21, 0));

    @Test
    public void filterByDate_dateWithOneTask_tasklist() {
        LocalDate date = LocalDate.of(2025, 8, 27);
        TaskList actualTaskList = new TaskList();
        actualTaskList.add(todo);
        actualTaskList.add(deadline);
        actualTaskList.add(event);
        TaskList expectedTaskList = new TaskList();
        expectedTaskList.add(event);
        assertEquals(expectedTaskList.toString(), actualTaskList.filterByDate(date).toString());
    }

    @Test
    public void filterByDate_dateWithTwoTasks_tasklist() {
        LocalDate date = LocalDate.of(2025, 8, 26);
        TaskList actualTaskList = new TaskList();
        actualTaskList.add(todo);
        actualTaskList.add(deadline);
        actualTaskList.add(event);
        TaskList expectedTaskList = new TaskList();
        expectedTaskList.add(deadline);
        expectedTaskList.add(event);
        assertEquals(expectedTaskList.toString(), actualTaskList.filterByDate(date).toString());
    }

    @Test
    public void filterByDate_dateWithNoTasks_emptyTasklist() {
        LocalDate date = LocalDate.of(2025, 8, 28);
        TaskList actualTaskList = new TaskList();
        actualTaskList.add(todo);
        actualTaskList.add(deadline);
        actualTaskList.add(event);
        TaskList expectedTaskList = new TaskList();
        assertEquals(expectedTaskList.toString(), actualTaskList.filterByDate(date).toString());
    }

    @Test
    public void toStorage_filledTaskList() {
        TaskList taskList = new TaskList();
        taskList.add(todo);
        taskList.add(deadline);
        taskList.add(event);
        assertEquals("""
                T | 0 | borrow book
                D | 0 | return book | 2025-08-26 2359
                E | 0 | CTF | 2025-08-26 0900 | 2025-08-27 2100
                """, taskList.toStorage());
    }

    @Test
    public void toStorage_emptyTaskList_emptyString() {
        assertEquals("", new TaskList().toStorage());
    }
}
