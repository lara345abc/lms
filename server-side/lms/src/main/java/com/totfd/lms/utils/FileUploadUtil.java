//package com.totfd.lms.utils;
//
//import com.amazonaws.services.s3.AmazonS3;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class FileUploadUtil {
//
//    private final AmazonS3 amazonS3;
//
//    private final String bucketName = System.getenv("DO_BUCKET_NAME"); // or inject via @Value
//
//    public String uploadFile(String path, MultipartFile file) throws IOException {
//        String fileName = path + "/" + file.getOriginalFilename();
//        amazonS3.putObject(bucketName, fileName, file.getInputStream(), null);
//        return amazonS3.getUrl(bucketName, fileName).toString();
//    }
//}


package com.totfd.lms.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileUploadUtil {

    private final AmazonS3 amazonS3;

    private final String bucketName = System.getenv("DO_BUCKET_NAME"); // or use @Value

    public String uploadFile(String path, MultipartFile file) throws IOException {
        String fileName = path + "/" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // Upload with public-read ACL
        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        amazonS3.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);

        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}

