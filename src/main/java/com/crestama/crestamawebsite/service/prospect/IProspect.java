package com.crestama.crestamawebsite.service.prospect;

import com.crestama.crestamawebsite.entity.Prospect;

import java.util.List;

public interface IProspect {
    List<Prospect> findAll();

    Prospect findById(Long id);
}
