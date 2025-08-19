public class InvalidTaskException extends Exception {
    public InvalidTaskException(String taskType, String missingInfo) {
        super("Please provide a " + missingInfo + " for your " + taskType + ".");
    }
}
