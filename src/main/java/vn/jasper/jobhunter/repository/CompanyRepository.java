package vn.jasper.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.jasper.jobhunter.domain.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
