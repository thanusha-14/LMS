package com.lms.repository;

import com.lms.model.Course;
import com.lms.model.CourseStatus;
import com.lms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByInstructor(User instructor);
    
    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId AND c.status = :status")
    List<Course> findByInstructorAndStatus(@Param("instructorId") Long instructorId, 
                                         @Param("status") CourseStatus status);
    
    Page<Course> findByStatus(CourseStatus status, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.title LIKE %:keyword% OR c.description LIKE %:keyword%")
    Page<Course> searchCourses(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(e) FROM Course c JOIN c.enrollments e WHERE c.id = :courseId")
    long getEnrollmentCount(@Param("courseId") Long courseId);
    
    @Query("SELECT c FROM Course c WHERE c.maxStudents > (SELECT COUNT(e) FROM c.enrollments e)")
    List<Course> findAvailableCourses();
}
