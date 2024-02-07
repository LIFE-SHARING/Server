package com.umc.lifesharing.user.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.inquiry.converter.InquiryConverter;
import com.umc.lifesharing.inquiry.dto.InquiryRequestDTO;
import com.umc.lifesharing.inquiry.dto.InquiryResponseDTO;
import com.umc.lifesharing.inquiry.repository.InquiryImageRepository;
import com.umc.lifesharing.inquiry.repository.InquiryRepository;
import com.umc.lifesharing.location.entity.Location;
import com.umc.lifesharing.location.repository.LocationRepository;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.config.security.*;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.product.service.ProductCommandService;
import com.umc.lifesharing.reservation.entity.Reservation;
import com.umc.lifesharing.reservation.entity.enum_class.Status;
import com.umc.lifesharing.reservation.repository.ReservationRepository;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.rerpository.ReviewRepository;
import com.umc.lifesharing.review.service.ReviewCommandService;
import com.umc.lifesharing.s3.AwsS3Service;
import com.umc.lifesharing.user.converter.UserConverter;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.inquiry.entity.Inquiry;
import com.umc.lifesharing.inquiry.entity.InquiryImage;
import com.umc.lifesharing.user.entity.Roles;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.RolesRepository;
import com.umc.lifesharing.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    private final AwsS3Service awsS3Service;
    private final ProductCommandService productCommandService;
    private final ReviewCommandService reviewCommandService;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final LocationRepository locationRepository;


    @Value("${s3.url}")
    private String url;

    @Override
    public UserResponseDTO.ResponseDTO join(UserRequestDTO.JoinDTO joinDTO, MultipartFile multipartFile) {
        if (emailDuplicated(joinDTO.getEmail()) || nicknameDuplicated(joinDTO.getName())) {
            throw new UserHandler(ErrorStatus.DUPLICATED_EMAIL_OR_NICKNAME);
        }

        String imageUrl = getImageUrl(multipartFile);
        User user = UserConverter.toUser(joinDTO, passwordEncoder, imageUrl);
        Roles roles = new Roles().addUser(user);
        Location location = new Location().addUser(user);

        rolesRepository.save(roles);
        locationRepository.save(location);
        user = userRepository.save(user);

        TokenDTO tokenDTO = jwtProvider.generateTokenByUser(user);

        return UserConverter.toResponseDTO(user, tokenDTO);
    }

    @Override
    public UserResponseDTO.ResponseDTO login(UserRequestDTO.LoginDTO loginDTO) {
        User user = validUserByEmail(loginDTO.getEmail());

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.INVALID_PASSWORD);
        }

        TokenDTO tokenDTO = jwtProvider.generateTokenByUser(user);
        return UserConverter.toResponseDTO(user, tokenDTO);
    }

    @Override
    public UserResponseDTO.ChangePasswordResponseDTO changePassword(UserAdapter userAdapter, UserRequestDTO.ChangePasswordDTO changePasswordDTO) {
        // 변경 감지를 위해..
        User user = userRepository.findByEmail(userAdapter.getUser().getEmail()).get();

        // 비밀번호 불일치
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.INVALID_PASSWORD);
        }

        user.updatePassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);

        return UserResponseDTO.ChangePasswordResponseDTO.builder()
                .isChanged(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public String getAdminRole(UserAdapter userAdapter) {
        try {
            User user = validUserByEmail(userAdapter.getUser().getEmail());
            Roles roles = user.getRoles().get(0);
            roles.toAdmin();
            rolesRepository.save(roles);
            return user.getEmail() + "에 admin 권한 부여 완료";
        } catch (Exception e) {
            throw new UserHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    private User validUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));
    }

    private boolean emailDuplicated(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean nicknameDuplicated(String nickname) {
        return userRepository.existsByName(nickname);
    }

    @Override
    public List<Product> getProductList(UserAdapter userAdapter, String filter) {
        Long userId = userAdapter.getUser().getId();
//        Optional<User> mem = userRepository.findById(userId.getId());

        List<Product> productList = null;

        if (filter.equals("recent")) {
            productList = productRepository.findByUserIdOrderByCreatedAtDesc(userId);
        } else if (filter.equals("popular")) {
            productList = productRepository.findByUserIdOrderByScoreDesc(userId);
        } else if (filter.equals("review")) {
            productList = productRepository.findByUserIdOrderByReviewCountDesc(userId);
        }

        return productList;
    }


    private String getImageUrl(MultipartFile multipartFile) {
        String url;
        if (!multipartFile.isEmpty())
            url = awsS3Service.uploadUserFile(Arrays.asList(multipartFile));
        else
            url = "";   // TODO: 기본 image url로 변경
        return url;
    }

//    @Override
//    public NoticeResponse.CreateSuccessDTO createNotice(NoticeRequest.CreateDTO createDTO) {
//    }

    @Override
    public List<Product> getProductsByUserId(Long userId) {
        // userRepository에서 사용자를 가져온 후 해당 사용자가 등록한 제품들을 반환하는 코드
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            return productRepository.findAllByUser(user);
        } else {
            // 사용자를 찾을 수 없을 경우 빈 리스트 반환 또는 예외 처리 등
            return Collections.emptyList();
        }
    }

    @Override
    public UserResponseDTO.ProductPreviewListDTO getOtherProduct(Long userId, UserAdapter userAdapter) {
        User otherUser = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));
        User loggedInUser = userRepository.findById(userAdapter.getUser().getId()).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));

        // 대여자가 등록한 제품 목록을 가져오는 코드
        List<Product> productList = getProductsByUserId(userId);

        UserResponseDTO.ProductPreviewListDTO list = UserConverter.productPreviewListDTO(productList);
        return list;
    }

    @Override
    public UserResponseDTO.ProductPreviewListDTO getOtherRentProduct(Long userId, UserAdapter userAdapter) {
        User otherUser = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));

        // 반환할 리스트
        List<UserResponseDTO.ProductPreviewDTO> rentingList = new ArrayList<>();

        List<Product> productList = getProductsByUserId(userId);
        List<Reservation> reservationList = reservationRepository.findAllByProductIn(productList);

        //제품 추가
//        rentingList = reservationList.stream()
//                .map(reservation -> {
//                    UserResponseDTO.ProductPreviewDTO toProductDto = UserConverter.otherRentingProduct(reservation.getProduct(), null);
//                    return toProductDto;
//                })
//                .collect(Collectors.toList());

        // 현재 날짜 구하기
        LocalDateTime now = LocalDateTime.now();

        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (Reservation r : reservationList) {
            if (now.isAfter(r.getStartDate()) && now.isBefore(r.getEndDate())) {
                // 포맷 적용
                String start = r.getStartDate().format(formatter);
                String end = r.getEndDate().format(formatter);
                String lentDate = start + " - " + end;

                // 이미 제품이 리스트에 추가되었는지 확인
                boolean productAlreadyAdded = false;
                for (UserResponseDTO.ProductPreviewDTO p : rentingList) {
                    if (p.getProductId().equals(r.getProduct().getId())) {
                        p.setIsReserved(lentDate); // 대여 시작일-종료일과 대여중임을 표시
                        productAlreadyAdded = false;
                        break;
                    }
                }

                // 제품이 리스트에 추가되지 않은 경우 새로운 DTO를 생성하여 추가
                if (!productAlreadyAdded) {
                    UserResponseDTO.ProductPreviewDTO toProductDto = UserConverter.otherRentingProduct(r.getProduct(), lentDate);
                    rentingList.add(toProductDto);
                }
            }
        }

        return UserConverter.otherRentingProductList(rentingList);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        // 사용자를 가져온 후 해당 사용자가 등록한 제품들을 반환
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            return reviewRepository.findByProductId(productId);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public UserResponseDTO.UserReviewListDTO getOtherReview(Long userId, UserAdapter userAdapter) {
        User otherUser = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));
        User loggedInUser = userRepository.findById(userAdapter.getUser().getId()).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));

        List<Product> productList = productRepository.findAllByUser(otherUser);
        /// 각 제품에 대한 리뷰 목록을 가져오는 코드
        List<Review> reviewList = productList.stream()
                .flatMap(product -> getReviewByProductId(product.getId()).stream())
                .collect(Collectors.toList());

        UserResponseDTO.UserReviewListDTO userReviewListDTO = UserConverter.otherUserReviewListDTO(reviewList);
        return userReviewListDTO;
    }

    @Override
    public UserResponseDTO.UserProfileDTO getOtherProfile(Long userId, UserAdapter userAdapter) {
        User otherUser = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));
        User loggedInUser = userRepository.findById(userAdapter.getUser().getId()).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));

        String imageUrl = (otherUser.getProfileUrl() == null) ? null : url + otherUser.getProfileUrl();

        List<Product> productList = productRepository.findAllByUser(otherUser);
        /// 각 제품에 대한 리뷰 목록을 가져오는 코드
        List<Review> reviewList = productList.stream()
                .flatMap(product -> getReviewByProductId(product.getId()).stream())
                .collect(Collectors.toList());

        // 반환할 리스트
        List<UserResponseDTO.ProductPreviewDTO> rentingList = new ArrayList<>();

        List<Reservation> reservationList = reservationRepository.findAllByProductInAndStatus(productList, Status.ACTIVE);

        //제품 추가
        rentingList = reservationList.stream()
                .map(reservation -> {
                    UserResponseDTO.ProductPreviewDTO toProductDto = UserConverter.otherRentingProduct(reservation.getProduct(), null);
                    return toProductDto;
                })
                .collect(Collectors.toList());

        Integer averageScore = productCommandService.otherAverageScoreByUserId(otherUser.getId());

        UserResponseDTO.UserProfileDTO userProfile = UserConverter.otherUserProfileDTO(otherUser, averageScore, reviewList.size(), productList.size(), rentingList.size(), imageUrl);
        return userProfile;
    }
}
