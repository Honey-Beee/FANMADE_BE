package com.unithon.domain.user.controller;

import com.unithon.domain.user.application.ArtistService;
import com.unithon.domain.user.dto.ArtistDTO;
import com.unithon.domain.user.dto.UserDTO;
import com.unithon.global.common.BaseResponse;
import com.unithon.global.error.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;

    /**
     * 아티스트 검색 API
     * @param keyword 검색어
     */
    @GetMapping("/search")
    public BaseResponse<List<ArtistDTO.ArtistResponse>> searchArtists(
            @RequestParam(name = "keyword") String keyword
    ) {
        List<ArtistDTO.ArtistResponse> response = artistService.searchArtists(keyword);
        return BaseResponse.onSuccess(SuccessStatus.ARTIST_SEARCH_SUCCESS, response);
    }
}

