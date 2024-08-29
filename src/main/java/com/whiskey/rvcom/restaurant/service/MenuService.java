package com.whiskey.rvcom.restaurant.service;

import com.whiskey.rvcom.entity.restaurant.menu.Menu;
import com.whiskey.rvcom.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Menu getMenuData(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    public void deleteMenuById(Long id) {
        menuRepository.deleteById(id);
    }
}
