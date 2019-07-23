package com.bellsoft.updater.api.v1.domain.file;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "fim_client_file_tb")
public class FimClientFileTb {
    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //부트 2.x부터 TABLE이 기본값임
    private String fileId;
    @Column(name = "file_version")
    private String fileVersion;
    @Column(name = "file_nm")
    private String fileNm;
    @Column(name = "file_size")
    private Long fileSize;
    @Column(name = "file_hash")
    private String fileHash;
    @Column(name = "file_download_path")
    private String fileDownloadPath;
    @Column(name = "reg_id")
    private String regId;
    @Column(name = "reg_date")
    private LocalDateTime regDate;
    @Column(name = "mod_id")
    private String modId;
    @Column(name = "mod_date")
    private LocalDateTime modDate;
    
    @Column(name = "ip_address")
    private long ipAddress;
    
    @Column(columnDefinition = "UNSIGNED INT(11)")
    public long getIpAddress()
    {
        return ipAddress;
    }
}
