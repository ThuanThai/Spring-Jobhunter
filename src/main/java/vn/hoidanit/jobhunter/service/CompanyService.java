package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.DTO.CompanyDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(CompanyDTO companyDTO) {
        Company c = new Company();
        c.setName(companyDTO.getName());
        c.setDescription(companyDTO.getDescription());
        c.setAddress(companyDTO.getAddress());
        c.setLogo(companyDTO.getLogo());

        return this.companyRepository.save(c);
    }

    public List<Company> handleGetAllCompanies() {
        return this.companyRepository.findAll();
    }

    public boolean handleDeleteCompany(long id) {
        Optional<Company> optional = this.companyRepository.findById(id);
        if (optional.isPresent()) {
            this.companyRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
