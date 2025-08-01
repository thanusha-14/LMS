package com.lms.repository;

import com.lms.model.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByCourseId(Long courseId);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.id = :courseId AND a.deadline > :now")
    List<Assignment> findUpcomingAssignments(@Param("courseId") Long courseId, 
                                           @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.id = :courseId AND a.deadline < :now")
    List<Assignment> findPastAssignments(@Param("courseId") Long courseId, 
                                       @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.instructor.id = :instructorId")
    Page<Assignment> findByInstructor(@Param("instructorId") Long instructorId, 
                                    Pageable pageable);
    
    @Query("SELECT a FROM Assignment a WHERE a.deadline BETWEEN :startDate AND :endDate")
    List<Assignment> findAssignmentsInPeriod(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
                                           
    @Query("SELECT COUNT(s) FROM Assignment a JOIN a.submissions s WHERE a.id = :assignmentId")
    long getSubmissionCount(@Param("assignmentId") Long assignmentId);
}
