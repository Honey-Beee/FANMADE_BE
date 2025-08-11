package com.unithon.s3;

import lombok.Builder;
import lombok.*;

public class FileDTO {

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class UploadResponse {
            private String imageUrl;
            private String originalFileName;
            private long size;
        }

}
