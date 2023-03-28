package com.example.demo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEmailDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 赛事Id
     */
    private String eventId;

    private String mailText;

    private MultipartFile file;
}
