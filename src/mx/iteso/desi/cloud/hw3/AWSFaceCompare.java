/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.iteso.desi.cloud.hw3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import java.nio.ByteBuffer;

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

    public AWSFaceCompare(String accessKey, String secretKey, Regions region,String srcBucket) {
        this.srcBucket = srcBucket;
        this.region = region;
        
        /*
         * @TODO
         * Build AmazonRekognition Object.
        */
        arek = null;  
    }

    public Face compare(ByteBuffer imageBuffer) {
        // TODO
        return null;
    }

}
