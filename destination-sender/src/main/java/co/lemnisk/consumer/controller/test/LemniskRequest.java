package co.lemnisk.consumer.controller.test;

public class LemniskRequest {

    String bucketName;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public LemniskRequest(){
    }

    public LemniskRequest(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public String toString() {
        return "LemniskRequest{" +
                "bucketName='" + bucketName + '\'' +
                '}';
    }
}
