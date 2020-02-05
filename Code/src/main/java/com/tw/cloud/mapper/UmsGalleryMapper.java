package com.tw.cloud.mapper;

import com.tw.cloud.bean.UmsGallery;
import com.tw.cloud.bean.UmsGalleryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsGalleryMapper {
    int countByExample(UmsGalleryExample example);

    int deleteByExample(UmsGalleryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UmsGallery record);

    int insertSelective(UmsGallery record);

    List<UmsGallery> selectByExampleWithBLOBs(UmsGalleryExample example);

    List<UmsGallery> selectByExample(UmsGalleryExample example);

    UmsGallery selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UmsGallery record, @Param("example") UmsGalleryExample example);

    int updateByExampleWithBLOBs(@Param("record") UmsGallery record, @Param("example") UmsGalleryExample example);

    int updateByExample(@Param("record") UmsGallery record, @Param("example") UmsGalleryExample example);

    int updateByPrimaryKeySelective(UmsGallery record);

    int updateByPrimaryKeyWithBLOBs(UmsGallery record);

    int updateByPrimaryKey(UmsGallery record);
}