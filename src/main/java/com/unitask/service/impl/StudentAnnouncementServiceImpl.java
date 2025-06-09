package com.unitask.service.impl;

import com.unitask.dao.AnnouncementDAO;
import com.unitask.dto.PageRequest;
import com.unitask.dto.annoucement.AnnouncementResponse;
import com.unitask.dto.annoucement.AnnouncementTuple;
import com.unitask.entity.Announcement;
import com.unitask.mapper.AnnouncementMapper;
import com.unitask.service.ContextService;
import com.unitask.service.StudentAnnouncementService;
import com.unitask.util.PageUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentAnnouncementServiceImpl extends ContextService implements StudentAnnouncementService {

    private final AnnouncementDAO announcementDAO;

    @Override
    public AnnouncementResponse read(Long id) {
        Announcement announcement = announcementDAO.findById(id);
        return AnnouncementMapper.INSTANCE.toResponse(announcement);
    }

    @Override
    public Page<AnnouncementTuple> list(PageRequest pageRequest) {
        Pageable pageable = PageUtil.pageable(pageRequest);
        return announcementDAO.findListing(pageable, null, pageRequest.getSearch());
    }
}
