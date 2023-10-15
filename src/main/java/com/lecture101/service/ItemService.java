package com.lecture101.service;

import com.lecture101.dto.ItemFormDto;
import com.lecture101.dto.ItemImgDto;
import com.lecture101.dto.ItemSearchDto;
import com.lecture101.dto.MainItemDto;
import com.lecture101.entity.Item;
import com.lecture101.entity.ItemImg;
import com.lecture101.repository.ItemImgRepository;
import com.lecture101.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Item item = itemFormDto.createItem();

        /*현석 파트 시작*/
        //날짜/시간 추가한 작업 시작 부분
        LocalDate classStartDate = itemFormDto.getClassStartDateAsLocalDate();
        LocalDate classEndDate = itemFormDto.getClassEndDateAsLocalDate();
        item.setClassStartDate(classStartDate);
        item.setClassEndDate(classEndDate);
        itemRepository.save(item);
        //날짜/시간 추가한 작업 끝 부분
        /*현석 파트 끝*/

        itemRepository.save(item);

        // 이미지 등록
        /*if (itemImgFileList.isEmpty()) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            itemImg.setRepimgYn("Y"); // 첫 번째 이미지를 대표 이미지로 설정

            // 더미 이미지의 경로를 설정
            itemImg.setFilePath("/images/noimg.jpg");

            itemImgService.saveItemImg(itemImg, null); // 이미지 파일이 없으므로 두 번째 파라미터는 null로 전달
        } else {
            for (int i = 0; i < itemImgFileList.size(); i++) {
                ItemImg itemImg = new ItemImg();
                itemImg.setItem(item);

                if (i == 0)
                    itemImg.setRepimgYn("Y");
                else
                    itemImg.setRepimgYn("N");

                MultipartFile imgFile = itemImgFileList.get(i);

                if (imgFile.isEmpty()) {
                    // 이미지가 업로드되지 않은 경우 더미 이미지를 사용
                    itemImg.setFilePath("/images/noimg.jpg");
                } else {
                    // 이미지가 업로드된 경우 실제 파일을 저장하고 그 경로를 설정
                    String filePath = "/images/item/" + imgFile.getOriginalFilename();
                    // 여기에서 실제 파일을 저장하는 코드를 작성해야 합니다.
                    itemImg.setFilePath(filePath);
                }

                itemImgService.saveItemImg(itemImg, imgFile);
            }
        }*/

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

}