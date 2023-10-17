package com.lecture101.dto;


import com.lecture101.constant.Category;
import com.lecture101.constant.ItemSellStatus;
import com.lecture101.constant.LectureType;
import com.lecture101.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Size;

@Getter @Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "클래스명은 필수 입력 값 입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "클래스 소개는 필수 입력 값 입니다.")
    private String itemDetail;

    @NotNull(message = "총 인원수는 필수 입력 값입니다.")
    private Integer stockNumber;

    /*현석 파트 시작*/
    //날짜/시간 추가한 작업 시작 부분
    private String classStartDate; // 문자열로 날짜를 저장할 필드

    private String classEndDate;
    //날짜/시간 추가한 작업 끝 부분
    /*현석 파트 끝*/

    @NotNull(message = "클래스 상태는 필수 입력 값입니다.")
    private ItemSellStatus itemSellStatus;

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private Category category;

    @NotNull(message = "클래스 타입은 필수 입력 값입니다.")
    private LectureType lectureType;

    /*@Size(min = 3, message = "최소 3개의 이미지를 등록해야 합니다.") // 이미지는 최소 3개가 필요*/
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }

    /*현석 파트 시작*/
    //날짜/시간 추가한 작업 시작 부분
    public void setClassStartDate(String classStartDate) {
        this.classStartDate = classStartDate;
    }

    // 종료 날짜를 문자열로 설정
    public void setClassEndDate(String classEndDate) {
        this.classEndDate = classEndDate;
    }

    // 필요한 경우 문자열에서 LocalDate로 변환하여 반환
    public LocalDate getClassStartDateAsLocalDate() {
        if (classStartDate != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return LocalDate.parse(classStartDate, dateFormatter);
        }
        return null;
    }

    // 필요한 경우 문자열에서 LocalDate로 변환하여 반환
    public LocalDate getClassEndDateAsLocalDate() {
        if (classEndDate != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return LocalDate.parse(classEndDate, dateFormatter);
        }
        return null;
    }
    //날짜/시간 추가한 작업 끝 부분
    /*현석 파트 끝*/

}