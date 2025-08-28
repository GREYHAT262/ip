package gray.exception;

import gray.ui.Parser;

public class InvalidTaskException extends Exception {
    public InvalidTaskException(Parser.TaskType taskType, Parser.MissingInfo missingInfo) {
        super("Please provide a " + missingInfo.getMissingInfo() + " for your " + taskType.getTaskType() + ".");
    }
}
