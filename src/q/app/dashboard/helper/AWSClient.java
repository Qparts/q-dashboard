package q.app.dashboard.helper;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class AWSClient {

    private static final String MIME_TYPE = "image/png";


    //run asynchronously
        public static void uploadImage(InputStream is, String fileName, String bucket){
            CompletableFuture.runAsync(()->{
                try {
                    AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("eu-central-1").build();
                    byte[] bytes = Helper.inputStreamToBytesArray(is);
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentType(MIME_TYPE);
                    metadata.addUserMetadata("x-amz-meta-title", "someTitle");
                    metadata.setContentLength(bytes.length);
                    PutObjectRequest request = new PutObjectRequest(bucket, fileName, bais , metadata);
                    request.setCannedAcl(CannedAccessControlList.PublicRead);
                    s3.putObject(request);
                }
                catch(Exception e) {
                    System.out.println("some exception");
                    e.printStackTrace();
                }
            });

        }

}
