package com.example.demo.utils;

import com.example.demo.common.exception.CustomException;
import com.github.pagehelper.Page;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public class BeanCopyUtil {


    private static final Map<String, BeanCopier> BEAN_COPIERS = new HashMap<>();

    public static <T>T copyProperties(Object sourceObj,Class<T> targetClass){
        if (sourceObj == null){
            return null;
        }
        T targetObj;
        try {
            targetObj = targetClass.newInstance();
        }catch (Exception e){
            throw new CustomException("属性拷贝创建对象失败");
        }
        BeanCopier copier = BEAN_COPIERS.get(sourceObj.getClass().toString() + targetClass);
        if (copier == null){
            copier = BeanCopier.create(sourceObj.getClass(),targetClass,false);
            BEAN_COPIERS.put(sourceObj.getClass().toString() + targetClass,copier);
        }
        copier.copy(sourceObj,targetObj,null);
        return targetObj;
    }

    public static <T> List<T> copyListProperties(List<?> sourceList, Class<T> targetClass){
        if (sourceList == null){
            return null;
        }
        List<T> resultList = new ArrayList<>(sourceList.size());
        sourceList.parallelStream().forEachOrdered(source -> {
            try {
                resultList.add(copyProperties(source,targetClass));
            }catch (Exception e){
                throw new CustomException("属性拷贝异常",e);
            }
        });
        if (sourceList instanceof Page<?>){
            Page<?> page = (Page<?>) sourceList;
            Page<T> resultPage = new Page<>();
            resultPage.addAll(resultList);

            resultPage.setPageNum(page.getPageNum());
            resultPage.setPageSize(page.getPageSize());
            resultPage.setOrderBy(page.getOrderBy());
            resultPage.setTotal(page.getTotal());
            resultPage.setStartRow(page.getStartRow());
            resultPage.setEndRow(page.getEndRow());
            return resultPage;
        }else {
            return resultList;
        }
    }
}
