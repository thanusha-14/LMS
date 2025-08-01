package com.lms.repository;

import com.lms.model.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    
    List<Submission> findByAssignmentId(Long assignmentId);
    
    List<Submission> findByStudentId(Long studentId);
    
    Optional<Submission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);
    
    @Query("SELECT s FROM Submission s WHERE s.assignment.id = :assignmentId AND s.grade IS NULL")
    List<Submission> findUngradedSubmissions(@Param("assignmentId") Long assignmentId);
    
    @Query("SELECT s FROM Submission s WHERE s.assignment.course.instructor.id = :instructorId AND s.grade IS NULL")
    Page<Submission> findPendingGradingByInstructor(@Param("instructorId") Long instructorId, 
                                                   Pageable pageable);
    
    @Query("SELECT AVG(s.grade) FROM Submission s WHERE s.assignment.id = :assignmentId AND s.grade IS NOT NULL")
    Double getAverageGrade(@Param("assignmentId") Long assignmentId);
    
    @Query("SELECT COUNT(s) FROM Submission s WHERE s.assignment.id = :assignmentId AND s.grade >= :minGrade")
    long countPassingSubmissions(@Param("assignmentId") Long assignmentId, 
                               @Param("minGrade") Integer minGrade);
                               
    @Query("SELECT s FROM Submission s WHERE s.student.id = :studentId AND s.grade IS NOT NULL ORDER BY s.grade DESC")
    List<Submission> findTopSubmissionsByStudent(@Param("studentId") Long studentId, 
                                               Pageable pageable);
}
