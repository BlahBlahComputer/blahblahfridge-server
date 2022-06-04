package sogang.capstone.blahblahfridge.unit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.controller.MenuController;
import sogang.capstone.blahblahfridge.domain.Ingredient;
import sogang.capstone.blahblahfridge.domain.IngredientCategory;
import sogang.capstone.blahblahfridge.domain.Menu;
import sogang.capstone.blahblahfridge.domain.MenuCategory;
import sogang.capstone.blahblahfridge.domain.MenuIngredient;
import sogang.capstone.blahblahfridge.domain.Review;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.MenuDTO;
import sogang.capstone.blahblahfridge.dto.MenuIngredientDTO;
import sogang.capstone.blahblahfridge.dto.ReviewDTO;
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
        CommonResponse<List<MenuDTO>> result = menuController.findAllMenu(null);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new ArrayList<>()), result);
    }

    @Test
    @DisplayName("메뉴를 조회 결과가 있을 때, 리스트가 나오는지 확인")
    public void testIfListExistReturnMenuList() {
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
        CommonResponse<List<MenuDTO>> result = menuController.findAllMenu(null);

        // then
        MenuDTO menuDTO = new MenuDTO(menu);
        List<MenuDTO> menuDTOList = new ArrayList<MenuDTO>();
        menuDTOList.add(menuDTO);

        Assertions.assertEquals(CommonResponse.onSuccess(menuDTOList), result);
    }

    @Test
    @DisplayName("메뉴 이름 검색 시 결과가 없을 때, 빈 배열로 나오는지 확인")
    public void testIfMenuNameNotExistThenReturnEmptyList() {
        // given
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        Mockito.when(mockMenuRepository.findAllByNameContaining("된장찌개"))
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
        CommonResponse<List<MenuDTO>> result = menuController.findAllMenu("된장찌개");

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new ArrayList<>()), result);
    }

    @Test
    @DisplayName("메뉴 이름 검색 시 결과가 있을 때, 리스트가 나오는지 확인")
    public void testIfMenuNameExistReturnMenuList() {
        // given
        MenuCategory menuCategory = MenuCategory.builder()
            .id(1L)
            .name("한식")
            .build();

        Menu menu1 = Menu.builder()
            .id(1L)
            .name("메뉴1")
            .time(1)
            .recipe("레시피")
            .image("이미지")
            .menuCategory(menuCategory)
            .build();
        Menu menu2 = Menu.builder()
            .id(1L)
            .name("메뉴2")
            .time(1)
            .recipe("레시피")
            .image("이미지")
            .menuCategory(menuCategory)
            .build();
        List<Menu> menuList = new ArrayList<Menu>();
        menuList.add(menu1);
        menuList.add(menu2);

        List<MenuDTO> menuDTOList = menuList.stream()
            .map(MenuDTO::new)
            .collect(Collectors.toList());

        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        Mockito.when(mockMenuRepository.findAllByNameContaining("메뉴"))
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
        CommonResponse<List<MenuDTO>> result = menuController.findAllMenu("메뉴");

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(menuDTOList), result);
    }

    @Test
    @DisplayName("메뉴를 아이디로 검색했으나 결과가 없을 때, 예외 처리 되는지 확인")
    public void testIfMenuNotExistThenThrowException() {
        // given
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        Mockito.when(mockMenuRepository.findById(100L))
            .thenReturn(Optional.ofNullable(null));

        MenuIngredientRepository mockMenuIngredientRepository = Mockito.mock(
            MenuIngredientRepository.class);
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        MenuController menuController = new MenuController(
            mockMenuRepository,
            mockMenuIngredientRepository,
            mockReviewRepository
        );

        // then
        CommonResponse result = menuController.getMenuById(100L);
        Assertions.assertEquals(CommonResponse.onFailure(HttpStatus.NOT_FOUND, "해당 메뉴가 없습니다."),
            result);
    }

    @Test
    @DisplayName("메뉴를 아이디로 검색해서 결과가 있을 때, 결과가 나오는지 확인")
    public void testIfMenuExistThenReturnMenu() {
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

        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        Mockito.when(mockMenuRepository.findById(1L))
            .thenReturn(Optional.ofNullable(menu));

        MenuIngredientRepository mockMenuIngredientRepository = Mockito.mock(
            MenuIngredientRepository.class);
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        MenuController menuController = new MenuController(
            mockMenuRepository,
            mockMenuIngredientRepository,
            mockReviewRepository
        );
        CommonResponse<MenuDTO> result = menuController.getMenuById(1L);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new MenuDTO(menu)), result);
    }

    @Test
    @DisplayName("메뉴 아이디로 재료 조회 시 결과가 없을 때, 빈 배열로 나오는지 확인")
    public void testIfMenuIngredientNotExistThenReturnEmptyList() {
        // given
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        MenuIngredientRepository mockMenuIngredientRepository = Mockito.mock(
            MenuIngredientRepository.class);
        Mockito.when(mockMenuIngredientRepository.findAllByMenuId(1L))
            .thenReturn(new ArrayList<>());
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        MenuController menuController = new MenuController(
            mockMenuRepository,
            mockMenuIngredientRepository,
            mockReviewRepository
        );
        CommonResponse<List<MenuIngredientDTO>> menuIngredientDTOList = menuController.getMenuIngredientByMenuId(
            1L);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new ArrayList<>()), menuIngredientDTOList);
    }

    @Test
    @DisplayName("메뉴 아이디로 재료 조회 시 결과가 있을 때, 결과가 나오는지 확인")
    public void testIfMenuIngredientExistThenReturnMenuIngredientList() {
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

        IngredientCategory ingredientCategory = IngredientCategory.builder().id(1L).name("한식")
            .build();
        Ingredient ingredient1 = Ingredient.builder().id(1L).name("김치")
            .ingredientCategory(ingredientCategory).build();
        Ingredient ingredient2 = Ingredient.builder().id(2L).name("고기")
            .ingredientCategory(ingredientCategory).build();

        MenuIngredient menuIngredient1 = MenuIngredient.builder().id(1L).menu(menu)
            .ingredient(ingredient1).build();
        MenuIngredient menuIngredient2 = MenuIngredient.builder().id(2L).menu(menu)
            .ingredient(ingredient2).build();

        List<MenuIngredient> menuIngredientList = new ArrayList<MenuIngredient>();
        menuIngredientList.add(menuIngredient1);
        menuIngredientList.add(menuIngredient2);

        List<MenuIngredientDTO> menuIngredientDTOList = menuIngredientList.stream()
            .map(MenuIngredientDTO::new)
            .collect(Collectors.toList());

        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        MenuIngredientRepository mockMenuIngredientRepository = Mockito.mock(
            MenuIngredientRepository.class);
        Mockito.when(mockMenuIngredientRepository.findAllByMenuId(1L))
            .thenReturn(menuIngredientList);
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        MenuController menuController = new MenuController(
            mockMenuRepository,
            mockMenuIngredientRepository,
            mockReviewRepository
        );
        CommonResponse<List<MenuIngredientDTO>> result = menuController.getMenuIngredientByMenuId(
            1L);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(menuIngredientDTOList), result);
    }

    @Test
    @DisplayName("메뉴 아이디로 리뷰 조회 시 결과가 없을 때, 빈 배열로 나오는지 확인")
    public void testIfReviewNotExistThenReturnEmptyList() {
        // given
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        MenuIngredientRepository mockMenuIngredientRepository = Mockito.mock(
            MenuIngredientRepository.class);
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        Mockito.when(mockReviewRepository.findAllByMenuId(1L))
            .thenReturn(new ArrayList<>());

        // when
        MenuController menuController = new MenuController(
            mockMenuRepository,
            mockMenuIngredientRepository,
            mockReviewRepository
        );
        CommonResponse<List<ReviewDTO>> reviewDTOList = menuController.getReviewByMenuId(1L);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new ArrayList<>()), reviewDTOList);
    }

    @Test
    @DisplayName("메뉴 아이디로 리뷰 조회 시 결과가 있을 때, 결과가 나오는지 확인")
    public void testIfReviewExistThenReturnReviewList() {
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

        User user1 = User.builder().username("유저1").authenticationCode("123").provider("kakao")
            .build();
        User user2 = User.builder().username("유저2").authenticationCode("456").provider("kakao")
            .build();

        Review review1 = Review.builder().rate(5).content("굿").user(user1).menu(menu).build();
        Review review2 = Review.builder().rate(1).content("밷").user(user2).menu(menu).build();
        List<Review> reviewList = new ArrayList<Review>();
        reviewList.add(review1);
        reviewList.add(review2);

        List<ReviewDTO> reviewDTOList = reviewList.stream()
            .map(ReviewDTO::new)
            .collect(Collectors.toList());

        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        MenuIngredientRepository mockMenuIngredientRepository = Mockito.mock(
            MenuIngredientRepository.class);
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        Mockito.when(mockReviewRepository.findAllByMenuId(1L))
            .thenReturn(reviewList);

        // when
        MenuController menuController = new MenuController(
            mockMenuRepository,
            mockMenuIngredientRepository,
            mockReviewRepository
        );
        CommonResponse<List<ReviewDTO>> result = menuController.getReviewByMenuId(1L);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(reviewDTOList), result);
    }
}
