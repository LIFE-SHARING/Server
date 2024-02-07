package com.umc.lifesharing.inquiry.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.GeneralException;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.inquiry.converter.InquiryConverter;
import com.umc.lifesharing.inquiry.dto.InquiryRequestDTO;
import com.umc.lifesharing.inquiry.dto.InquiryResponseDTO;
import com.umc.lifesharing.inquiry.entity.Inquiry;
import com.umc.lifesharing.inquiry.entity.InquiryImage;
import com.umc.lifesharing.inquiry.entity.Reply;
import com.umc.lifesharing.inquiry.repository.InquiryImageRepository;
import com.umc.lifesharing.inquiry.repository.InquiryRepository;
import com.umc.lifesharing.inquiry.repository.ReplyRepository;
import com.umc.lifesharing.s3.AwsS3Service;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Transactional(readOnly = true)
public class InquiryService {
    private final AwsS3Service awsS3Service;
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final InquiryImageRepository inquiryImageRepository;
    private final ReplyRepository replyRepository;

    // 1:1 문의 생성
    @Transactional
    public InquiryResponseDTO.InquiryDTO createInquiry(UserAdapter userAdapter, InquiryRequestDTO.InquiryDTO inquiryDTO, List<MultipartFile> multipartFiles) {
        User user = findUser(userAdapter);
        List<String> imageUrlList = uploadImages(multipartFiles);

        Inquiry inquiry = InquiryConverter.toInquiry(inquiryDTO);
        List<InquiryImage> inquiryImageList = InquiryConverter.toInquiryImageList(imageUrlList, inquiry);

        inquiryImageRepository.saveAll(inquiryImageList);
        inquiry.addUser(user);
        inquiry = inquiryRepository.save(inquiry);

        return InquiryConverter.toInquiryResponseDTO(inquiry);
    }

    public InquiryResponseDTO.InquiryPreviewDTO getInquiryList(UserAdapter userAdapter, Long lastInquiryId, Integer size) {
        User user = findUser(userAdapter);
        Slice<Inquiry> inquiryPage = inquiryRepository.findSliceByIdLessThanAndUser(lastInquiryId, user,
                PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        return InquiryConverter.toInquiryPreviewDTO(inquiryPage);
    }

    @Transactional
    public String deleteInquiry(UserAdapter userAdapter, Long inquiryId) {
        User user = userAdapter.getUser();
        inquiryRepository.deleteByIdAndUser(inquiryId, user);
        return inquiryId + "삭제 완료";
    }

    public InquiryResponseDTO.InquiryAndReplyDTO getInquiry(UserAdapter userAdapter, Long inquiryId) {
        User user = findUser(userAdapter);
        Inquiry inquiry = findInquiry(inquiryId, user);
        Reply reply = inquiry.getState().equals("대기") ? null : inquiry.getReply();
        return InquiryConverter.toInquiryAndReplayDTO(reply, inquiry);
    }

    @Transactional
    public InquiryResponseDTO.InquiryAndReplyDTO createReply(InquiryRequestDTO.ReplyDTO replyDTO, Long inquiryId) {
        Inquiry inquiry = findInquiry(inquiryId);
        Reply reply = findOrCreateReply(inquiry, replyDTO);

        inquiry.updateState();

        reply = replyRepository.save(reply);
        inquiry = inquiryRepository.save(inquiry.addReply(reply));

        return InquiryConverter.toInquiryAndReplayDTO(reply, inquiry);
    }

    private List<String> uploadImages(List<MultipartFile> multipartFiles) {
        int check = 0;
        List<String> imageUrlList = new ArrayList<>();

        for (MultipartFile image : multipartFiles) {
            if (!image.isEmpty()) check++;
        }
        if (check > 0)
            imageUrlList = awsS3Service.uploadProductFiles(multipartFiles);

        return imageUrlList;
    }

    // 중복검사, 있으면 body 업데이트, 없으면 만들어서 반환
    private Reply findOrCreateReply(Inquiry inquiry, InquiryRequestDTO.ReplyDTO replyDTO) {
        Reply reply = replyRepository.findByInquiry(inquiry).orElseGet(() -> Reply.builder()
                .inquiry(inquiry)
                .body(replyDTO.getBody())
                .build());
        reply.updateBody(replyDTO.getBody());
        return reply;
    }

    private Inquiry findInquiry(Long inquiryId) {
        return inquiryRepository.findById(inquiryId).orElseThrow(() -> new GeneralException(ErrorStatus.INQUIRY_NOT_FOUND));
    }

    private Inquiry findInquiry(Long inquiryId, User user) {
        return inquiryRepository.findByIdAndUser(inquiryId, user).orElseThrow(() -> new GeneralException(ErrorStatus.INQUIRY_NOT_FOUND));
    }

    private User findUser(UserAdapter userAdapter) {
        return userRepository.findByEmail(userAdapter.getUser().getEmail()).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));
    }
}
