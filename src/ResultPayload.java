import java.io.Serializable;

class ResultPayload implements Serializable{
    private boolean isPath;
    public ResultPayload(boolean isPath){
        this.isPath = isPath;
    }

    public boolean getIsPath(){
        return this.isPath;
    }

    @Override
    public String toString() {
        return "ResultPayload{" +
                "isPath=" + isPath +
                '}';
    }
}