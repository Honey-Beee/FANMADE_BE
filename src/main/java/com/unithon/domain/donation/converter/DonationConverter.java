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
     * 예: user@email.com -> us***@email.com
     * @param email 원본 이메일 문자열
     * @return 마스킹 처리된 이메일 문자열
     */
    private static String maskEmail(String email) {
        // 1. 이메일이 null이거나 '@'가 없는 비정상적인 경우, 안전하게 기본값 반환
        if (email == null || !email.contains("@")) {
            return "***";
        }

        // 2. '@'를 기준으로 아이디 부분과 도메인 부분을 분리
        int atIndex = email.indexOf("@");
        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex); // '@' 포함한 뒷부분

        // 3. 아이디(username) 부분의 길이에 따라 마스킹 처리
        String maskedUsername;
        if (username.length() <= 2) {
            // 아이디가 2글자 이하이면, 마스킹하지 않고 그대로 사용 (예: us@email.com)
            maskedUsername = username;
        } else {
            // 아이디가 3글자 이상이면, 앞 2글자만 남기고 나머지를 "***"로 대체
            maskedUsername = username.substring(0, 2) + "***";
        }

        // 4. 마스킹된 아이디와 원래 도메인을 합쳐서 최종 결과 반환
        return maskedUsername + domain;
    }
}
