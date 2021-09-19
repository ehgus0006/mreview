package org.zerock.mreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "movie")
public class MovieImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    // java.util.UUID를 이용해서 고유한 번호를 생성해서 사용할 것이다.
    private String uuid;

    private String imaName;

    // 저장 경로(path)는 년/월/일 폴더 구조
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
}
