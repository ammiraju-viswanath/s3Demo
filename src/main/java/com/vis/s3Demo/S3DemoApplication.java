package com.vis.s3Demo;

import com.vis.service.S3Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
@ComponentScan("com.vis")

public class S3DemoApplication {


	public S3DemoApplication(S3Service s3Service) {
		this.s3Service =s3Service;
		String bucketName ="s3-demo-vis-1";
		this.s3Service.createBucket(bucketName);
		String filePath ="C:\\Users\\ammir\\Desktop\\hello_text.txt";
		String fileName= "hello_text.txt";
		String downloadPath="/download/to/files/downloaded-file.txt";
		//upload a file to s3
		s3Service.uploadFile(bucketName,fileName,filePath);
		System.out.println("file is successfully uploaded");
		//download the file
		s3Service.downloadFile(bucketName,fileName,downloadPath);
		System.out.println("file is successfully downloaded");
		//list files in s3
		List<String> files = s3Service.listFiles(bucketName);
		for(String file:files){
			System.out.println(file);
		}

		this.s3Service.createBucket("s3-demo-vis-2-bucket-name");
		s3Service.listS3Buckets().forEach(System.out::println);
		//RemoveS3
		//s3Service.removeS3Bucket(bucketName);
		//s3Service.removeS3Bucket("s3-demo-vis-2-bucket-name");


	}


	private final S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(S3DemoApplication.class, args);
	}

}
