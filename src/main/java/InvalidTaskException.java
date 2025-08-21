public class InvalidTaskException extends Exception {
    public InvalidTaskException(Gray.TaskType taskType, Gray.MissingInfo missingInfo) {
        super("Please provide a " + missingInfo.getMissingInfo() + " for your " + taskType.getTaskType() + ".");
    }
}
