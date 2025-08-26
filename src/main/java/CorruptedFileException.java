public class CorruptedFileException extends RuntimeException {
    public CorruptedFileException() {
        super("""
                Unfortunately, your file is corrupted.
                I have created a fresh copy for you!""");
    }
}
