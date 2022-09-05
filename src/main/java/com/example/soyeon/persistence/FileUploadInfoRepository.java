package com.example.soyeon.persistence;

import com.example.soyeon.domain.Board.FileUploadEntity;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface FileUploadInfoRepository extends JpaRepository<FileUploadEntity, Long> {

    //finby 튜플을 찾겠다.
    //boardSeq : boardSeq컬럼에 데이터를 찾겠다.

    FileUploadEntity findByBoardSeq(Long seq);
}