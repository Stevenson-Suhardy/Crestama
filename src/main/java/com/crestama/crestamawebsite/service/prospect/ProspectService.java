package com.crestama.crestamawebsite.service.prospect;

import com.crestama.crestamawebsite.entity.Prospect;
import com.crestama.crestamawebsite.repository.ProspectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProspectService implements IProspect {
    private ProspectRepository prospectRepository;

    public ProspectService(ProspectRepository prospectRepository) {
        this.prospectRepository = prospectRepository;
    }

    @Override
    public List<Prospect> findAll() {
        return prospectRepository.findAll();
    }

    @Override
    public Prospect findById(Long id) {
        Prospect tempProspect = null;

        Optional<Prospect> result = prospectRepository.findById(id);

        if (result.isPresent()) {
            tempProspect = result.get();
        }

        return tempProspect;
    }
}
