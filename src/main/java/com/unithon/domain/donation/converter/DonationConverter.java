package com.unithon.domain.donation.converter;

import com.unithon.domain.donation.domain.Donation;
import com.unithon.domain.donation.dto.DonationDTO;
import com.unithon.domain.user.domain.entity.User;

public class DonationConverter {
    /**
     * 생성된 Donation 엔티티를 DonationResponse DTO로 변환합니다.
     * @param donation 방금 저장된 Donation 객체
     * @return 생성된 donation의 ID를 담은 DTO
     */
    public static DonationDTO.DonationResponse toDonationResponse(Donation donation) {
        return DonationDTO.DonationResponse.builder()
                .createdDonationId(donation.getId())
                .build();
    }

    /**
     * 조회된 User 엔티티와 총 후원액, 순위를 DTO로 변환합니다.
     * @param rank 순위 (1, 2, 3)
     * @param user User 엔티티
     * @param totalAmount 총 후원 금액
     * @return TopDonorResponse DTO
     */
    public static DonationDTO.TopDonorResponse toTopDonorResponse(int rank, User user, Long totalAmount) {
        String maskedEmail = maskEmail(user.getEmail());

        return DonationDTO.TopDonorResponse.builder()
                .rank(rank)
                .userEmail(maskedEmail) // 마스킹 이메일
                .totalAmount(totalAmount)
                .build();
    }

    /**
     * 이메일 주소의 끝 3자리를 "***"로 마스킹하는 헬퍼 메서드
     * @param email 원본 이메일 문자열
     * @return 마스킹 처리된 이메일 문자열
     */
    private static String maskEmail(String email) {
        // 이메일이 null이거나 3글자 이하일 경우의 예외 처리
        if (email == null || email.length() <= 3) {
            return "***";
        }

        // 끝 3자리를 제외한 앞부분 + "***"를 조합하여 반환
        return email.substring(0, email.length() - 3) + "***";
    }
}
