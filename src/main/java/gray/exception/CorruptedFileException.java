package gray.exception;

/**
 * Represents errors in file format.
 */
public class CorruptedFileException extends RuntimeException {
    /**
     * Creates new CorruptedFileException.
     */
    public CorruptedFileException() {
        super("""
                Unfortunately, your file is corrupted.
                I have created a fresh copy for you!""");
    }
}
