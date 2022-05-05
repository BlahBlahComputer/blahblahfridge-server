package sogang.capstone.blahblahfridge.controller;

import java.util.Optional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sogang.capstone.blahblahfridge.domain.Menu;
import sogang.capstone.blahblahfridge.dto.MenuDTO;
import sogang.capstone.blahblahfridge.persistence.MenuRepository;

@Log
@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    MenuRepository repo;

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
}