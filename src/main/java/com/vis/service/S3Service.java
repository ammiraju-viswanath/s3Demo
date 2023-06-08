package com.vis.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.vis.configuration.S3Config;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service {
    @Autowired
    AmazonS3 s3;

    public Bucket createBucket(String bucketName){
        return listS3Buckets().stream().filter(s->s.getName().equals(bucketName)).findFirst().orElseGet(()->s3.createBucket(bucketName));
    }

    public List<Bucket> listS3Buckets(){
        return s3.listBuckets();
    }

    public void removeS3Bucket(String bucketName){
        s3.listObjects(bucketName).getObjectSummaries().stream().filter(e->e!=null).forEach(e->s3.deleteObject(bucketName,e.getKey()));
        s3.deleteBucket(bucketName);
    }


    public void uploadFile(String bucketName, String fileName, String filePath){
        s3.putObject(bucketName,fileName, new File(filePath));

    }

    public List<String> listFiles(String bucketName){
       ListObjectsV2Result result = s3.listObjectsV2(bucketName);
       List<String> files = new ArrayList<>();
       for(S3ObjectSummary summary :result.getObjectSummaries()){
           files.add(summary.getKey());
       }
       return files;
    }
    public String deleteFile(String bucketName, String fileName){
        s3.deleteObject(bucketName, fileName);
        return fileName + " removed successfully !";
    }

    @SneakyThrows
    public void downloadFile(String bucketName, String fileName, String downloadPath){
        S3Object object =s3.getObject(bucketName,fileName);
        S3ObjectInputStream inputStream = object.getObjectContent();
        org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, new File(downloadPath));
    }

}
