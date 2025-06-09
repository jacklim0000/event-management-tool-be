package com.unitask.repository;

import com.unitask.dto.annoucement.AnnouncementTuple;
import com.unitask.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @Query("SELECT a.id as id, " +
            "a.title as title, " +
            "a.subject.name as subjectName, " +
            "a.subject.code as subjectCode, " +
            "a.description as description, " +
            "a.postedDate as postedDate, " +
            "a.subject.color as color " +
            "FROM Announcement a " +
            "WHERE (:ownerId IS NULL OR a.owner.id = :ownerId) " +
            "AND (:search IS NULL or a.title LIKE %:search%)")
    Page<AnnouncementTuple> findListing(Pageable pageable, Long ownerId, String search);

    @Query("SELECT a.id as id, " +
            "a.title as title, " +
            "a.subject.name as subjectName, " +
            "a.subject.code as subjectCode, " +
            "a.description as description, " +
            "a.postedDate as postedDate, " +
            "a.subject.color as color " +
            "FROM Announcement a " +
            "LEFT JOIN StudentSubject ss ON (ss.subject.id = a.subject.id AND ss.user.id = :ownerId)" +
            "WHERE ss IS NOT NULL " +
            "AND (:search IS NULL or a.title LIKE %:search%)"
    )
    Page<AnnouncementTuple> findStudentListing(Pageable pageable, Long ownerId, String search);
}
