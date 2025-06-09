package com.unitask.service.impl;

import com.unitask.constant.Enum.UserRole;
import com.unitask.dao.*;
import com.unitask.dto.DocumentPageRequest;
import com.unitask.dto.PageRequest;
import com.unitask.dto.document.DocumentListingTuple;
import com.unitask.entity.User.AppUser;
import com.unitask.entity.assessment.Assessment;
import com.unitask.entity.assessment.AssessmentSubmission;
import com.unitask.service.ContextService;
import com.unitask.service.DocumentService;
import com.unitask.util.OssUtil;
import com.unitask.util.PageUtil;
import com.unitask.util.PageWrapperVO;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl extends ContextService implements DocumentService {

    @Autowired
    private AssessmentSubmissionDAO assessmentSubmissionDAO;
    @Autowired
    private AppUserDAO appUserDAO;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private OssUtil ossUtil;
    @Autowired
    private SubjectDAO subjectDAO;
    @Autowired
    private AssessmentDao assessmentDao;


    public Map<Assessment, Optional<AssessmentSubmission>> getLatestSubmissionsByAssessment(List<AssessmentSubmission> submissions) {
        return submissions.stream().collect(Collectors.groupingBy(AssessmentSubmission::getAssessment,
                Collectors.maxBy(Comparator.comparing(AssessmentSubmission::getSubmissionDate))));
    }


    @Override
    public PageWrapperVO getListing(DocumentPageRequest documentPageRequest) {
        PageRequest pageRequest = new PageRequest(documentPageRequest.getPage(), documentPageRequest.getPageSize(), documentPageRequest.getSearch(), documentPageRequest.getSort());
        Pageable pageable = PageUtil.pageable(pageRequest);
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        if (appUser.getUserRole().equals(UserRole.LECTURER)) {
            Page<DocumentListingTuple> subjectList = subjectDAO.documentListingLecturer(documentPageRequest.getSearch(),
                    documentPageRequest.getAssessmentName(), documentPageRequest.getSubjectName(), appUser.getId(), pageable);
            return new PageWrapperVO(subjectList, subjectList.getContent().stream().filter(content -> StringUtils.isNotBlank(content.getPath())).toList());
        }
        Page<DocumentListingTuple> assessmentSubmissionListing = assessmentSubmissionDAO.
                getAllAssessmentSubmissionsBaseOnIndividualAndGroup(documentPageRequest.getSearch(), documentPageRequest.getAssessmentName(),
                        documentPageRequest.getSubjectName(), appUser.getId(), pageable);
        return new PageWrapperVO(assessmentSubmissionListing, assessmentSubmissionListing.getContent().stream().filter(content -> StringUtils.isNotBlank(content.getPath())).toList());
    }

@Override
public void downloadFile(String path, HttpServletResponse response) {
    try {
        URL url = ossUtil.getObjectURL(path);

        if (url == null) {
            throw new IOException("Unable to retrieve URL for the specified path.");
        }
        String fileName = new File(path).getName();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        try (InputStream inputStream = url.openStream(); OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error streaming the file content: " + e.getMessage(), e);
        }
    } catch (IOException e) {
        throw new RuntimeException("Failed to download file: " + e.getMessage(), e);
    }
}


}
