package com.example.demo.mapper;

import com.example.demo.common.dto.AttentionForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */

@Mapper
public interface AttentionMapper {

    void setAttention(AttentionForm attentionForm);

    void deleteAttention(AttentionForm attentionForm);

    List<String> getAttention(AttentionForm attentionForm);
}
