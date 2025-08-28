package gray.exception;

import gray.ui.Parser;

/**
 * Represents errors in task instantiation due to missing arguments.
 */
public class InvalidTaskException extends Exception {
    /**
     * Creates new InvalidTaskException.
     * Error message depends on type of task and the combination of missing arguments.
     */
    public InvalidTaskException(Parser.TaskType taskType, Parser.MissingInfo missingInfo) {
        super("Please provide a " + missingInfo.getMissingInfo() + " for your " + taskType.getTaskType() + ".");
    }
}
