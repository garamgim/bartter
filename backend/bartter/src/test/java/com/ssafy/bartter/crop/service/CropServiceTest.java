package com.ssafy.bartter.crop.service;

import com.ssafy.bartter.domain.crop.entity.Crop;
import com.ssafy.bartter.domain.crop.entity.CropCategory;
import com.ssafy.bartter.domain.crop.repository.CropCategoryRepository;
import com.ssafy.bartter.domain.crop.repository.CropRepository;
import com.ssafy.bartter.domain.crop.service.CropService;
import com.ssafy.bartter.global.service.S3UploadService;
import com.ssafy.bartter.domain.user.entity.User;
import com.ssafy.bartter.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

import static com.ssafy.bartter.domain.crop.dto.CropDto.Create;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @Author 김가람
 *
 * CropService 테스트
 * */
@ExtendWith(MockitoExtension.class)
class CropServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CropCategoryRepository cropCategoryRepository;

    @Mock
    private S3UploadService s3UploadService;

    @Mock
    private CropRepository cropRepository;

    @InjectMocks
    private CropService cropService;

    @DisplayName("요청 본문에 담긴 정보를 사용해 농작물 프로필을 생성하면, 생성된 농작물 프로필은 요청 본문과 같은 정보를 반환한다.")
    @Test
    void 농작물_프로필_생성() {
        // given

        // 농작물 프로필 정보
        Integer userId = 1;
        Integer cropCategoryId = 1;
        MultipartFile image = mock(MultipartFile.class);
        String nickname = "new_nickname";
        String description = "new_description";
        LocalDate growDate = LocalDate.of(2024, 1, 1);

        // User, CropCategory 객체 Mocking
        User mockUser = mock(User.class);
        CropCategory mockCropCategory = mock(CropCategory.class);

        Create request = new Create(cropCategoryId, nickname, growDate, description);

        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
        given(cropCategoryRepository.findById(cropCategoryId)).willReturn(Optional.of(mockCropCategory));
        given(s3UploadService.upload(image)).willReturn("testurl");

        // when
        Crop crop = cropService.createCrop(request, image, 1);

        // then
        assertThat(crop).isNotNull();
        assertThat(crop.getUser()).isEqualTo(mockUser);
        assertThat(crop.getCategory()).isEqualTo(mockCropCategory);
        assertThat(crop.getImage()).isEqualTo("testurl");
        assertThat(crop.getNickname()).isEqualTo(nickname);
        assertThat(crop.getDescription()).isEqualTo(description);
        assertThat(crop.getGrowDate()).isEqualTo(growDate);
    }

    @DisplayName("농작물의 PK로 농작물 프로필을 조회한다.")
    @Test
    void 농작물_프로필_조회() {
        // given
        Crop crop = mock(Crop.class);
        given(cropRepository.findById(1)).willReturn(Optional.of(crop));
        given(crop.getId()).willReturn(1);

        // when
        Crop findCrop = cropService.getCrop(1);

        // then
        assertThat(findCrop).isNotNull();
        assertThat(findCrop).isEqualTo(crop);
        assertThat(findCrop.getId()).isEqualTo(1);
    }
}