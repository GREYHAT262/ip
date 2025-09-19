package gray.ui;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import gray.command.AddCommand;
import gray.command.ByeCommand;
import gray.command.DateCommand;
import gray.command.DeleteCommand;
import gray.command.InvalidCommand;
import gray.command.InvalidDateCommand;
import gray.command.InvalidDateTimeCommand;
import gray.command.InvalidTaskExceptionCommand;
import gray.command.ListCommand;
import gray.command.MarkCommand;
import gray.command.NoDateCommand;
import gray.command.NoIndexCommand;
import gray.command.UnmarkCommand;

public class ParserTest {
    @Test
    public void parse_stringWithValidList_returnListCommand() {
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
    }

    @Test
    public void parse_stringWithInvalidList_returnListCommand() {
        assertInstanceOf(InvalidCommand.class, Parser.parse("list two"));
        assertInstanceOf(InvalidCommand.class, Parser.parse("list 2 6"));
    }

    @Test
    public void parse_stringWithMark_returnMarkCommand() {
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 2"));
    }

    @Test
    public void parse_stringWithInvalidMark_returnNoIndexCommand() {
        assertInstanceOf(NoIndexCommand.class, Parser.parse("mark"));
        assertInstanceOf(NoIndexCommand.class, Parser.parse("mark two"));
    }

    @Test
    public void parse_stringWithUnmark_returnUnmarkCommand() {
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 2"));
    }

    @Test
    public void parse_stringWithInvalidUnmark_returnNoIndexCommand() {
        assertInstanceOf(NoIndexCommand.class, Parser.parse("unmark"));
        assertInstanceOf(NoIndexCommand.class, Parser.parse("unmark three"));
    }

    @Test
    public void parse_stringWithTodo_returnAddCommand() {
        assertInstanceOf(AddCommand.class, Parser.parse("todo borrow book"));
    }

    @Test
    public void parse_stringWithInvalidTodo_returnInvalidTaskExceptionCommand() {
        assertInstanceOf(InvalidTaskExceptionCommand.class, Parser.parse("todo"));
    }

    @Test
    public void parse_stringWithDeadline_returnAddCommand() {
        assertInstanceOf(AddCommand.class, Parser.parse("deadline return book /by 2025-08-26 2359"));
    }

    @Test
    public void parse_stringWithInvalidDeadline_returnInvalidTaskExceptionCommand() {
        assertInstanceOf(InvalidTaskExceptionCommand.class, Parser.parse("deadline"));
        assertInstanceOf(InvalidTaskExceptionCommand.class, Parser.parse("deadline return book"));
        assertInstanceOf(InvalidTaskExceptionCommand.class, Parser.parse("deadline /by 2025-08-26 2359"));
        assertInstanceOf(InvalidTaskExceptionCommand.class, Parser.parse("deadline /by"));
    }

    @Test
    public void parse_stringWithInvalidDeadlineDateTime_returnInvalidDateTimeCommand() {
        assertInstanceOf(InvalidDateTimeCommand.class, Parser.parse("deadline return book /by Sunday"));
    }

    @Test
    public void parse_stringWithEvent_returnAddCommand() {
        assertInstanceOf(AddCommand.class, Parser.parse(
                "event marathon /from 2025-08-26 0800 /to 2025-08-26 1200"));
    }

    @Test
    public void parse_stringWithInvalidEvent_returnInvalidTaskExceptionCommand() {
        assertInstanceOf(InvalidTaskExceptionCommand.class, Parser.parse("event"));
        assertInstanceOf(InvalidTaskExceptionCommand.class, Parser.parse("event marathon"));
        assertInstanceOf(InvalidTaskExceptionCommand.class, Parser.parse("event marathon /from 2025-08-26 0800"));
        assertInstanceOf(InvalidTaskExceptionCommand.class, Parser.parse("event marathon /to 2025-08-26 1200"));
        assertInstanceOf(InvalidTaskExceptionCommand.class, Parser.parse(
                "event /from 2025-08-26 0800 /to 2025-08-26 1200"));
    }

    @Test
    public void parse_stringWithInvalidEventDateTime_returnInvalidDateTimeCommand() {
        assertInstanceOf(InvalidDateTimeCommand.class, Parser.parse(
                "event marathon /from Sunday 8am /to Sunday 12pm"));
    }

    @Test
    public void parse_stringWithDelete_returnDeleteCommand() {
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 2"));
    }

    @Test
    public void parse_stringWithInvalidDelete_returnNoIndexCommand() {
        assertInstanceOf(NoIndexCommand.class, Parser.parse("delete"));
        assertInstanceOf(NoIndexCommand.class, Parser.parse("delete three"));
    }

    @Test
    public void parse_stringWithDate_returnDateCommand() {
        assertInstanceOf(DateCommand.class, Parser.parse("date 2025-08-26"));
    }

    @Test
    public void parse_stringWithNoDate_returnNoDateCommand() {
        assertInstanceOf(NoDateCommand.class, Parser.parse("date"));
    }

    @Test
    public void parse_stringWithInvalidDate_returnInvalidDateCommand() {
        assertInstanceOf(InvalidDateCommand.class, Parser.parse("date Sunday"));
    }

    @Test
    public void parse_stringWithBye_returnByeCommand() {
        assertInstanceOf(ByeCommand.class, Parser.parse("bye"));
    }

    @Test
    public void parse_stringWithInvalidInstruction_returnInvalidCommand() {
        assertInstanceOf(InvalidCommand.class, Parser.parse("blah"));
        assertInstanceOf(InvalidCommand.class, Parser.parse("5 4 3 2 1"));
    }
}
