package com.unitask.service;

import com.unitask.dto.PageRequest;
import com.unitask.dto.annoucement.AnnouncementRequest;
import com.unitask.dto.annoucement.AnnouncementResponse;
import com.unitask.dto.annoucement.AnnouncementTuple;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

public interface AnnouncementService {

    AnnouncementResponse create(AnnouncementRequest announcementRequest) throws MessagingException;

    AnnouncementResponse read(Long id);

    AnnouncementResponse update(Long id, AnnouncementRequest announcementRequest);

    void delete();

    Page<AnnouncementTuple> list(PageRequest pageRequest);
}
