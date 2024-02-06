package com.umc.lifesharing.inquiry.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.inquiry.converter.InquiryConverter;
import com.umc.lifesharing.inquiry.dto.InquiryRequestDTO;
import com.umc.lifesharing.inquiry.dto.InquiryResponseDTO;
import com.umc.lifesharing.inquiry.entity.Inquiry;
import com.umc.lifesharing.inquiry.entity.InquiryImage;
import com.umc.lifesharing.inquiry.repository.InquiryImageRepository;
import com.umc.lifesharing.inquiry.repository.InquiryRepository;
import com.umc.lifesharing.s3.AwsS3Service;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InquiryService {
    private final AwsS3Service awsS3Service;
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final InquiryImageRepository inquiryImageRepository;

    // 1:1 문의 생성
    public InquiryResponseDTO.InquiryDTO createInquiry(UserAdapter userAdapter, InquiryRequestDTO.InquiryDTO inquiryDTO, List<MultipartFile> multipartFiles) {
        User user = userRepository.findByEmail(userAdapter.getUser().getEmail()).get();

        List<String> imageUrlList = new ArrayList<>();

        int check = 0;

        for (MultipartFile image : multipartFiles) {
            if (!image.isEmpty()) check++;
        }
        if (check > 0)
            imageUrlList = awsS3Service.uploadProductFiles(multipartFiles);

        Inquiry inquiry = InquiryConverter.toInquiry(inquiryDTO);
        List<InquiryImage> inquiryImageList = InquiryConverter.toInquiryImageList(imageUrlList, inquiry);

        inquiryImageRepository.saveAll(inquiryImageList);
        inquiry.addUser(user);
        inquiry = inquiryRepository.save(inquiry);

        return InquiryConverter.toInquiryResponseDTO(inquiry);
    }

    @Transactional
    public InquiryResponseDTO.InquiryPreviewDTO getInquiry(UserAdapter userAdapter, Long lastInquiryId, Integer size) {
        User user = userRepository.findByEmail(userAdapter.getUser().getEmail()).get();
        Slice<Inquiry> inquiryPage = inquiryRepository.findSliceByIdLessThanAndUser(lastInquiryId, user,
                PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        return InquiryConverter.toInquiryPreviewDTO(inquiryPage);
    }

    public String deleteInquiry(UserAdapter userAdapter, Long inquiryId) {
        User user = userAdapter.getUser();
        inquiryRepository.deleteByIdAndUser(inquiryId, user);
        return inquiryId + "삭제 완료";
    }
}
