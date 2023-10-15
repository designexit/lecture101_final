package com.lecture101.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Getter @Setter
public class ItemImg extends BaseEntity{

    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; //이미지 파일명

    private String oriImgName; //원본 이미지 파일명

    private String imgUrl; //이미지 조회 경로

    private String repimgYn; //대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    /*private String filePath;

    // 생성자
    public void ItemImage() {
        // 초기화 등의 작업을 수행할 수 있습니다.
    }

    // filePath 설정 메서드
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // filePath 가져오기 메서드 (필요하다면)
    public String getFilePath() {
        return this.filePath;
    }*/

}