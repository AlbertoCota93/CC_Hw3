/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.iteso.desi.cloud.hw3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

/**
 *
 * @author parres
 */
public class AWSFaceCompare {

    String srcBucket;
    AmazonRekognition arek;
    String accessKey;
    String secretKey;
    Regions region;
    AmazonS3 s3;
    AWSCredentials cred;
    EndpointConfiguration end;
    ByteBuffer targetImg=null;
    Image src;
    Image trg;
    S3Object img;
    ByteBuffer buffersito;
    public AWSFaceCompare(String accessKey, String secretKey, Regions region,String srcBucket) {
        this.srcBucket = Config.srcBucket;
        this.region = region;
        
        /*
         * @TODO
         * Build AmazonRekognition Object.
        */
        this.cred = new BasicAWSCredentials("AKIAI73YSS2GBNQGOBRA","0BeWwXrrr62I9Ykm9BLGZern5vcExN7nTrmL4rsP"); 
        this.end = new EndpointConfiguration("rekognition.us-west-2.amazonaws.com", "us-west-2");
        arek =  AmazonRekognitionClientBuilder.standard()
                .withEndpointConfiguration(end).withCredentials(new AWSStaticCredentialsProvider(cred))
                .build();
        s3= AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(cred)).
                    withRegion(Config.amazonRegion).build();
    }

    public Face compare(ByteBuffer imageBuffer) throws IOException {
        // TODO
        for (S3ObjectSummary sum : S3Objects.withPrefix(s3, "fotoscota", "")){
            img= s3.getObject(Config.srcBucket,sum.getKey());
            InputStream target = new BufferedInputStream(img.getObjectContent());
            buffersito = ByteBuffer.wrap(IOUtils.toByteArray(target));
            
            src = new Image().withBytes(imageBuffer);
            trg = new Image().withBytes(buffersito);
            
            CompareFacesRequest req = new CompareFacesRequest()
                    .withSourceImage(src)
                    .withTargetImage(trg)
                    .withSimilarityThreshold((float)85);
            CompareFacesResult res = arek.compareFaces(req);
            List <CompareFacesMatch> match = res.getFaceMatches();
            for(CompareFacesMatch mat : match){
                ComparedFace face = mat.getFace();
                
                if (face.getConfidence() > 85.0){
                    return new Face(sum.getKey(),face.getConfidence());
                }
            }
        }
        return null;
    }

}
