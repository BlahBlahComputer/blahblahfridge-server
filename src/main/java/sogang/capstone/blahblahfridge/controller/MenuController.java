package sogang.capstone.blahblahfridge.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.domain.Menu;
import sogang.capstone.blahblahfridge.domain.MenuIngredient;
import sogang.capstone.blahblahfridge.domain.Review;
import sogang.capstone.blahblahfridge.dto.MenuDTO;
import sogang.capstone.blahblahfridge.dto.MenuIngredientDTO;
import sogang.capstone.blahblahfridge.dto.ReviewDTO;
import sogang.capstone.blahblahfridge.exception.NotFoundException;
import sogang.capstone.blahblahfridge.persistence.MenuIngredientRepository;
import sogang.capstone.blahblahfridge.persistence.MenuRepository;
import sogang.capstone.blahblahfridge.persistence.ReviewRepository;

@Log
@Controller
@RequestMapping("menu")
@AllArgsConstructor
public class MenuController {

    MenuRepository repo;
    MenuIngredientRepository miRepo;
    ReviewRepository rRepo;

    @GetMapping(produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<List<MenuDTO>> findAllMenu() {
        List<Menu> menuList = repo.findAll();
        List<MenuDTO> menuDTOList = menuList.stream()
            .map(MenuDTO::new)
            .collect(Collectors.toList());

        return CommonResponse.onSuccess(menuDTOList);
    }

    @GetMapping(value = "/search", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<List<MenuDTO>> getMenuByName(@RequestParam("name") String name) {
        List<Menu> menuList = repo.findAllByNameContaining(name);
        List<MenuDTO> menuDTOList = menuList.stream()
            .map(MenuDTO::new)
            .collect(Collectors.toList());

        return CommonResponse.onSuccess(menuDTOList);
    }

    @GetMapping(value = "/{id}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<MenuDTO> getMenuById(@PathVariable("id") Long id) {
        Optional<Menu> menu = repo.findById(id);
        if (menu.isEmpty()) {
            throw new NotFoundException("해당 메뉴가 없습니다.");
        }

        MenuDTO menuDTO = new MenuDTO(menu.get());
        return CommonResponse.onSuccess(menuDTO);
    }

    @GetMapping(value = "/{id}/ingredient", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<List<MenuIngredientDTO>> getMenuIngredientByMenuId(
        @PathVariable("id") Long id) {
        List<MenuIngredient> menuIngredientList = miRepo.findAllByMenuId(id);
        List<MenuIngredientDTO> menuIngredientDTOList = menuIngredientList.stream()
            .map(MenuIngredientDTO::new)
            .collect(Collectors.toList());

        return CommonResponse.onSuccess(menuIngredientDTOList);
    }

    @GetMapping(value = "/{id}/review", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<List<ReviewDTO>> getReviewByMenuId(@PathVariable("id") Long id) {
        List<Review> reviewList = rRepo.findAllByMenuId(id);
        List<ReviewDTO> reviewDTOList = reviewList.stream()
            .map(ReviewDTO::new)
            .collect(Collectors.toList());

        return CommonResponse.onSuccess(reviewDTOList);
    }
}