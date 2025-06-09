package com.unitask.service;

import com.unitask.dto.PageRequest;
import com.unitask.dto.annoucement.AnnouncementResponse;
import com.unitask.dto.annoucement.AnnouncementTuple;
import org.springframework.data.domain.Page;

public interface StudentAnnouncementService {

    AnnouncementResponse read(Long id);

    Page<AnnouncementTuple> list(PageRequest pageRequest);

}
