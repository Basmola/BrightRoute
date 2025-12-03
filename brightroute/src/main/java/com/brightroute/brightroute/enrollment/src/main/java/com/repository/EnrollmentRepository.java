package enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import enrollment.model.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
}
