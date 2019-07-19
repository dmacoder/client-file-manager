package com.bellsoft.updater.api.v1.domain.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileVO {
    private String fileId;
    private String fileVersion;
    private String fileNm;
    private String fileSize;
    private String fileHash;
    private String fileDownloadPath;
    private String regId;
    private String regDate;
    private String modId;
    private String modDate;
    private String ipAddress;
}
