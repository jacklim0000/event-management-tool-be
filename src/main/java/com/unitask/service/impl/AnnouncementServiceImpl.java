package com.unitask.service.impl;

import com.unitask.constant.Enum.UserRole;
import com.unitask.dao.AnnouncementDAO;
import com.unitask.dao.AppUserDAO;
import com.unitask.dao.StudentAssessmentDao;
import com.unitask.dao.SubjectDAO;
import com.unitask.dto.PageRequest;
import com.unitask.dto.annoucement.AnnouncementRequest;
import com.unitask.dto.annoucement.AnnouncementResponse;
import com.unitask.dto.annoucement.AnnouncementTuple;
import com.unitask.entity.Announcement;
import com.unitask.entity.StudentAssessment;
import com.unitask.entity.Subject;
import com.unitask.entity.User.AppUser;
import com.unitask.mapper.AnnouncementMapper;
import com.unitask.service.AnnouncementService;
import com.unitask.service.ContextService;
import com.unitask.util.EmailUtil;
import com.unitask.util.PageUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AnnouncementServiceImpl extends ContextService implements AnnouncementService {

    private final AppUserDAO appUserDAO;
    private final SubjectDAO subjectDAO;
    private final AnnouncementDAO announcementDAO;
    private final EmailUtil emailUtil;
    private final StudentAssessmentDao studentAssessmentDao;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public AnnouncementResponse create(AnnouncementRequest announcementRequest) throws MessagingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Subject subject = subjectDAO.findById(announcementRequest.getSubjectId());
        Announcement announcement = AnnouncementMapper.INSTANCE.toEntity(announcementRequest, subject, appUser);
        announcementDAO.save(announcement);
        List<StudentAssessment> studentAssessmentList = studentAssessmentDao.findBySubject(subject.getId());
        if (CollectionUtils.isNotEmpty(studentAssessmentList)) {
            List<String> studentEmailList = studentAssessmentList.stream()
                    .map(StudentAssessment::getUser)
                    .map(AppUser::getEmail)
                    .distinct()
                    .filter(email -> EMAIL_PATTERN.matcher(email).matches())
                    .toList();
            HashMap<String, Object> values = new HashMap<>();
            values.put("title", announcement.getTitle() + " - " + announcement.getDescription());
            values.put("dateTime", formatter.format(announcement.getPostedDate()));
            values.put("subject", subject.getCode() + " " + subject.getName());
            emailUtil.emailBlast("Announcement", studentEmailList, "Announcement", values);
        }
        return AnnouncementMapper.INSTANCE.toResponse(announcement);
    }

    @Override
    public AnnouncementResponse read(Long id) {
        Announcement announcement = announcementDAO.findById(id);
        return AnnouncementMapper.INSTANCE.toResponse(announcement);
    }

    @Override
    public AnnouncementResponse update(Long id, AnnouncementRequest announcementRequest) {
        Announcement announcement = announcementDAO.findById(id);
        Subject subject = subjectDAO.findById(announcementRequest.getSubjectId());
        AnnouncementMapper.INSTANCE.toEntity(announcement, announcementRequest, subject);
        announcementDAO.save(announcement);
        return AnnouncementMapper.INSTANCE.toResponse(announcement);
    }

    @Override
    public void delete() {

    }

    @Override
    public Page<AnnouncementTuple> list(PageRequest pageRequest) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Pageable pageable = PageUtil.pageable(pageRequest, Sort.by("postedDate").descending());
        if (UserRole.LECTURER.equals(appUser.getUserRole())) {
            return announcementDAO.findListing(pageable, appUser.getId(), pageRequest.getSearch());
        } else {
            return announcementDAO.findStudentListing(pageable, appUser.getId(), pageRequest.getSearch());
        }
    }
}
