package gray.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GrayTest {
    private final Gray gray = new Gray("./data/dummy.txt");

    @Test
    public void getResponse_invalid() {
        assertEquals("I don't understand what you mean.\n" + "Please enter a valid instruction.",
                gray.getResponse("blah"));
        assertEquals("I don't understand what you mean.\n" + "Please enter a valid instruction.",
                gray.getResponse("some random command"));
    }

    @Test
    public void getResponse_list() {
        assertEquals("Nice! You don't have any tasks left!", gray.getResponse("list"));
    }

    @Test
    public void getResponse_taskNotFound() {
        assertEquals("This task cannot be found!", gray.getResponse("mark 1"));
        assertEquals("This task cannot be found!", gray.getResponse("unmark 1"));
        assertEquals("This task cannot be found!", gray.getResponse("delete 1"));
    }

    @Test
    public void getResponse_noIndex() {
        assertEquals("Please give the index of the task.", gray.getResponse("mark two"));
        assertEquals("Please give the index of the task.", gray.getResponse("unmark two"));
        assertEquals("Please give the index of the task.", gray.getResponse("delete two"));
    }

    @Test
    public void getResponse_invalidTodo() {
        assertEquals("Please provide a description for your todo.", gray.getResponse("todo "));
    }

    @Test
    public void getResponse_invalidDeadline() {
        assertEquals("Please provide a description and due date/time for your deadline.",
                gray.getResponse("deadline "));
        assertEquals("Please provide a due date/time for your deadline.",
                gray.getResponse("deadline return book"));
        assertEquals("Please provide a description and due date/time for your deadline.",
                gray.getResponse("deadline /by"));
        assertEquals("Please provide a description for your deadline.",
                gray.getResponse("deadline /by 2025-08-25 2359"));
        assertEquals("Please provide a due date/time for your deadline.",
                gray.getResponse("deadline return book /by"));
    }

    @Test
    public void getResponse_invalidEvent() {
        assertEquals("Please provide a description, start and end date/time for your event.",
                gray.getResponse("event "));
        assertEquals("Please provide a description, start and end date/time for your event.",
                gray.getResponse("event  /from  /to  "));
        assertEquals("Please provide a start and end date/time for your event.",
                gray.getResponse("event marathon"));
        assertEquals("Please provide a description and end date/time for your event.",
                gray.getResponse("event    /from 2025-08-25 2359"));
        assertEquals("Please provide a description and start date/time for your event.",
                gray.getResponse("event    /to 2025-08-25 2359"));
        assertEquals("Please provide a description for your event.",
                gray.getResponse("event    /from 2025-08-25 2359 /to 2025-08-25 2359"));
        assertEquals("Please provide a end date/time for your event.",
                gray.getResponse("event marathon /from 2025-08-25 2359 /to"));
        assertEquals("Please provide a start date/time for your event.",
                gray.getResponse("event marathon /from  /to 2025-08-25 2359"));
        assertEquals("Please provide a correct ordering of information for your event.",
                gray.getResponse("event marathon /to 2025-08-25 2359 /from 2025-08-25 2359"));
    }

    @Test
    public void getResponse_invalidDeadlineOrEventDateTime() {
        assertEquals("""
                Invalid date and time!
                Please use the format yyyy-MM-dd HHmm.""",
                gray.getResponse("deadline return book /by 2025-13-12 1900"));
        assertEquals("""
                Invalid date and time!
                Please use the format yyyy-MM-dd HHmm.""",
                gray.getResponse("event marathon /from 2025-13-25 2359 /to 2025-08-25 2359"));
    }

    @Test
    public void getResponse_noDate() {
        assertEquals("Please give a date.", gray.getResponse("date "));
    }

    @Test
    public void getResponse_invalidDate() {
        assertEquals("Invalid date! Please use the format yyyy-MM-dd.",
                gray.getResponse("date 2025-08-"));
    }

    @Test
    public void getResponse_noTasksOnDate() {
        assertEquals("There are no tasks on this date.",
                gray.getResponse("date 2025-08-26"));
    }

    @Test
    public void getResponse_invalidFind() {
        assertEquals("I don't understand what you mean.\n" + "Please enter a valid instruction.",
                gray.getResponse("find "));
    }

    @Test
    public void getResponse_findNoMatch() {
        assertEquals("No matching tasks can be found!",
                gray.getResponse("find book"));
    }

    @Test
    public void getResponse_invalidFindFreeTime() {
        assertEquals("I don't understand what you mean.\n" + "Please enter a valid instruction.",
                gray.getResponse("free "));
        assertEquals("I don't understand what you mean.\n" + "Please enter a valid instruction.",
                gray.getResponse("free two"));
    }

    @Test
    public void getResponse_bye() {
        assertEquals("Bye and see you soon!", gray.getResponse("bye"));
    }
}
