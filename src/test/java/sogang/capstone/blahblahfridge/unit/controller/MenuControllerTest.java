package sogang.capstone.blahblahfridge.unit.controller;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sogang.capstone.blahblahfridge.controller.MenuController;
import sogang.capstone.blahblahfridge.domain.Menu;
import sogang.capstone.blahblahfridge.domain.MenuCategory;
import sogang.capstone.blahblahfridge.dto.MenuDTO;
import sogang.capstone.blahblahfridge.persistence.MenuIngredientRepository;
import sogang.capstone.blahblahfridge.persistence.MenuRepository;
import sogang.capstone.blahblahfridge.persistence.ReviewRepository;

public class MenuControllerTest {

    @Test
    @DisplayName("메뉴를 조회 했는데 아무것도 없을 경우, 빈 배열로 나오는지 확인")
    public void testIfNothingThenReturnEmptyList() {
        // given
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        Mockito.when(mockMenuRepository.findAll())
            .thenReturn(new ArrayList<>());

        MenuIngredientRepository mockMenuIngredientRepository = Mockito.mock(
            MenuIngredientRepository.class);
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        MenuController menuController = new MenuController(
            mockMenuRepository,
            mockMenuIngredientRepository,
            mockReviewRepository
        );
        List<MenuDTO> result = menuController.findAllMenu();

        // then
        Assertions.assertEquals(new ArrayList<>(), result);
    }

    @Test
    @DisplayName("메뉴를 조회 결과가 있을 때, 리스트가 나오는지 확인")
    public void testIfListExistReturnList() {
        // given
        MenuCategory menuCategory = MenuCategory.builder()
            .id(1L)
            .name("한식")
            .build();

        Menu menu = Menu.builder()
            .id(1L)
            .name("메뉴")
            .time(1)
            .recipe("레시피")
            .image("이미지")
            .menuCategory(menuCategory)
            .build();
        List<Menu> menuList = new ArrayList<Menu>();
        menuList.add(menu);

        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        Mockito.when(mockMenuRepository.findAll())
            .thenReturn(menuList);

        MenuIngredientRepository mockMenuIngredientRepository = Mockito.mock(
            MenuIngredientRepository.class);
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        MenuController menuController = new MenuController(
            mockMenuRepository,
            mockMenuIngredientRepository,
            mockReviewRepository
        );
        List<MenuDTO> result = menuController.findAllMenu();

        // then
        MenuDTO menuDTO = new MenuDTO(menu);
        ArrayList<MenuDTO> menuDTOList = new ArrayList<MenuDTO>();
        menuDTOList.add(menuDTO);
        Assertions.assertEquals(menuDTOList, result);
    }

}
