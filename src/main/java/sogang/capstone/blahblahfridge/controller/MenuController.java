package sogang.capstone.blahblahfridge.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sogang.capstone.blahblahfridge.domain.Menu;
import sogang.capstone.blahblahfridge.domain.MenuIngredient;
import sogang.capstone.blahblahfridge.domain.Review;
import sogang.capstone.blahblahfridge.dto.MenuDTO;
import sogang.capstone.blahblahfridge.dto.MenuIngredientDTO;
import sogang.capstone.blahblahfridge.dto.ReviewDTO;
import sogang.capstone.blahblahfridge.persistence.MenuIngredientRepository;
import sogang.capstone.blahblahfridge.persistence.MenuRepository;
import sogang.capstone.blahblahfridge.persistence.ReviewRepository;

@Log
@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    MenuRepository repo;

    @Autowired
    MenuIngredientRepository miRepo;

    @Autowired
    ReviewRepository rRepo;

    @GetMapping(produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<MenuDTO> findAllMenu() {
        List<MenuDTO> menuDTOList = new ArrayList<>();
        List<Menu> menuList = repo.findAll();

        for (Menu menu : menuList) {
            menuDTOList.add(new MenuDTO(menu));
        }

        return menuDTOList;
    }

    @GetMapping(value = "/{name}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public MenuDTO getMenuByName(@PathVariable("name") String name) {
        Optional<Menu> menu = repo.findByName(name);
        if (menu.isEmpty()) {
            throw new RuntimeException();
        } else {
            MenuDTO menuDTO = new MenuDTO(menu.get());
            return menuDTO;
        }
    }

    @GetMapping(value = "/detail/{id}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public MenuDTO getMenuById(@PathVariable("id") Long id) {
        Optional<Menu> menu = repo.findById(id);
        if (menu.isEmpty()) {
            throw new RuntimeException();
        } else {
            MenuDTO menuDTO = new MenuDTO(menu.get());
            return menuDTO;
        }
    }

    @GetMapping(value = "/detail/{id}/ingredient", produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<MenuIngredientDTO> getMenuIngredientById(@PathVariable("id") Long id) {
        List<MenuIngredientDTO> menuIngredientDTOList = new ArrayList<>();
        List<MenuIngredient> menuIngredientList = miRepo.findAllByMenuId(id);

        for (MenuIngredient menuIngredient : menuIngredientList) {
            menuIngredientDTOList.add(new MenuIngredientDTO(menuIngredient));
        }

        return menuIngredientDTOList;
    }

    @GetMapping(value = "/detail/{id}/review", produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<ReviewDTO> getReviewById(@PathVariable("id") Long id) {
        List<ReviewDTO> reviewDTOList = new ArrayList<>();
        List<Review> reviewList = rRepo.findAllByMenuId(id);

        for (Review review : reviewList) {
            reviewDTOList.add(new ReviewDTO(review));
        }

        return reviewDTOList;
    }
}