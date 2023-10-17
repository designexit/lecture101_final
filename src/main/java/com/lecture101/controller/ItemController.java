package com.lecture101.controller;

import com.lecture101.dto.CommentDTO;
import com.lecture101.dto.ItemFormDto;
import com.lecture101.dto.ItemSearchDto;
import com.lecture101.entity.Item;
import com.lecture101.entity.Member;
import com.lecture101.service.CommentService;
import com.lecture101.service.ItemService;
import com.lecture101.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

        /*현석 파트 시작*/
        //날짜/시간 추가한 작업 시작 부분
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                          @RequestParam("classStartDate") String classStartDateStr,
                          @RequestParam("classEndDate") String classEndDateStr) {

        itemFormDto.setClassStartDate(classStartDateStr);
        itemFormDto.setClassEndDate(classEndDateStr);
        //날짜/시간 추가한 작업 끝 부분
        /*현석 파트 끝*/

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        /*//이미지 3개 등록
        if(itemImgFileList.size() < 3) {
            model.addAttribute("errorMessage", "이미지는 최소 3개가 필요합니다.");
            return "item/itemForm";
        }*/

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }


    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        /*if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");*/
        if(itemImgFileList.size() < 3 || itemImgFileList.get(0).isEmpty() || itemImgFileList.get(2).isEmpty()){
            model.addAttribute("errorMessage", "세 번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";
    }

    // 1011 ktb 수정
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId,
                          @AuthenticationPrincipal User user,
                          @RequestParam(required = false, defaultValue = "0") int page) {

        //각각의 페이지에 해당하는 댓글객체를 담아서 전달
        Pageable pageable = PageRequest.of(page, 5);
        Page<CommentDTO> commentsPage = commentService.findCommentsByItemId(itemId, pageable);
        model.addAttribute("commentsPage", commentsPage);

        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);

        if (user != null) {
            Member member = memberService.findByEmail2(user.getUsername());
            model.addAttribute("member", member);
        }
        return "item/itemDtl";
    }



}