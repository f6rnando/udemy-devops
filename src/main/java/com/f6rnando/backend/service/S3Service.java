package com.f6rnando.backend.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/************************************
 Created by f6rnando@gmail.com
 2018-03-23
 *************************************/

@Service
public class S3Service {

    // The application logger
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    private static final String PROFILE_PICTURE_FILE_NAME = "profilePicture";

    @Value("${aws.s3.root.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Value("${image.store.tmp.folder}")
    private String tempImageStore;

    @Autowired
    private AmazonS3 s3Client;

    /**
     * It stores the given file name in S3
     * @param uploadedFile The multipart file uploaded by the user
     * @param username The username for which to upload this file
     * @return the key URL where the profile image is located on S3
     * @throws IOException
     */
    public String storeProfileImage (MultipartFile uploadedFile, String username) throws IOException {
        String profileImageUrl = null;

        if (uploadedFile != null && !uploadedFile.isEmpty()) {
            byte[] bytes = uploadedFile.getBytes();

            File tmpImageStoredFolder = new File(tempImageStore + File.separatorChar + username);

            if (!tmpImageStoredFolder.exists()) {
                logger.info("Creating the temporary root for the S3 assets");
                tmpImageStoredFolder.mkdirs();
            }

            File tmpProfileImageFile = new File(
                    tmpImageStoredFolder.getAbsolutePath() + File.separatorChar +
                            PROFILE_PICTURE_FILE_NAME + "." +
                            FilenameUtils.getExtension(uploadedFile.getOriginalFilename())
            );

            logger.info("Temporary file will be saved to {}", tmpProfileImageFile.getAbsolutePath());

            try(
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(new File(tmpProfileImageFile.getAbsolutePath())))) {
                    stream.write(bytes);
            }

            profileImageUrl = this.storeProfileImageToS3(tmpProfileImageFile, username);

            // Clean up the temp folder
            tmpProfileImageFile.delete();
        }

        return profileImageUrl;
    }

    /*
     *   PRIVATE METHODS
     */

    /**
     * <p>Please note the bucket URL doesn't contains the name of the bucket</p>
     * @param bucketName The bucket name
     * @return The root URL where the bucket name is located.
     */
    private String ensureBucketExists(String bucketName) {
        String bucketUrl = null;

        try {
            if (!s3Client.doesBucketExistV2(bucketName)) {
                logger.info("Bucket {} doesn't exists... Creating one");
                s3Client.createBucket(bucketName);
                logger.info("Created bucket: {}", bucketName);
            }

            // TODO change to return the name of the bucket
            bucketUrl = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName));
        } catch (SdkClientException e) {
            logger.error("An error occurred while connecting to S3. Will not execute action for bucket: {}", bucketName, e);
        }

        logger.debug("Returning {}.", bucketUrl);
        return bucketUrl;
    }

    /**
     * @method storeProfileImageToS3
     * @param resource The profile image File to be stored
     * @param username The username
     * @return {String} The URL path for the resource stored
     */
    private String storeProfileImageToS3(File resource, String username) {
        String resourceUrl = null;

        if (!resource.exists()) {
            logger.error("The file {} does not exist", resource.getAbsolutePath());
            throw new IllegalArgumentException("The file " + resource.getAbsolutePath() + " doesn't exist");
        }

        String rootBucketUrl = this.ensureBucketExists(bucketName);

        if (rootBucketUrl != null) {
            AccessControlList accessControlList = new AccessControlList();
            accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);

            String key = username + "/" + PROFILE_PICTURE_FILE_NAME + "." + FilenameUtils.getExtension(resource.getName());

            try {
                s3Client.putObject(new PutObjectRequest(bucketName, key, resource).withAccessControlList(accessControlList));
                resourceUrl = s3Client.getUrl(bucketName, key).getPath();
            } catch (AmazonClientException e) {
                logger.error("A client exception occurred while trying to store the profile image {} on S3. " +
                        "The profile image won't be stored", resource.getAbsolutePath(), e);
            }
        } else {
            logger.error("The bucket {} does not exist and the application was not able to create it.", rootBucketUrl);
            logger.error("The profile image won't be stored with the profile.");
        }

        return resourceUrl;
    }
}
