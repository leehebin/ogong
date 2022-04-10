package com.ogong.service.study;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ogong.common.Search;
import com.ogong.service.domain.Study;

@Repository
@Mapper
public interface TestStudyMapper {

   void addStudy(Study study) throws Exception;
   
   Study getStudy(int studyNo) throws Exception;
   
   void entranceStudy(int studyNo) throws Exception;
   
   void leaveStudy(int studyNo) throws Exception;
   
   List<Study> getStudyList(Search search) throws Exception;
   
   int getTotalCount(Search search) throws Exception;
   
   void updateMember(@Param("studyNo") int studyNo, @Param("num") int num) throws Exception;

}
