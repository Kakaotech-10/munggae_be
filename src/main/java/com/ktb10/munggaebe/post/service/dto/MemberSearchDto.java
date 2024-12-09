package com.ktb10.munggaebe.post.service.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSearchDto {
    private String name;
    private String alias;

    @Builder
    public MemberSearchDto(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }
}
