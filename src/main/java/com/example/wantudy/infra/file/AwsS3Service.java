package com.example.wantudy.infra.file;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import com.amazonaws.util.IOUtils;
import com.example.wantudy.domain.study.domain.Study;
import com.example.wantudy.domain.study.domain.StudyFile;
import com.example.wantudy.domain.study.dto.StudyFileDto;
import com.example.wantudy.domain.study.dto.StudyFileUploadDto;
import com.example.wantudy.domain.study.repository.StudyFileRepository;
import com.example.wantudy.domain.study.repository.StudyRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.*;
import java.net.URLEncoder;

import java.util.*;


@Slf4j
@Service
@NoArgsConstructor
@Transactional
public class AwsS3Service {

    private AmazonS3 s3Client;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyFileRepository studyFileRepository;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    /**
     * aws s3 로 파일 업로드
     *
     * @param file
     * @return
     */
    public StudyFileUploadDto upload(MultipartFile file) throws IOException {

        // 고유의 파일명 위해 UUID 사용
        String fileName = UUID.randomUUID() + "_uuid_" + file.getOriginalFilename();

        try {

//            SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());

            /** Warn message : No content length specified for stream data. Stream contents will be buffered in memory and could result in out of memory errors.
            **  방지 하기 위한 byte length 추가
            */
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            metadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

//             업로드 시 버킷에 날짜 폴더 생성하고 거기에 저장
//            PutObjectRequest putObjReq = new PutObjectRequest(bucket + "/" + date.format(new Date()), fileName, byteArrayIs, metadata);

            PutObjectRequest putObjReq = new PutObjectRequest(bucket, fileName, byteArrayIs, metadata).withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putObjReq);

            String filePath = s3Client.getUrl(bucket, fileName).toString();

            StudyFileUploadDto studyFileUploadDto = new StudyFileUploadDto();

            studyFileUploadDto.setFilepath(filePath);
            studyFileUploadDto.setS3FileName(fileName);

            return studyFileUploadDto;

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            log.error("uploadToAWS AmazonServiceException filePath={}, yyyymm={}, error={}", e.getMessage());
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            log.error("uploadToAWS SdkClientException filePath={}, error={}", e.getMessage());
        } catch (Exception e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            log.error("uploadToAWS SdkClientException filePath={}, error={}", e.getMessage());
        }

        return null;
    }

    public StudyFileDto.downloadFileResponse getObject(long studyFileId) throws IOException {

        Optional<StudyFile> studyFile = studyFileRepository.findById(studyFileId);
        String storedFileName = studyFile.get().getS3FileName();

        S3Object o = s3Client.getObject(new GetObjectRequest(bucket, storedFileName));

        S3ObjectInputStream objectInputStream = ((S3Object) o).getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String tempFileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");
        String[] fileNameSplit = tempFileName.split("_uuid_");
        String fileName = fileNameSplit[1];

        return new StudyFileDto.downloadFileResponse(bytes, fileName);
    }

    //버킷에서 파일 삭제
    public void deleteOnlyFile(long studyFileId) {
        Optional<StudyFile> studyFile = studyFileRepository.findById(studyFileId);
        String storedFileName = studyFile.get().getS3FileName();
        s3Client.deleteObject(bucket, storedFileName);
    }

    //버킷에서 파일 삭제
    public void deleteStudyAndFile(long studyId) {
        Optional<Study> study = studyRepository.findById(studyId);
        for (int i=0; i < study.get().getStudyFiles().size(); i++){
            this.deleteOnlyFile(study.get().getStudyFiles().get(i).getStudyFileId());
        }
    }
}