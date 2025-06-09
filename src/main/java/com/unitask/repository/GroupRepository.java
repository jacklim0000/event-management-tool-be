package com.unitask.repository;

import com.unitask.dto.group.GroupMemberTuple;
import com.unitask.dto.group.GroupTuple;
import com.unitask.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {


    @Query("select g.id as id," +
            "g.assessment.subject.code as subjectCode, " +
            "g.assessment.name as assignmentName," +
            "g.assessment.maxMember as maxMember," +
            "g.name as groupName," +
            "(SELECT COUNT(sa) FROM g.studentAssessment sa WHERE sa.group.id = g.id ) as memberCount " +
            "from Group g where (:name is null or g.name like %:name%) " +
            "AND g.assessment.subject.owner.id = :subjectOwner ")
    Page<GroupTuple> findLecturerGroup(String name, Pageable pageable, Long subjectOwner);

    @Query("select g.id as id," +
            "g.assessment.subject.code as subjectCode, " +
            "g.assessment.name as assignmentName," +
            "g.assessment.maxMember as maxMember," +
            "g.name as groupName," +
            "(SELECT COUNT(sa) FROM g.studentAssessment sa WHERE sa.group.id = g.id ) as memberCount " +
            "from Group g  " +
            "LEFT JOIN g.studentAssessment ga " +
            "WHERE ga.user.id = :userId " +
            "AND (:name is null or g.name like %:name%)")
    Page<GroupTuple> findMyGroup(String name, Pageable pageable, Long userId);


    @Query("select g from Group g inner join g.studentAssessment studentAssessment where studentAssessment.user.id = ?1")
    List<Group> findByStudentAssessment_User_Id(Long id);

    @Query("select sa.group FROM StudentAssessment sa WHERE sa.assessment.id=:assessmentId AND sa.user.id=:userId ")
    List<Group> findByAssessmentIdAndUserId(Long assessmentId, Long userId);

    @Query("select sa.user.id as id, sa.user.name as name FROM StudentAssessment sa WHERE sa.group.id = :groupId ")
    List<GroupMemberTuple> findMember(Long groupId);

    @Query("select g.id as id," +
            "g.assessment.subject.code as subjectCode, " +
            "g.assessment.name as assignmentName," +
            "g.assessment.maxMember as maxMember," +
            "g.name as groupName," +
            "(SELECT COUNT(sa) FROM g.studentAssessment sa WHERE sa.group.id = g.id ) as memberCount " +
            "from Group g where " +
            "g.assessment.id in :ids " +
            "AND g.openForPublic = :openForPublic AND g.locked = :locked ")
    Page<GroupTuple> findByAssessment_IdIn(Pageable pageable, Collection<Long> ids, Boolean openForPublic, Boolean locked);

}
