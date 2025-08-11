package com.unithon.s3;

import com.unithon.domain.user.domain.entity.User;
import com.unithon.domain.user.domain.repository.UserRepository;
import com.unithon.global.common.BaseResponse;
import com.unithon.global.error.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping(value = "/ads/{adId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<FileDTO.UploadResponse> uploadAdImage(
            @PathVariable Long adId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        FileDTO.UploadResponse body = s3Service.uploadAndSave(adId, file);

        return BaseResponse.onSuccess(SuccessStatus.FILE_UPLOAD_SUCCESS, body);
    }


}
