package com.unitask.dao;

import com.unitask.constant.error.AnnouncementErrorConstant;
import com.unitask.dto.annoucement.AnnouncementTuple;
import com.unitask.entity.Announcement;
import com.unitask.exception.ServiceAppException;
import com.unitask.repository.AnnouncementRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AnnouncementDAO {

    private final AnnouncementRepository announcementRepository;

    public Announcement save(Announcement announcement) {
        if (Objects.isNull(announcement)) {
            return null;
        }

        return announcementRepository.save(announcement);
    }

    public Announcement findById(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }

        return announcementRepository.findById(id).orElseThrow(() -> new ServiceAppException(HttpStatus.BAD_REQUEST, AnnouncementErrorConstant.NOT_FOUND));
    }

    public Page<AnnouncementTuple> findListing(Pageable pageable, Long ownerId,String search) {
        return announcementRepository.findListing(pageable, ownerId, search);
    }

    public Page<AnnouncementTuple> findStudentListing(Pageable pageable, Long ownerId,String search) {
        return announcementRepository.findStudentListing(pageable, ownerId, search);
    }
}
