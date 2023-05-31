package co.lemnisk.consumer.controller.test;


public class ClientRequest {

    String arn;
    String bucketName;
    String region;

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public ClientRequest(String arn, String bucketName, String region) {
        this.arn = arn;
        this.bucketName = bucketName;
        this.region = region;
    }

    @Override
    public String toString() {
        return "ClientRequest{" +
                "arn='" + arn + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
