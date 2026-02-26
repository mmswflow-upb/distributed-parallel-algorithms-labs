package imagestoreq;

import java.util.Arrays;

public class ImageData {
    public final String filename;
    public final byte[] bytes;

    public ImageData(String filename, byte[] bytes) {
        this.filename = filename;
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "ImageData{filename='" + filename + "', bytes=" + (bytes == null ? 0 : bytes.length) + "}";
    }
}
