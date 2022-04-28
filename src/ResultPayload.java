import java.io.ByteArrayInputStream;
import java.io.Serializable;

class ResultPayload implements Serializable{
    private final boolean isPath;
    private final byte[] image;
    public ResultPayload(boolean isPath, byte[] image){
        this.isPath = isPath;
        this.image = image;
    }

    public boolean getIsPath(){
        return this.isPath;
    }
    public byte[] getImage() { return this.image; }

    @Override
    public String toString() {
        return "ResultPayload{" +
                "isPath=" + isPath +
                ", image=" + image +
                '}';
    }
}